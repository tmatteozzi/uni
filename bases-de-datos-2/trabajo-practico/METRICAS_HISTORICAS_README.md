# Métricas Históricas - Sistema de Acumulación

## 📊 Descripción

Sistema de métricas históricas que guarda cada cálculo en tablas separadas con timestamp. Cada ejecución del ETL agrega nuevos registros, permitiendo analizar la evolución de las métricas en el tiempo.

## 🗂️ Tablas de Métricas Históricas

### 1. **Stock Value History** (`dwh_metrics.stock_value_history`)
Valor del inventario por tienda y producto.

**Campos:**
- `calculated_at`: Timestamp del cálculo
- `store_sk`: Tienda
- `product_sk`: Producto
- `stock_quantity`: Cantidad en stock
- `stock_value`: Valor monetario del stock

**Ejemplo de consulta:**
```sql
SELECT 
    TO_CHAR(svh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ds.store_name,
    dp.product_name,
    svh.stock_quantity,
    ROUND(svh.stock_value, 2) as stock_value
FROM dwh_metrics.stock_value_history svh
JOIN dwh.dim_store ds ON svh.store_sk = ds.store_sk AND ds.is_current = true
JOIN dwh.dim_product dp ON svh.product_sk = dp.product_sk AND dp.is_current = true
WHERE svh.calculated_at > NOW() - INTERVAL '7 days'
ORDER BY svh.calculated_at DESC, svh.stock_value DESC;
```

---

### 2. **Store Capacity History** (`dwh_metrics.store_capacity_history`)
Porcentaje de capacidad utilizada por tienda a lo largo del tiempo.

**Campos:**
- `calculated_at`: Timestamp del cálculo
- `store_sk`: Tienda
- `total_stock`: Stock total actual
- `max_capacity`: Capacidad máxima de la tienda
- `capacity_pct`: Porcentaje de capacidad utilizada

**Ejemplo de consulta:**
```sql
SELECT 
    TO_CHAR(calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ds.store_name,
    total_stock,
    max_capacity,
    ROUND(capacity_pct, 2) as capacity_pct
FROM dwh_metrics.store_capacity_history sch
JOIN dwh.dim_store ds ON sch.store_sk = ds.store_sk AND ds.is_current = true
ORDER BY calculated_at DESC;
```

---

### 3. **Profit Margin History** (`dwh_metrics.profit_margin_history`)
Margen de ganancia por producto a lo largo del tiempo.

**Campos:**
- `calculated_at`: Timestamp del cálculo
- `product_sk`: Producto
- `total_revenue`: Ingresos totales
- `total_cost`: Costo estimado total
- `profit_margin_pct`: Porcentaje de margen de ganancia

**Ejemplo de consulta:**
```sql
SELECT 
    TO_CHAR(pmh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    dp.product_name,
    ROUND(pmh.total_revenue, 2) as revenue,
    ROUND(pmh.profit_margin_pct, 2) as profit_margin_pct
FROM dwh_metrics.profit_margin_history pmh
JOIN dwh.dim_product dp ON pmh.product_sk = dp.product_sk AND dp.is_current = true
WHERE pmh.calculated_at > NOW() - INTERVAL '7 days'
ORDER BY pmh.calculated_at DESC, pmh.total_revenue DESC;
```

---

### 4. **Shipping Days History** (`dwh_metrics.shipping_days_history`)
Días promedio de envío por tienda a lo largo del tiempo.

**Campos:**
- `calculated_at`: Timestamp del cálculo
- `store_sk`: Tienda
- `total_shipments`: Número total de envíos
- `avg_shipping_days`: Días promedio de envío

**Ejemplo de consulta:**
```sql
SELECT 
    TO_CHAR(sdh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ds.store_name,
    sdh.total_shipments,
    ROUND(sdh.avg_shipping_days, 2) as avg_shipping_days
FROM dwh_metrics.shipping_days_history sdh
JOIN dwh.dim_store ds ON sdh.store_sk = ds.store_sk AND ds.is_current = true
ORDER BY sdh.calculated_at DESC;
```

---

### 5. **Avg Ticket History** (`dwh_metrics.avg_ticket_history`)
Ticket promedio global a lo largo del tiempo.

**Campos:**
- `calculated_at`: Timestamp del cálculo
- `total_orders`: Número total de órdenes
- `total_revenue`: Ingresos totales
- `avg_ticket`: Ticket promedio

**Ejemplo de consulta:**
```sql
SELECT 
    TO_CHAR(calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    total_orders,
    ROUND(total_revenue, 2) as total_revenue,
    ROUND(avg_ticket, 2) as avg_ticket
FROM dwh_metrics.avg_ticket_history
ORDER BY calculated_at DESC;
```

---

### 6. **Discount Margin History** (`dwh_metrics.discount_margin_history`)
Margen de descuento por producto y tienda a lo largo del tiempo.

**Campos:**
- `calculated_at`: Timestamp del cálculo
- `product_sk`: Producto
- `store_sk`: Tienda
- `gross_amount`: Monto bruto (sin descuento)
- `discounted_amount`: Monto con descuento
- `discount_margin_pct`: Porcentaje de descuento aplicado

**Ejemplo de consulta:**
```sql
SELECT 
    TO_CHAR(dmh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    dp.product_name,
    ds.store_name,
    ROUND(dmh.gross_amount, 2) as gross_amount,
    ROUND(dmh.discounted_amount, 2) as discounted_amount,
    ROUND(dmh.discount_margin_pct, 4) as discount_margin_pct
FROM dwh_metrics.discount_margin_history dmh
JOIN dwh.dim_product dp ON dmh.product_sk = dp.product_sk AND dp.is_current = true
JOIN dwh.dim_store ds ON dmh.store_sk = ds.store_sk AND ds.is_current = true
WHERE dmh.calculated_at > NOW() - INTERVAL '7 days'
ORDER BY dmh.calculated_at DESC, dmh.gross_amount DESC;
```

---

## 🔄 Funcionamiento

### Ejecución Automática
El sistema ejecuta el ETL automáticamente cada 30 segundos (configurable en `run_etl_scheduler.sh`), calculando y guardando las métricas.

### Ejecución Manual
```sql
SELECT dwh.run_full_etl();
```

### Verificar Ejecuciones
```sql
SELECT 
    TO_CHAR(finished_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    status,
    rows_processed,
    message
FROM etl.etl_runs
WHERE job = 'calculate_metrics'
ORDER BY finished_at DESC
LIMIT 10;
```

---

## 📈 Análisis de Evolución

### Ejemplo: Evolución de Ticket Promedio
```sql
SELECT 
    TO_CHAR(calculated_at, 'YYYY-MM-DD') as dia,
    COUNT(*) as num_calculos,
    ROUND(AVG(avg_ticket), 2) as avg_ticket_promedio,
    ROUND(MIN(avg_ticket), 2) as min_ticket,
    ROUND(MAX(avg_ticket), 2) as max_ticket
FROM dwh_metrics.avg_ticket_history
GROUP BY TO_CHAR(calculated_at, 'YYYY-MM-DD')
ORDER BY dia DESC;
```

### Ejemplo: Evolución de Capacidad por Tienda
```sql
SELECT 
    ds.store_name,
    TO_CHAR(sch.calculated_at, 'YYYY-MM-DD HH24:MI') as fecha_hora,
    sch.total_stock,
    ROUND(sch.capacity_pct, 2) as capacity_pct
FROM dwh_metrics.store_capacity_history sch
JOIN dwh.dim_store ds ON sch.store_sk = ds.store_sk AND ds.is_current = true
WHERE sch.calculated_at > NOW() - INTERVAL '1 day'
ORDER BY ds.store_name, sch.calculated_at;
```

### Ejemplo: Top Productos por Profit Margin (último cálculo)
```sql
WITH last_calc AS (
    SELECT MAX(calculated_at) as max_date
    FROM dwh_metrics.profit_margin_history
)
SELECT 
    dp.product_name,
    ROUND(pmh.total_revenue, 2) as revenue,
    ROUND(pmh.profit_margin_pct, 2) as profit_margin_pct
FROM dwh_metrics.profit_margin_history pmh
JOIN dwh.dim_product dp ON pmh.product_sk = dp.product_sk AND dp.is_current = true
CROSS JOIN last_calc
WHERE pmh.calculated_at = last_calc.max_date
ORDER BY pmh.total_revenue DESC
LIMIT 10;
```

---

## 🧪 Testing

Ejecutar el script de prueba completo:
```bash
docker cp test_metrics_history.sql pg_dw:/tmp/test_metrics_history.sql
docker exec pg_dw psql -U postgres -d bike_stores -f /tmp/test_metrics_history.sql
```

---

## 🔑 Ventajas del Sistema

1. **Histórico Completo**: Cada cálculo se guarda con timestamp, permitiendo análisis temporal
2. **Tablas Separadas**: Cada métrica tiene su propia tabla, optimizando consultas
3. **Fácil Análisis**: Queries simples para ver evolución de métricas
4. **Escalable**: El sistema sigue funcionando con grandes volúmenes de datos
5. **Automatizado**: Se ejecuta automáticamente con el ETL

---

## 📊 Resumen de Métricas

| Métrica | Tabla | Dimensiones | Frecuencia |
|---------|-------|-------------|------------|
| Stock Value | `stock_value_history` | store_sk, product_sk | Cada ETL |
| Store Capacity | `store_capacity_history` | store_sk | Cada ETL |
| Profit Margin | `profit_margin_history` | product_sk | Cada ETL |
| Avg Shipping Days | `shipping_days_history` | store_sk | Cada ETL |
| Avg Ticket | `avg_ticket_history` | Global | Cada ETL |
| Discount Margin | `discount_margin_history` | product_sk, store_sk | Cada ETL |

---

## 🚀 Próximos Pasos

Para analizar cambios en los datos:
1. Insertar nuevos pedidos en `bike_stores.orders`
2. Ejecutar `SELECT dwh.run_full_etl();`
3. Comparar los nuevos valores con los anteriores en las tablas de histórico


