# 🚀 Guía Rápida - Sistema de Métricas Históricas

## ✅ Sistema Implementado y Funcionando

El sistema calcula y guarda métricas históricas automáticamente cada vez que se ejecuta el ETL.

## 📊 Métricas Disponibles

| # | Métrica | Tabla | Dimensiones |
|---|---------|-------|-------------|
| 1 | **Stock Value** | `dwh_metrics.stock_value_history` | store_sk, product_sk |
| 2 | **Store Capacity %** | `dwh_metrics.store_capacity_history` | store_sk |
| 3 | **Profit Margin %** | `dwh_metrics.profit_margin_history` | product_sk |
| 4 | **Avg Shipping Days** | `dwh_metrics.shipping_days_history` | store_sk |
| 5 | **Avg Ticket** | `dwh_metrics.avg_ticket_history` | global |
| 6 | **Discount Margin** | `dwh_metrics.discount_margin_history` | product_sk, store_sk |

## 🔄 Cómo Funciona

1. **Automático**: El scheduler ejecuta el ETL cada 30 segundos
2. **Manual**: Puedes ejecutar `SELECT dwh.run_full_etl();`
3. **Histórico**: Cada ejecución AGREGA nuevos registros (no sobrescribe)
4. **Timestamp**: Cada registro tiene `calculated_at` para análisis temporal

## 📝 Consultas Rápidas

### Ver últimos valores de Avg Ticket
```sql
SELECT 
    TO_CHAR(calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    total_orders,
    ROUND(avg_ticket, 2) as avg_ticket
FROM dwh_metrics.avg_ticket_history
ORDER BY calculated_at DESC
LIMIT 10;
```

### Ver capacidad de tiendas en el tiempo
```sql
SELECT 
    TO_CHAR(sch.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ds.store_name,
    sch.total_stock,
    ROUND(sch.capacity_pct, 2) as capacity_pct
FROM dwh_metrics.store_capacity_history sch
JOIN dwh.dim_store ds ON sch.store_sk = ds.store_sk AND ds.is_current = true
ORDER BY sch.calculated_at DESC, ds.store_name;
```

### Ver profit margin de productos
```sql
SELECT 
    TO_CHAR(pmh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    dp.product_name,
    ROUND(pmh.total_revenue, 2) as revenue,
    ROUND(pmh.profit_margin_pct, 2) as profit_margin_pct
FROM dwh_metrics.profit_margin_history pmh
JOIN dwh.dim_product dp ON pmh.product_sk = dp.product_sk AND dp.is_current = true
WHERE pmh.calculated_at = (SELECT MAX(calculated_at) FROM dwh_metrics.profit_margin_history)
ORDER BY pmh.total_revenue DESC
LIMIT 20;
```

### Ver evolución de shipping days por tienda
```sql
SELECT 
    TO_CHAR(sdh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ds.store_name,
    ROUND(sdh.avg_shipping_days, 2) as avg_shipping_days
FROM dwh_metrics.shipping_days_history sdh
JOIN dwh.dim_store ds ON sdh.store_sk = ds.store_sk AND ds.is_current = true
ORDER BY ds.store_name, sdh.calculated_at DESC;
```

### Ver discount margin por producto y tienda
```sql
SELECT 
    TO_CHAR(dmh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    dp.product_name,
    ds.store_name,
    ROUND(dmh.discount_margin_pct, 4) as discount_margin_pct
FROM dwh_metrics.discount_margin_history dmh
JOIN dwh.dim_product dp ON dmh.product_sk = dp.product_sk AND dp.is_current = true
JOIN dwh.dim_store ds ON dmh.store_sk = ds.store_sk AND ds.is_current = true
WHERE dmh.calculated_at = (SELECT MAX(calculated_at) FROM dwh_metrics.discount_margin_history)
ORDER BY dmh.discount_margin_pct DESC
LIMIT 20;
```

### Ver stock value de productos
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
WHERE svh.calculated_at = (SELECT MAX(calculated_at) FROM dwh_metrics.stock_value_history)
ORDER BY svh.stock_value DESC
LIMIT 20;
```

## 🧪 Scripts de Prueba

```bash
# Test completo de todas las métricas
docker cp test_metrics_history.sql pg_dw:/tmp/
docker exec pg_dw psql -U postgres -d bike_stores -f /tmp/test_metrics_history.sql

# Test de evolución con datos nuevos
docker cp test_metrics_evolution.sql pg_dw:/tmp/
docker exec pg_dw psql -U postgres -d bike_stores -f /tmp/test_metrics_evolution.sql
```

## 🔧 Comandos Útiles

### Ejecutar ETL manualmente
```bash
docker exec pg_dw psql -U postgres -d bike_stores -c "SELECT dwh.run_full_etl();"
```

### Ver historial de ejecuciones
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

### Ver resumen de registros por tabla
```sql
SELECT 
    'stock_value_history' as tabla,
    COUNT(*) as registros,
    COUNT(DISTINCT calculated_at) as snapshots
FROM dwh_metrics.stock_value_history
UNION ALL
SELECT 'store_capacity_history', COUNT(*), COUNT(DISTINCT calculated_at)
FROM dwh_metrics.store_capacity_history
UNION ALL
SELECT 'profit_margin_history', COUNT(*), COUNT(DISTINCT calculated_at)
FROM dwh_metrics.profit_margin_history
UNION ALL
SELECT 'shipping_days_history', COUNT(*), COUNT(DISTINCT calculated_at)
FROM dwh_metrics.shipping_days_history
UNION ALL
SELECT 'avg_ticket_history', COUNT(*), COUNT(DISTINCT calculated_at)
FROM dwh_metrics.avg_ticket_history
UNION ALL
SELECT 'discount_margin_history', COUNT(*), COUNT(DISTINCT calculated_at)
FROM dwh_metrics.discount_margin_history;
```

## 📈 Análisis de Tendencias

### Comparar valores actuales vs anteriores
```sql
WITH current_metrics AS (
    SELECT 
        store_sk,
        capacity_pct,
        calculated_at
    FROM dwh_metrics.store_capacity_history
    WHERE calculated_at = (SELECT MAX(calculated_at) FROM dwh_metrics.store_capacity_history)
),
previous_metrics AS (
    SELECT 
        store_sk,
        capacity_pct,
        calculated_at
    FROM dwh_metrics.store_capacity_history
    WHERE calculated_at = (
        SELECT MAX(calculated_at) 
        FROM dwh_metrics.store_capacity_history 
        WHERE calculated_at < (SELECT MAX(calculated_at) FROM dwh_metrics.store_capacity_history)
    )
)
SELECT 
    ds.store_name,
    ROUND(c.capacity_pct, 2) as actual,
    ROUND(p.capacity_pct, 2) as anterior,
    ROUND(c.capacity_pct - p.capacity_pct, 2) as cambio
FROM current_metrics c
JOIN previous_metrics p ON c.store_sk = p.store_sk
JOIN dwh.dim_store ds ON c.store_sk = ds.store_sk AND ds.is_current = true
ORDER BY cambio DESC;
```

## ✨ Características

- ✅ **Histórico completo**: Todos los cálculos se guardan
- ✅ **No sobrescribe**: Cada ejecución AGREGA registros
- ✅ **Análisis temporal**: Puedes ver evolución en el tiempo
- ✅ **Automático**: Se ejecuta con el ETL
- ✅ **Optimizado**: Índices en todas las columnas importantes
- ✅ **Simple**: Queries fáciles de entender

## 📚 Documentación Completa

Ver `METRICAS_HISTORICAS_README.md` para documentación detallada.


