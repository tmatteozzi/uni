# SCD Type 2 - Sistema de Historial Automático

## 🎯 Qué es SCD Type 2

**Slowly Changing Dimension Type 2** es una técnica de data warehousing que mantiene el **historial completo** de cambios en las dimensiones. Cuando un registro cambia:

1. ✅ El registro antiguo se marca como **histórico** (`is_current = false`, `valid_to = timestamp`)
2. ✅ Se crea una **nueva versión** con un nuevo `surrogate_key` (`is_current = true`, `valid_to = NULL`)
3. ✅ Las nuevas transacciones automáticamente referencian la versión actual
4. ✅ Los cubos OLAP se **refrescan automáticamente**

## 📊 Estructura Implementada

### Dimensiones con Historial

Todas las dimensiones tienen estos campos adicionales:

```sql
-- Ejemplo: dwh.dim_store
CREATE TABLE dwh.dim_store (
    store_sk         bigint PRIMARY KEY,        -- Surrogate Key (auto-incrementa)
    store_id         int NOT NULL,              -- Business Key (original)
    store_name       varchar(255),              -- Atributos
    city             varchar(255),
    ...
    valid_from       timestamptz NOT NULL,      -- Inicio de validez
    valid_to         timestamptz,               -- Fin de validez (NULL = actual)
    is_current       boolean NOT NULL           -- true = versión actual
);
```

### Tablas de Hechos

Usan `surrogate keys` en lugar de business keys:

```sql
CREATE TABLE dwh.fact_bike_order (
    order_id         int,
    customer_sk      bigint,  -- ← Surrogate Key (no customer_id)
    store_sk         bigint,  -- ← Surrogate Key (no store_id)
    product_sk       bigint,  -- ← Surrogate Key (no product_id)
    staff_sk         bigint,  -- ← Surrogate Key (no staff_id)
    ...
);
```

## 🔄 Cómo Funciona el ETL

### 1. ETL de Dimensiones (con refresh automático de cubos)

```sql
-- Cuando ejecutas:
SELECT dwh.etl_dim_store();

-- El sistema automáticamente:
-- a) Detecta cambios comparando atributos
-- b) Expira versiones antiguas (is_current=false, valid_to=now())
-- c) Crea nuevas versiones (nuevo SK, is_current=true)
-- d) REFRESCA CUBOS AUTOMÁTICAMENTE ✓
```

### 2. ETL de Facts

```sql
-- Cuando ejecutas:
SELECT dwh.etl_fact_orders();

-- El sistema automáticamente:
-- a) Lee órdenes nuevas desde el sistema origen
-- b) Busca los surrogate keys ACTUALES de las dimensiones
-- c) Inserta en facts usando los SKs correctos
```

### 3. Refresh Automático de Cubos

**Los cubos se refrescan automáticamente** cuando cambias dimensiones:

- ✅ `etl_dim_customer()` → refresca cubos si hay cambios
- ✅ `etl_dim_store()` → refresca cubos si hay cambios
- ✅ `etl_dim_staff()` → refresca cubos si hay cambios
- ✅ `etl_dim_product()` → refresca cubos si hay cambios

## 🧪 Cómo Probarlo

### Prueba Rápida

```bash
# 1. Conectarse a la base de datos
docker exec -it pg_dw psql -U postgres -d bike_stores

# 2. Ejecutar el script de prueba
\i /tmp/test_auto_cube_refresh.sql
```

O desde fuera del contenedor:

```bash
# Copiar script al contenedor
docker cp test_auto_cube_refresh.sql pg_dw:/tmp/

# Ejecutar
docker exec pg_dw psql -U postgres -d bike_stores -f /tmp/test_auto_cube_refresh.sql
```

### Prueba Manual Paso a Paso

```sql
-- 1. Ver estado inicial
SELECT store_sk, store_id, store_name, is_current 
FROM dwh.dim_store WHERE store_id = 1;

-- 2. Modificar el store
UPDATE bike_stores.stores 
SET store_name = 'Santa Cruz Bikes - NUEVO NOMBRE'
WHERE store_id = 1;

-- 3. Ejecutar ETL (refresca cubos automáticamente)
SELECT dwh.etl_dim_store();

-- 4. Ver resultado (debería haber 2 versiones)
SELECT 
    store_sk,
    store_name,
    CASE WHEN is_current THEN '✓ ACTUAL' ELSE '✗ HISTÓRICO' END as estado,
    valid_from,
    valid_to
FROM dwh.dim_store 
WHERE store_id = 1
ORDER BY valid_from;

-- 5. Verificar cubos (actualizados automáticamente)
SELECT store_sk, SUM(rows) as total_rows
FROM dwh_cube.cube_bike_order
WHERE store_sk IN (1, 34) AND gid = 0
GROUP BY store_sk;
```

## 📈 Ejemplo Completo

### Escenario: Cambio de nombre de tienda

```sql
-- ANTES
store_sk | store_id | store_name       | is_current
---------|----------|------------------|------------
   1     |    1     | Santa Cruz Bikes | t

-- Ejecuto cambio:
UPDATE bike_stores.stores SET store_name = 'Santa Cruz Bikes - Renovated' WHERE store_id = 1;
SELECT dwh.etl_dim_store();

-- DESPUÉS
store_sk | store_id | store_name                  | is_current | valid_to
---------|----------|----------------------------|------------|----------
   1     |    1     | Santa Cruz Bikes           | f          | 2025-11-06
   34    |    1     | Santa Cruz Bikes - Renovated| t          | NULL
```

### Nueva Orden usa SK Correcto

```sql
-- Crear nueva orden
INSERT INTO bike_stores.orders (..., store_id = 1, ...);
SELECT dwh.etl_fact_orders();

-- Verificar
SELECT order_id, store_sk FROM dwh.fact_bike_order WHERE order_id = 1617;
-- Resultado: usa store_sk = 34 (el nuevo) ✓
```

### Cubos Actualizados Automáticamente

```sql
-- Los cubos se refrescaron automáticamente al ejecutar etl_dim_store()
SELECT store_sk, SUM(rows) FROM dwh_cube.cube_bike_order 
WHERE store_sk IN (1, 34) AND gid = 0 
GROUP BY store_sk;

-- Resultado:
store_sk | rows
---------|------
   1     | 1006  (órdenes antiguas)
   34    | 1     (orden nueva)
```

## 🔍 Consultas Útiles

### Ver historial de una dimensión

```sql
-- Ver todas las versiones de un store
SELECT 
    store_sk,
    store_name,
    valid_from,
    COALESCE(valid_to::text, 'CURRENT') as valid_to,
    CASE WHEN is_current THEN '✓ Activa' ELSE '✗ Histórica' END as estado
FROM dwh.dim_store
WHERE store_id = 1
ORDER BY valid_from;
```

### Análisis temporal (Point-in-Time Query)

```sql
-- ¿Cómo era el producto X el 2020-05-15?
SELECT * 
FROM dwh.dim_product
WHERE product_id = 1 
  AND valid_from <= '2020-05-15'
  AND (valid_to > '2020-05-15' OR valid_to IS NULL);
```

### Estadísticas de cambios

```sql
-- ¿Cuántas versiones tengo por dimensión?
SELECT 
    'Customers' as dimension,
    COUNT(*) as total_versions,
    COUNT(*) FILTER (WHERE is_current) as current,
    COUNT(*) FILTER (WHERE NOT is_current) as historical
FROM dwh.dim_customer
UNION ALL
SELECT 'Stores', COUNT(*), COUNT(*) FILTER (WHERE is_current), 
       COUNT(*) FILTER (WHERE NOT is_current)
FROM dwh.dim_store
UNION ALL
SELECT 'Products', COUNT(*), COUNT(*) FILTER (WHERE is_current),
       COUNT(*) FILTER (WHERE NOT is_current)
FROM dwh.dim_product;
```

## ⚙️ Archivos Modificados

### Dimensiones
- `dwh/create_dimensions.sql` - Agregado: SK, valid_from, valid_to, is_current

### Facts
- `dwh/create_facts.sql` - Usan surrogate keys en lugar de business keys

### ETL
- `etl/etl_dim_customer_store_staff.sql` - SCD Type 2 + refresh automático
- `etl/etl_dim_product.sql` - SCD Type 2 + refresh automático
- `etl/etl_fact_orders.sql` - Usan surrogate keys actuales

### Cubos
- `dwh/create_cubes.sql` - Usan surrogate keys
- `etl/etl_refresh_cubes.sql` - Función de refresh (llamada automáticamente)

## 🎓 Ventajas del Sistema

1. **✅ Historial Completo**: Nunca pierdes datos antiguos
2. **✅ Auditoría**: Puedes ver cómo era la data en cualquier momento
3. **✅ Análisis Temporal**: Reportes históricos precisos
4. **✅ Automático**: Los cubos se actualizan solos
5. **✅ Integridad**: Las facts siempre apuntan a la versión correcta

## 🚀 Próximos Pasos

Para mejorar aún más el sistema:

1. **Late-Arriving Facts**: Manejar facts que llegan con fechas pasadas
2. **Type 3 Híbrido**: Mantener "previous_value" para cambios frecuentes
3. **Particionamiento**: Particionar facts por fecha para mejor performance
4. **CDC (Change Data Capture)**: Detectar cambios en tiempo real

## 📞 Soporte

Para dudas sobre el sistema SCD Type 2:
- Revisar logs: `SELECT * FROM etl.etl_runs ORDER BY finished_at DESC LIMIT 20;`
- Ver errores: `SELECT * FROM etl.etl_runs WHERE status != 'OK';`

