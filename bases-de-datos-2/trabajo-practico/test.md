# Pruebas del Sistema ETL - Bike Stores

Este archivo contiene todos los scripts necesarios para probar que el sistema ETL funciona correctamente.

## Configuración Inicial

```bash
# Configurar contraseña de PostgreSQL
export PGPASSWORD=postgres

# Conectar a la base de datos
psql -h localhost -p 5435 -U postgres -d bike_stores
```

## 1. Verificar Triggers Existentes

```sql
-- Verificar que todos los triggers están funcionando
SELECT t.tgname as trigger_name, c.relname as table_name, t.tgenabled as enabled
FROM pg_trigger t
JOIN pg_class c ON t.tgrelid = c.oid
WHERE c.relname IN ('customers', 'stores', 'staffs', 'products', 'orders', 'order_items', 'stocks')
AND t.tgname LIKE '%updated_at%'
ORDER BY c.relname;
```

**Resultado esperado:** 7 triggers (customers, stores, staffs, products, orders, order_items, stocks)

## 2. Pruebas de Modificación en Diferentes Tablas

### Prueba 1: Modificar Customers (para dim_customer ETL)

```sql
-- Modificar customers
UPDATE bike_stores.customers
SET phone = '555-TEST-' || customer_id
WHERE customer_id = 2;

-- Verificar que updated_at cambió automáticamente
SELECT customer_id, first_name, phone, updated_at
FROM bike_stores.customers
WHERE customer_id = 2;
```

### Prueba 2: Modificar Stores (para dim_store ETL)

```sql
-- Modificar stores
UPDATE bike_stores.stores
SET store_name = store_name || ' [TEST]'
WHERE store_id = 1;

-- Verificar que updated_at cambió automáticamente
SELECT store_id, store_name, updated_at
FROM bike_stores.stores
WHERE store_id = 1;
```

### Prueba 3: Modificar Stocks (para fact_store_stock ETL)

```sql
-- Modificar stocks
UPDATE bike_stores.stocks
SET quantity = quantity + 15
WHERE store_id = 1 AND product_id = 2;

-- Verificar que updated_at cambió automáticamente
SELECT store_id, product_id, quantity, updated_at
FROM bike_stores.stocks
WHERE store_id = 1 AND product_id = 2;
```

### Prueba 4: Modificar Products (para dim_product ETL)

```sql
-- Modificar products
UPDATE bike_stores.products
SET list_price = list_price * 1.1
WHERE product_id = 3;

-- Verificar que updated_at cambió automáticamente
SELECT product_id, product_name, list_price, updated_at
FROM bike_stores.products
WHERE product_id = 3;
```

## 3. Verificar Watermarks del ETL

```sql
-- Verificar los watermarks actuales
SELECT job, load_watermark
FROM etl.etl_watermarks
ORDER BY job;
```

## 3.1. Test de Watermark - Resetear para Forzar Procesamiento

```sql
-- IMPORTANTE: Si los watermarks están muy actualizados, resetear para forzar procesamiento
-- Esto es útil cuando quieres probar que el ETL detecta cambios

-- Resetear watermark de fact_store_stock para forzar procesamiento
UPDATE etl.etl_watermarks
SET load_watermark = '1970-01-01'::timestamptz
WHERE job = 'fact_store_stock';

-- Resetear watermark de dim_customer para forzar procesamiento
UPDATE etl.etl_watermarks
SET load_watermark = '1970-01-01'::timestamptz
WHERE job = 'dim_customer';

-- Verificar que se reseteo correctamente
SELECT job, load_watermark
FROM etl.etl_watermarks
WHERE job IN ('fact_store_stock', 'dim_customer');
```

## 3.2. Test de Watermark - Ejecutar ETL Después del Reset

```sql
-- Después de resetear watermarks, ejecutar ETL manualmente para ver rows_processed > 0
SELECT dwh.etl_fact_store_stock();
SELECT dwh.etl_dim_customer();

-- Verificar logs para confirmar que procesó registros
SELECT job, finished_at, status, rows_processed, message
FROM etl.etl_runs
ORDER BY finished_at DESC
LIMIT 3;
```

**Resultado esperado después del reset:** `rows_processed > 0` en los logs

## 4. Prueba Final - Modificación Después del Watermark

```sql
-- Hacer un cambio DESPUÉS del watermark para probar detección
UPDATE bike_stores.customers
SET email = 'nuevo.email@test.com'
WHERE customer_id = 3;

-- Verificar timestamp
SELECT customer_id, first_name, email, updated_at
FROM bike_stores.customers
WHERE customer_id = 3;
```

## 5. Verificar Logs del ETL

```sql
-- Verificar logs del ETL (ejecutar después de esperar 30-40 segundos)
SELECT job, finished_at, status, rows_processed, message
FROM etl.etl_runs
ORDER BY finished_at DESC
LIMIT 8;
```

**Resultado esperado:** Al menos un job con `rows_processed > 0` (especialmente `dim_customer`)

## 6. Verificación de Datos en el DWH

```sql
-- Verificar que los datos se actualizaron en el DWH
SELECT
    'SOURCE' as tabla,
    store_id,
    product_id,
    quantity,
    updated_at
FROM bike_stores.stocks
WHERE store_id = 1 AND product_id = 2
UNION ALL
SELECT
    'DWH' as tabla,
    store_id,
    product_id,
    quantity,
    NULL as updated_at
FROM dwh.fact_store_stock
WHERE store_id = 1 AND product_id = 2;
```

## 7. Ejecutar ETL Manualmente (Opcional)

```sql
-- Ejecutar ETL completo manualmente
SELECT dwh.run_full_etl();

-- O ejecutar solo un fact específico
SELECT dwh.etl_fact_store_stock();
SELECT dwh.etl_dim_customer();
```

## 8. Verificar Logs Después de ETL Manual

```sql
-- Verificar logs después de ejecución manual
SELECT job, finished_at, status, rows_processed, message
FROM etl.etl_runs
ORDER BY finished_at DESC
LIMIT 3;
```

## Comandos de Terminal

```bash
# Esperar para que el ETL automático procese cambios
sleep 40

# Verificar logs del ETL
export PGPASSWORD=postgres && psql -h localhost -p 5435 -U postgres -d bike_stores -c "
SELECT job, finished_at, status, rows_processed, message
FROM etl.etl_runs
ORDER BY finished_at DESC
LIMIT 5;"
```

## Resultados Esperados

### ✅ Éxito - El ETL funciona correctamente:

- Todos los triggers están creados (7 triggers)
- Los cambios se detectan automáticamente
- `rows_processed > 0` en los logs del ETL
- Los datos coinciden entre tablas fuente y DWH

### ❌ Problema - El ETL no funciona:

- Triggers faltantes
- `rows_processed = 0` en todos los logs
- Los datos no se actualizan en el DWH

## Notas Importantes

1. **Watermarks**: El ETL solo procesa cambios después del último watermark
2. **Timing**: El ETL automático se ejecuta cada 30 segundos
3. **Triggers**: Deben actualizar automáticamente `updated_at` en cada modificación
4. **Verificación**: Siempre comparar timestamps entre fuente y DWH

## Troubleshooting

Si `rows_processed = 0`:

1. Verificar que los triggers están funcionando
2. Verificar watermarks vs timestamps de modificación
3. Resetear watermarks si es necesario:

```sql
UPDATE etl.etl_watermarks
SET load_watermark = '1970-01-01'::timestamptz
WHERE job = 'nombre_del_job';
```

### Test Completo de Watermark (Método que usamos)

```sql
-- 1. Verificar watermark actual
SELECT job, load_watermark
FROM etl.etl_watermarks
WHERE job = 'fact_store_stock';

-- 2. Hacer modificación con timestamp explícito
UPDATE bike_stores.stocks
SET quantity = quantity + 10,
    updated_at = now()
WHERE store_id = 1 AND product_id = 1;

-- 3. Verificar que updated_at cambió
SELECT store_id, product_id, quantity, updated_at
FROM bike_stores.stocks
WHERE store_id = 1 AND product_id = 1;

-- 4. Ejecutar ETL manualmente
SELECT dwh.etl_fact_store_stock();

-- 5. Verificar que procesó el cambio
SELECT job, finished_at, status, rows_processed, message
FROM etl.etl_runs
ORDER BY finished_at DESC
LIMIT 3;

-- 6. Comparar datos fuente vs DWH
SELECT
    'SOURCE' as tabla, store_id, product_id, quantity, updated_at
FROM bike_stores.stocks
WHERE store_id = 1 AND product_id = 1
UNION ALL
SELECT
    'DWH' as tabla, store_id, product_id, quantity, NULL as updated_at
FROM dwh.fact_store_stock
WHERE store_id = 1 AND product_id = 1;
```

**Resultado esperado:** `rows_processed = 1` y datos coincidentes entre fuente y DWH
