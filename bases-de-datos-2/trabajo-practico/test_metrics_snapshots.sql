-- test_metrics_snapshots.sql
-- Script para probar los snapshots de métricas
-- =========================================================

\echo '===================================================================='
\echo 'TEST: Snapshots de Métricas'
\echo '===================================================================='
\echo ''

-- 1. Ver cantidad de snapshots
\echo '1. Cantidad de snapshots por cubo:'
\echo '--------------------------------------------------------------------'
SELECT 
    cube_name,
    COUNT(*) as total_combinaciones,
    COUNT(DISTINCT snapshot_date) as num_snapshots,
    MIN(snapshot_date) as primer_snapshot,
    MAX(snapshot_date) as ultimo_snapshot
FROM dwh_metrics.metrics_snapshots
GROUP BY cube_name
ORDER BY cube_name;

\echo ''
\echo '2. Métricas ACTUALES globales (desde v_current_metrics):'
\echo '--------------------------------------------------------------------'
SELECT 
    cube_name,
    rows,
    quantity,
    ROUND(final_price, 2) as final_price,
    ROUND(COALESCE(avg_ticket, profit_margin_pct, stock_capacity_pct), 2) as metric_especial,
    TO_CHAR(snapshot_date, 'YYYY-MM-DD HH24:MI:SS') as fecha
FROM dwh_metrics.v_current_metrics
WHERE gid IN (31, 7)  -- Nivel más agregado para cada cubo
ORDER BY cube_name;

\echo ''
\echo '3. Métricas ACTUALES por tienda (Orders):'
\echo '--------------------------------------------------------------------'
SELECT 
    dst.store_name,
    cm.rows,
    ROUND(cm.quantity::numeric, 0) as quantity,
    ROUND(cm.final_price, 2) as final_price,
    ROUND(cm.avg_ticket, 2) as avg_ticket,
    ROUND(cm.discount_margin_pct, 4) as discount_margin_pct,
    TO_CHAR(cm.snapshot_date, 'YYYY-MM-DD HH24:MI:SS') as fecha
FROM dwh_metrics.v_current_metrics cm
JOIN dwh.dim_store dst ON cm.store_sk = dst.store_sk AND dst.is_current = true
WHERE cm.cube_name = 'cube_bike_order'
  AND cm.store_sk IS NOT NULL
  AND cm.order_date_id IS NULL
  AND cm.product_sk IS NULL
  AND cm.customer_sk IS NULL
  AND cm.staff_sk IS NULL
ORDER BY cm.final_price DESC;

\echo ''
\echo '4. Métricas ACTUALES por tienda (Stock):'
\echo '--------------------------------------------------------------------'
SELECT 
    dst.store_name,
    cm.total_stock,
    ROUND(cm.stock_capacity_pct, 2) as stock_capacity_pct,
    ROUND(cm.stock_value, 2) as stock_value,
    TO_CHAR(cm.snapshot_date, 'YYYY-MM-DD HH24:MI:SS') as fecha
FROM dwh_metrics.v_current_metrics cm
JOIN dwh.dim_store dst ON cm.store_sk = dst.store_sk AND dst.is_current = true
WHERE cm.cube_name = 'cube_store_stock'
  AND cm.store_sk IS NOT NULL
  AND cm.date_id IS NULL
  AND cm.product_sk IS NULL
ORDER BY cm.store_sk;

\echo ''
\echo '5. Comparación con cubos originales (verificación):'
\echo '--------------------------------------------------------------------'
\echo 'Desde Snapshot (Orders):'
SELECT 
    SUM(rows) as total_rows,
    SUM(quantity) as total_quantity,
    ROUND(SUM(final_price), 2) as total_final_price
FROM dwh_metrics.v_current_metrics
WHERE cube_name = 'cube_bike_order' AND gid = 31;

\echo ''
\echo 'Desde Cubo Original (Orders):'
SELECT 
    rows as total_rows,
    quantity as total_quantity,
    ROUND(final_price, 2) as total_final_price
FROM dwh_cube.cube_bike_order
WHERE gid = 31;

\echo ''
\echo '6. Evolución de métricas (si hay múltiples snapshots):'
\echo '--------------------------------------------------------------------'
SELECT 
    cube_name,
    store_sk,
    current_rows,
    prev_rows,
    rows_delta,
    ROUND(current_final_price, 2) as current_final_price,
    ROUND(COALESCE(prev_final_price, 0), 2) as prev_final_price,
    ROUND(final_price_delta, 2) as final_price_delta,
    TO_CHAR(snapshot_date, 'YYYY-MM-DD HH24:MI:SS') as fecha
FROM dwh_metrics.v_metrics_evolution
WHERE store_sk IS NOT NULL 
  AND product_sk IS NULL
  AND (rows_delta != 0 OR rows_delta IS NULL)
ORDER BY cube_name, store_sk
LIMIT 10;

\echo ''
\echo '7. Historial de snapshots ETL:'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(finished_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    status,
    rows_processed,
    message
FROM etl.etl_runs
WHERE job = 'snapshot_metrics'
ORDER BY finished_at DESC
LIMIT 5;

\echo ''
\echo '===================================================================='
\echo 'TEST COMPLETADO'
\echo 'Los snapshots permiten ver la evolución de métricas en el tiempo'
\echo '===================================================================='


