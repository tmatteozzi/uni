-- test_aggregated_metrics.sql
-- Script para probar las métricas agregadas acumulativas
-- =========================================================

\echo '===================================================================='
\echo 'TEST: Métricas Agregadas Acumulativas'
\echo '===================================================================='
\echo ''

-- 1. Ver estado inicial de métricas agregadas
\echo '1. Estado inicial de métricas agregadas:'
\echo '--------------------------------------------------------------------'
SELECT 
    cube_name,
    COUNT(*) as total_combinations,
    SUM(rows) as total_rows,
    SUM(calculation_count) as total_calculations
FROM dwh_metrics.aggregated_metrics
GROUP BY cube_name
ORDER BY cube_name;

\echo ''
\echo '2. Ejemplo de métricas del cubo Orders (top 5 por tienda):'
\echo '--------------------------------------------------------------------'
SELECT 
    dst.store_name,
    am.rows,
    ROUND(am.quantity::numeric, 0) as quantity,
    ROUND(am.final_price, 2) as final_price,
    ROUND(am.avg_ticket, 2) as avg_ticket,
    ROUND(am.discount_margin_pct, 4) as discount_margin_pct,
    am.calculation_count,
    am.last_updated
FROM dwh_metrics.aggregated_metrics am
JOIN dwh.dim_store dst ON am.store_sk = dst.store_sk AND dst.is_current = true
WHERE am.cube_name = 'cube_bike_order'
  AND am.store_sk IS NOT NULL
  AND am.order_date_id IS NULL
  AND am.product_sk IS NULL
  AND am.customer_sk IS NULL
  AND am.staff_sk IS NULL
ORDER BY am.final_price DESC
LIMIT 5;

\echo ''
\echo '3. Ejemplo de métricas del cubo Shipments (top 5 por tienda):'
\echo '--------------------------------------------------------------------'
SELECT 
    dst.store_name,
    am.rows,
    ROUND(am.quantity::numeric, 0) as quantity,
    ROUND(am.final_price, 2) as final_price,
    ROUND(am.profit_margin_pct, 4) as profit_margin_pct,
    ROUND(am.avg_shipping_days, 2) as avg_shipping_days,
    am.calculation_count,
    am.last_updated
FROM dwh_metrics.aggregated_metrics am
JOIN dwh.dim_store dst ON am.store_sk = dst.store_sk AND dst.is_current = true
WHERE am.cube_name = 'cube_bike_shipment'
  AND am.store_sk IS NOT NULL
  AND am.shipment_date_id IS NULL
  AND am.product_sk IS NULL
  AND am.customer_sk IS NULL
  AND am.staff_sk IS NULL
ORDER BY am.final_price DESC
LIMIT 5;

\echo ''
\echo '4. Ejemplo de métricas del cubo Stock (por tienda):'
\echo '--------------------------------------------------------------------'
SELECT 
    dst.store_name,
    am.total_stock,
    ROUND(am.stock_capacity_pct, 2) as stock_capacity_pct,
    ROUND(am.stock_value, 2) as stock_value,
    am.calculation_count,
    am.last_updated
FROM dwh_metrics.aggregated_metrics am
JOIN dwh.dim_store dst ON am.store_sk = dst.store_sk AND dst.is_current = true
WHERE am.cube_name = 'cube_store_stock'
  AND am.store_sk IS NOT NULL
  AND am.date_id IS NULL
  AND am.product_sk IS NULL
ORDER BY am.store_sk;

\echo ''
\echo '5. Comparación con cubos originales (Orders):'
\echo '--------------------------------------------------------------------'
\echo 'Métricas Agregadas:'
SELECT 
    SUM(rows) as total_rows,
    SUM(quantity) as total_quantity,
    ROUND(SUM(final_price), 2) as total_final_price
FROM dwh_metrics.aggregated_metrics
WHERE cube_name = 'cube_bike_order'
  AND gid = 31;  -- Nivel más agregado (todos null)

\echo ''
\echo 'Cubo Original:'
SELECT 
    rows as total_rows,
    quantity as total_quantity,
    ROUND(final_price, 2) as total_final_price
FROM dwh_cube.cube_bike_order
WHERE gid = 31;  -- Nivel más agregado (todos null)

\echo ''
\echo '6. Historial de actualizaciones ETL:'
\echo '--------------------------------------------------------------------'
SELECT 
    job,
    finished_at,
    status,
    rows_processed,
    message
FROM etl.etl_runs
WHERE job = 'aggregated_metrics'
ORDER BY finished_at DESC
LIMIT 5;

\echo ''
\echo '===================================================================='
\echo 'TEST COMPLETADO'
\echo '===================================================================='


