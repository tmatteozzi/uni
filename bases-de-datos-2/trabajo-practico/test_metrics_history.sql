-- test_metrics_history.sql
-- Script para probar las tablas de métricas históricas
-- =========================================================

\echo '===================================================================='
\echo 'TEST: Métricas Históricas'
\echo '===================================================================='
\echo ''

-- 1. Stock Value History
\echo '1. Stock Value por Store y Product (últimos registros):'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(svh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ds.store_name,
    dp.product_name,
    svh.stock_quantity,
    ROUND(svh.stock_value, 2) as stock_value
FROM dwh_metrics.stock_value_history svh
JOIN dwh.dim_store ds ON svh.store_sk = ds.store_sk AND ds.is_current = true
JOIN dwh.dim_product dp ON svh.product_sk = dp.product_sk AND dp.is_current = true
ORDER BY svh.calculated_at DESC, svh.stock_value DESC
LIMIT 10;

\echo ''
\echo '2. Store Capacity History por tienda:'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(sch.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ds.store_name,
    sch.total_stock,
    sch.max_capacity,
    ROUND(sch.capacity_pct, 2) as capacity_pct
FROM dwh_metrics.store_capacity_history sch
JOIN dwh.dim_store ds ON sch.store_sk = ds.store_sk AND ds.is_current = true
ORDER BY sch.calculated_at DESC, ds.store_name;

\echo ''
\echo '3. Profit Margin por producto (top 10):'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(pmh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    dp.product_name,
    ROUND(pmh.total_revenue, 2) as revenue,
    ROUND(pmh.profit_margin_pct, 2) as profit_margin_pct
FROM dwh_metrics.profit_margin_history pmh
JOIN dwh.dim_product dp ON pmh.product_sk = dp.product_sk AND dp.is_current = true
ORDER BY pmh.calculated_at DESC, pmh.total_revenue DESC
LIMIT 10;

\echo ''
\echo '4. Avg Shipping Days por tienda:'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(sdh.calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ds.store_name,
    sdh.total_shipments,
    ROUND(sdh.avg_shipping_days, 2) as avg_shipping_days
FROM dwh_metrics.shipping_days_history sdh
JOIN dwh.dim_store ds ON sdh.store_sk = ds.store_sk AND ds.is_current = true
ORDER BY sdh.calculated_at DESC, ds.store_name;

\echo ''
\echo '5. Avg Ticket global a lo largo del tiempo:'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    total_orders,
    ROUND(total_revenue, 2) as total_revenue,
    ROUND(avg_ticket, 2) as avg_ticket
FROM dwh_metrics.avg_ticket_history
ORDER BY calculated_at DESC;

\echo ''
\echo '6. Discount Margin por producto y tienda (top 10):'
\echo '--------------------------------------------------------------------'
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
ORDER BY dmh.calculated_at DESC, dmh.gross_amount DESC
LIMIT 10;

\echo ''
\echo '7. Resumen de registros por tabla:'
\echo '--------------------------------------------------------------------'
SELECT 'stock_value_history' as tabla, COUNT(*) as total_registros,
       COUNT(DISTINCT calculated_at) as snapshots,
       MIN(calculated_at) as primer_snapshot,
       MAX(calculated_at) as ultimo_snapshot
FROM dwh_metrics.stock_value_history
UNION ALL
SELECT 'store_capacity_history', COUNT(*), COUNT(DISTINCT calculated_at),
       MIN(calculated_at), MAX(calculated_at)
FROM dwh_metrics.store_capacity_history
UNION ALL
SELECT 'profit_margin_history', COUNT(*), COUNT(DISTINCT calculated_at),
       MIN(calculated_at), MAX(calculated_at)
FROM dwh_metrics.profit_margin_history
UNION ALL
SELECT 'shipping_days_history', COUNT(*), COUNT(DISTINCT calculated_at),
       MIN(calculated_at), MAX(calculated_at)
FROM dwh_metrics.shipping_days_history
UNION ALL
SELECT 'avg_ticket_history', COUNT(*), COUNT(DISTINCT calculated_at),
       MIN(calculated_at), MAX(calculated_at)
FROM dwh_metrics.avg_ticket_history
UNION ALL
SELECT 'discount_margin_history', COUNT(*), COUNT(DISTINCT calculated_at),
       MIN(calculated_at), MAX(calculated_at)
FROM dwh_metrics.discount_margin_history;

\echo ''
\echo '8. Historial de ejecuciones ETL:'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(finished_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    status,
    rows_processed,
    message
FROM etl.etl_runs
WHERE job = 'calculate_metrics'
ORDER BY finished_at DESC
LIMIT 5;

\echo ''
\echo '===================================================================='
\echo 'TEST COMPLETADO'
\echo 'Las métricas se guardan en tablas históricas separadas'
\echo '===================================================================='


