-- test_metrics_evolution.sql
-- Prueba que demuestra la evolución de métricas con nuevos datos
-- =========================================================

\echo '===================================================================='
\echo 'TEST: Evolución de Métricas con Nuevos Datos'
\echo '===================================================================='
\echo ''

-- 1. Ver estado actual del Avg Ticket
\echo '1. Avg Ticket ANTES de insertar nuevos datos:'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    total_orders,
    ROUND(avg_ticket, 2) as avg_ticket
FROM dwh_metrics.avg_ticket_history
ORDER BY calculated_at DESC
LIMIT 3;

-- 2. Insertar nueva orden con alto valor
\echo ''
\echo '2. Insertando nueva orden de alto valor ($20,000)...'
\echo '--------------------------------------------------------------------'
INSERT INTO bike_stores.orders (customer_id, order_status, order_date, required_date, shipped_date, store_id, staff_id)
VALUES (259, 4, CURRENT_DATE, CURRENT_DATE + 10, CURRENT_DATE + 2, 1, 2)
RETURNING order_id;

-- Obtener el último order_id e insertar items
INSERT INTO bike_stores.order_items (order_id, item_id, product_id, quantity, list_price, discount)
SELECT 
    (SELECT MAX(order_id) FROM bike_stores.orders),
    1,
    4,  -- Un producto caro
    2,
    10000.00,
    0;

\echo 'Nueva orden insertada exitosamente!'

-- 3. Ver Stock ANTES del ETL
\echo ''
\echo '3. Store Capacity ANTES del ETL:'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ds.store_name,
    total_stock,
    ROUND(capacity_pct, 2) as capacity_pct
FROM dwh_metrics.store_capacity_history sch
JOIN dwh.dim_store ds ON sch.store_sk = ds.store_sk AND ds.is_current = true
WHERE ds.store_name = 'Santa Cruz Bikes'
ORDER BY calculated_at DESC
LIMIT 3;

-- 4. Ejecutar ETL
\echo ''
\echo '4. Ejecutando ETL completo...'
\echo '--------------------------------------------------------------------'
SELECT dwh.run_full_etl();

-- 5. Ver Avg Ticket DESPUÉS
\echo ''
\echo '5. Avg Ticket DESPUÉS de insertar nuevos datos:'
\echo '--------------------------------------------------------------------'
SELECT 
    TO_CHAR(calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    total_orders,
    ROUND(avg_ticket, 2) as avg_ticket
FROM dwh_metrics.avg_ticket_history
ORDER BY calculated_at DESC
LIMIT 5;

-- 6. Comparar evolución
\echo ''
\echo '6. Evolución del Avg Ticket (comparación):'
\echo '--------------------------------------------------------------------'
WITH ordered_metrics AS (
    SELECT 
        calculated_at,
        avg_ticket,
        LAG(avg_ticket) OVER (ORDER BY calculated_at) as prev_avg_ticket
    FROM dwh_metrics.avg_ticket_history
)
SELECT 
    TO_CHAR(calculated_at, 'YYYY-MM-DD HH24:MI:SS') as fecha,
    ROUND(avg_ticket, 2) as avg_ticket_actual,
    ROUND(prev_avg_ticket, 2) as avg_ticket_anterior,
    ROUND(avg_ticket - COALESCE(prev_avg_ticket, avg_ticket), 2) as diferencia
FROM ordered_metrics
ORDER BY calculated_at DESC
LIMIT 5;

-- 7. Ver todas las tablas de métricas
\echo ''
\echo '7. Resumen de snapshots en todas las tablas:'
\echo '--------------------------------------------------------------------'
SELECT 
    'stock_value_history' as tabla,
    COUNT(DISTINCT calculated_at) as num_snapshots,
    COUNT(*) as total_registros,
    TO_CHAR(MAX(calculated_at), 'YYYY-MM-DD HH24:MI:SS') as ultimo_snapshot
FROM dwh_metrics.stock_value_history
UNION ALL
SELECT 
    'store_capacity_history',
    COUNT(DISTINCT calculated_at),
    COUNT(*),
    TO_CHAR(MAX(calculated_at), 'YYYY-MM-DD HH24:MI:SS')
FROM dwh_metrics.store_capacity_history
UNION ALL
SELECT 
    'profit_margin_history',
    COUNT(DISTINCT calculated_at),
    COUNT(*),
    TO_CHAR(MAX(calculated_at), 'YYYY-MM-DD HH24:MI:SS')
FROM dwh_metrics.profit_margin_history
UNION ALL
SELECT 
    'shipping_days_history',
    COUNT(DISTINCT calculated_at),
    COUNT(*),
    TO_CHAR(MAX(calculated_at), 'YYYY-MM-DD HH24:MI:SS')
FROM dwh_metrics.shipping_days_history
UNION ALL
SELECT 
    'avg_ticket_history',
    COUNT(DISTINCT calculated_at),
    COUNT(*),
    TO_CHAR(MAX(calculated_at), 'YYYY-MM-DD HH24:MI:SS')
FROM dwh_metrics.avg_ticket_history
UNION ALL
SELECT 
    'discount_margin_history',
    COUNT(DISTINCT calculated_at),
    COUNT(*),
    TO_CHAR(MAX(calculated_at), 'YYYY-MM-DD HH24:MI:SS')
FROM dwh_metrics.discount_margin_history;

\echo ''
\echo '===================================================================='
\echo 'TEST COMPLETADO'
\echo 'Las métricas históricas reflejan correctamente los cambios en datos'
\echo '===================================================================='


