-- test_incremental_metrics.sql
-- Script para probar el comportamiento incremental de métricas
-- =========================================================

\echo '===================================================================='
\echo 'TEST: Comportamiento Incremental de Métricas'
\echo '===================================================================='
\echo ''

-- 1. Ver estado actual
\echo '1. Estado ANTES de simular nuevos datos:'
\echo '--------------------------------------------------------------------'
SELECT 
    cube_name,
    COUNT(*) as combinaciones,
    SUM(calculation_count) as total_calculos,
    MAX(calculation_count) as max_calculos
FROM dwh_metrics.aggregated_metrics
GROUP BY cube_name
ORDER BY cube_name;

\echo ''
\echo '2. Métricas globales ANTES (Orders):'
\echo '--------------------------------------------------------------------'
SELECT 
    rows,
    quantity,
    ROUND(final_price, 2) as final_price,
    ROUND(avg_ticket, 2) as avg_ticket,
    calculation_count
FROM dwh_metrics.aggregated_metrics
WHERE cube_name = 'cube_bike_order' AND gid = 31;

\echo ''
\echo '3. Simulando inserción de nuevos datos...'
\echo '--------------------------------------------------------------------'
-- Simular una nueva orden
INSERT INTO bike_stores.orders (customer_id, order_status, order_date, required_date, shipped_date, store_id, staff_id)
VALUES (1, 4, '2024-01-01', '2024-01-10', '2024-01-03', 1, 2);

-- Obtener el order_id recién insertado
DO $$
DECLARE
    v_order_id int;
BEGIN
    SELECT order_id INTO v_order_id 
    FROM bike_stores.orders 
    WHERE order_date = '2024-01-01' 
    ORDER BY order_id DESC 
    LIMIT 1;
    
    -- Insertar items para esa orden
    INSERT INTO bike_stores.order_items (order_id, item_id, product_id, quantity, list_price, discount)
    VALUES 
        (v_order_id, 1, 1, 2, 1000, 10),
        (v_order_id, 2, 2, 1, 500, 5);
    
    RAISE NOTICE 'Nueva orden insertada con ID: %', v_order_id;
END $$;

\echo ''
\echo '4. Ejecutando ETL incremental...'
\echo '--------------------------------------------------------------------'
SELECT dwh.run_full_etl();

\echo ''
\echo '5. Estado DESPUÉS de ETL incremental:'
\echo '--------------------------------------------------------------------'
SELECT 
    cube_name,
    COUNT(*) as combinaciones,
    SUM(calculation_count) as total_calculos,
    MAX(calculation_count) as max_calculos
FROM dwh_metrics.aggregated_metrics
GROUP BY cube_name
ORDER BY cube_name;

\echo ''
\echo '6. Métricas globales DESPUÉS (Orders):'
\echo '--------------------------------------------------------------------'
SELECT 
    rows,
    quantity,
    ROUND(final_price, 2) as final_price,
    ROUND(avg_ticket, 2) as avg_ticket,
    calculation_count
FROM dwh_metrics.aggregated_metrics
WHERE cube_name = 'cube_bike_order' AND gid = 31;

\echo ''
\echo '7. Historial de ejecuciones ETL de métricas:'
\echo '--------------------------------------------------------------------'
SELECT 
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


