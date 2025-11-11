# 📊 Histórico de Métricas - Arquitectura

## 🎯 Respuesta Rápida

**¿Dónde se guarda el histórico de las métricas?**

Las métricas NO se guardan en tablas históricas separadas. Se **calculan dinámicamente** desde los cubos OLAP, que a su vez se calculan desde las tablas de hechos. El histórico está garantizado por:

1. **Tablas de Hechos**: Contienen registros con `date_id` (nunca se eliminan)
2. **Dimensiones SCD Type 2**: Mantienen versiones históricas de los atributos
3. **Cubos Materializados**: Pre-calculan todas las agregaciones incluyendo histórico por fecha

---

## 🏗️ Arquitectura de 3 Capas

### **CAPA 1: Tablas de Hechos (Granularidad Máxima)**

```
dwh.fact_bike_order
dwh.fact_bike_shipment  
dwh.fact_store_stock
```

**Características:**
- Contienen **cada transacción** individual
- Tienen `date_id` (fecha del evento)
- **Nunca se eliminan registros** (solo inserts/updates)
- Son la **fuente de verdad** para todo el histórico

**Ejemplo:**

```sql
SELECT 
    order_date_id,
    order_id,
    quantity,
    discounted_order_amount
FROM dwh.fact_bike_order
WHERE order_date_id BETWEEN 20160101 AND 20160131
ORDER BY order_date_id
LIMIT 5;
```

```
 order_date_id | order_id | quantity | discounted_order_amount 
---------------+----------+----------+-------------------------
      20160101 |        1 |        2 |                 3599.84
      20160101 |        1 |        1 |                 2899.79
      20160101 |        1 |        1 |                 1199.88
      20160101 |        1 |        2 |                 3097.90
      20160101 |        1 |        1 |                  599.79
```

### **CAPA 2: Dimensiones SCD Type 2 (Histórico de Atributos)**

```
dwh.dim_product
dwh.dim_customer
dwh.dim_store
dwh.dim_staff
```

**Características:**
- Mantienen **versiones** de cada registro
- Campos: `valid_from`, `valid_to`, `is_current`
- Permiten saber cómo eran los atributos en cualquier fecha

**Ejemplo - Si un producto cambia de precio:**

```sql
SELECT 
    product_sk,
    product_id,
    product_name,
    list_price,
    valid_from,
    valid_to,
    is_current
FROM dwh.dim_product
WHERE product_id = 42
ORDER BY valid_from DESC;
```

```
 product_sk | product_id | product_name | list_price |     valid_from      |      valid_to       | is_current
------------+------------+--------------+------------+---------------------+---------------------+------------
        142 |         42 | Trek X100    |     899.99 | 2024-01-01 00:00:00 |                     | t
        115 |         42 | Trek X100    |     799.99 | 2023-01-01 00:00:00 | 2024-01-01 00:00:00 | f
         42 |         42 | Trek X100    |     699.99 | 2022-01-01 00:00:00 | 2023-01-01 00:00:00 | f
```

### **CAPA 3: Cubos OLAP (Métricas Pre-calculadas)**

```
dwh_cube.cube_bike_order
dwh_cube.cube_bike_shipment
dwh_cube.cube_store_stock
```

**Características:**
- Son **materialized views** (vistas materializadas)
- Pre-calculan **todas las agregaciones** posibles usando `GROUP BY CUBE`
- Incluyen **histórico** por fecha
- Se **refrescan** con cada ejecución del ETL

**Ejemplo - Métricas históricas por fecha:**

```sql
SELECT 
    order_date_id,
    dd.date,
    rows as ordenes,
    ROUND(avg_ticket, 2) as ticket_promedio,
    ROUND(discount_margin_pct, 2) as margen_descuento
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
ORDER BY order_date_id DESC
LIMIT 5;
```

```
 order_date_id |    date    | ordenes | ticket_promedio | margen_descuento
---------------+------------+---------+-----------------+------------------
      20181228 | 2018-12-28 |       5 |         9079.99 |             0.00
      20181226 | 2018-12-26 |       4 |         6799.74 |             0.01
      20181224 | 2018-12-24 |       5 |         8579.79 |             0.00
      20181221 | 2018-12-21 |       3 |        10479.89 |             0.00
      20181220 | 2018-12-20 |       5 |        10929.97 |             0.00
```

---

## 🔄 Flujo de Datos y Actualización

```
┌─────────────────────────┐
│  Bike Stores (OLTP)     │ ← Sistema transaccional
│  bike_stores.orders     │
│  bike_stores.stocks     │
└──────────┬──────────────┘
           │
           ▼
    ┌──────────────┐
    │     ETL      │ ← Proceso incremental
    └──────┬───────┘
           │
           ▼
┌─────────────────────────┐
│  Dimensiones (SCD-2)    │ ← Histórico de atributos
│  dwh.dim_product        │
│  dwh.dim_customer       │
│  dwh.dim_store          │
│  dwh.dim_staff          │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│  Tablas de Hechos       │ ← Histórico de transacciones
│  dwh.fact_bike_order    │   (con date_id)
│  dwh.fact_bike_shipment │
│  dwh.fact_store_stock   │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│  Cubos OLAP             │ ← Métricas pre-calculadas
│  cube_bike_order        │   con histórico
│  - avg_ticket           │
│  - discount_margin_pct  │
│                         │
│  cube_bike_shipment     │
│  - profit_margin_pct    │
│  - avg_shipping_days    │
│                         │
│  cube_store_stock       │
│  - stock_capacity_pct   │
│  - stock_value          │
└─────────────────────────┘
           │
           ▼
    ┌──────────────┐
    │  Reportes /  │ ← Herramientas de BI
    │  Dashboards  │   (Tableau, Power BI, etc.)
    └──────────────┘
```

---

## 📈 Consultas de Ejemplo: Histórico de Métricas

### 1. **Evolución del Ticket Promedio por Mes**

```sql
SELECT 
    dd.year,
    dd.month,
    dd.month_name,
    COUNT(DISTINCT co.order_date_id) as dias_con_ventas,
    ROUND(AVG(co.avg_ticket), 2) as ticket_promedio_mes
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dd.year, dd.month, dd.month_name
ORDER BY dd.year, dd.month;
```

### 2. **Profit Margin Histórico por Trimestre**

```sql
SELECT 
    dd.year,
    dd.quarter,
    ROUND(AVG(cs.profit_margin_pct), 2) as profit_margin_trimestre,
    ROUND(AVG(cs.avg_shipping_days), 1) as dias_envio_trimestre
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id
WHERE cs.shipment_date_id IS NOT NULL
  AND cs.store_sk IS NULL
  AND cs.product_sk IS NULL
  AND cs.customer_sk IS NULL
  AND cs.staff_sk IS NULL
GROUP BY dd.year, dd.quarter
ORDER BY dd.year, dd.quarter;
```

### 3. **Evolución del Valor del Inventario por Tienda**

```sql
SELECT 
    dst.store_name,
    css.date_id,
    dd.date,
    css.total_stock,
    ROUND(css.stock_value, 2) as valor_inventario
FROM dwh_cube.cube_store_stock css
JOIN dwh.dim_store dst ON css.store_sk = dst.store_sk
JOIN dwh.dim_date dd ON css.date_id = dd.date_id
WHERE css.store_sk IS NOT NULL
  AND css.date_id IS NOT NULL
  AND css.product_sk IS NULL
ORDER BY css.date_id DESC, dst.store_name;
```

---

## 🔍 ¿Cómo se Mantiene el Histórico?

### **En el ETL:**

```sql
-- etl/etl_fact_orders.sql
-- Solo inserta NUEVOS registros, nunca elimina históricos
INSERT INTO dwh.fact_bike_order (...)
SELECT ...
FROM bike_stores.orders o
WHERE o.order_date > coalesce(wm::date, '1970-01-01'::date)  -- Solo nuevos
ON CONFLICT (...) DO NOTHING;  -- No sobrescribe
```

### **En los Cubos:**

```sql
-- dwh/create_cubes.sql
-- El GROUP BY CUBE genera agregaciones para TODAS las fechas
SELECT
    fo.order_date_id,  -- ← Mantiene la dimensión temporal
    fo.store_sk,
    ...
    ROUND(AVG(co.avg_ticket), 2) as avg_ticket
FROM dwh.fact_bike_order fo
GROUP BY CUBE (
    fo.order_date_id,  -- ← Cada fecha genera sus propias métricas
    fo.store_sk,
    fo.product_sk,
    ...
);
```

---

## 📊 Tamaños Actuales

En tu base de datos actual:

```sql
-- Verificar cantidad de histórico
SELECT 
    'fact_bike_order' as tabla,
    COUNT(*) as registros,
    COUNT(DISTINCT order_date_id) as fechas_distintas,
    MIN(order_date_id) as desde,
    MAX(order_date_id) as hasta
FROM dwh.fact_bike_order;
```

**Resultado actual:**
- **4,722 transacciones** en `fact_bike_order`
- **725 fechas distintas** (desde 2016-01-01 hasta 2018-12-28)
- **48,127 agregaciones** en `cube_bike_order` (todas las combinaciones históricas)

---

## ⚡ Ventajas de este Diseño

✅ **No hay pérdida de histórico**: Todas las transacciones se mantienen  
✅ **Consultas rápidas**: Los cubos pre-calculan las métricas  
✅ **Análisis flexible**: Puedes analizar cualquier período histórico  
✅ **Actualización eficiente**: ETL incremental solo procesa lo nuevo  
✅ **Compatible con BI**: Herramientas de reporting pueden consultar directamente los cubos  

---

## 🔄 Refresh de Métricas

Las métricas se recalculan automáticamente al ejecutar:

```sql
SELECT dwh.run_full_etl();
```

Esto:
1. Carga nuevos datos en hechos
2. Actualiza dimensiones (SCD Type 2)
3. **Refresca los cubos** con `REFRESH MATERIALIZED VIEW`
4. Todas las métricas históricas se recalculan con los datos actualizados

---

## 📌 Resumen

| Pregunta | Respuesta |
|----------|-----------|
| **¿Dónde está el histórico?** | En las tablas de hechos (con `date_id`) |
| **¿Dónde están las métricas?** | Se calculan en los cubos OLAP |
| **¿Se pierde el histórico?** | NO, nunca se eliminan registros de hechos |
| **¿Cómo consulto el histórico?** | Filtras por `order_date_id`, `shipment_date_id`, etc. |
| **¿Se actualiza automático?** | SÍ, con `dwh.run_full_etl()` |
| **¿Cuánto histórico hay?** | Desde 2016-01-01 hasta 2018-12-28 (725 días) |


