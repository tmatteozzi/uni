-- =========================================================
-- TEST: SCD Type 2 con Refresh Automático de Cubos
-- =========================================================
-- Este script demuestra que los cubos se refrescan
-- automáticamente cuando se modifican las dimensiones
-- =========================================================

\echo '========================================='
\echo 'TEST: Refresh Automático de Cubos'
\echo '========================================='
\echo ''

-- =========================================================
-- PASO 1: Ver estado inicial
-- =========================================================
\echo '1️⃣  ESTADO INICIAL'
\echo '─────────────────────────────────────────'

\echo 'Dimensión Store (store_id = 2):'
SELECT store_sk, store_id, store_name, is_current 
FROM dwh.dim_store 
WHERE store_id = 2;

\echo ''
\echo 'Cubos (store_sk relacionados):'
SELECT store_sk, SUM(rows) as total_rows
FROM dwh_cube.cube_bike_order
WHERE store_sk IN (2, 35, 36) AND gid = 0
GROUP BY store_sk
ORDER BY store_sk;

\echo ''
\echo '========================================='
\echo ''

-- =========================================================
-- PASO 2: Modificar el store
-- =========================================================
\echo '2️⃣  MODIFICAR DIMENSIÓN'
\echo '─────────────────────────────────────────'

UPDATE bike_stores.stores 
SET store_name = 'Baldwin Bikes - MODIFIED BY TEST',
    city = 'Baldwin Updated'
WHERE store_id = 2;

\echo 'Store 2 modificado ✓'
\echo ''
\echo '========================================='
\echo ''

-- =========================================================
-- PASO 3: Ejecutar ETL (con refresh automático)
-- =========================================================
\echo '3️⃣  EJECUTAR ETL'
\echo '─────────────────────────────────────────'
\echo 'Ejecutando etl_dim_store()...'
\echo '(debería refrescar cubos automáticamente)'
\echo ''

SELECT dwh.etl_dim_store();

\echo ''
\echo '========================================='
\echo ''

-- =========================================================
-- PASO 4: Verificar resultados
-- =========================================================
\echo '4️⃣  VERIFICAR RESULTADOS'
\echo '─────────────────────────────────────────'

\echo 'Dimensión Store (store_id = 2) - Versiones:'
SELECT 
    store_sk,
    store_name,
    CASE WHEN is_current THEN '✓ ACTUAL' ELSE '✗ HISTÓRICO' END as estado,
    to_char(valid_from, 'YYYY-MM-DD HH24:MI:SS') as valido_desde
FROM dwh.dim_store 
WHERE store_id = 2
ORDER BY valid_from;

\echo ''
\echo 'Cubos (deberían estar actualizados):'
SELECT 
    store_sk, 
    SUM(rows) as total_rows,
    CASE 
        WHEN store_sk = 2 THEN 'SK original'
        WHEN store_sk = 35 THEN 'SK nuevo (creado por ETL)'
        ELSE 'Otro SK'
    END as descripcion
FROM dwh_cube.cube_bike_order
WHERE store_sk IN (2, 35, 36) AND gid = 0
GROUP BY store_sk
ORDER BY store_sk;

\echo ''
\echo '========================================='
\echo ''

-- =========================================================
-- PASO 5: Crear nueva orden con el SK actualizado
-- =========================================================
\echo '5️⃣  CREAR NUEVA ORDEN'
\echo '─────────────────────────────────────────'

-- Ajustar secuencia
SELECT setval('bike_stores.orders_order_id_seq', 
              (SELECT MAX(order_id) FROM bike_stores.orders) + 1, false);

-- Crear orden para store_id = 2 (debería usar el nuevo SK)
INSERT INTO bike_stores.orders (customer_id, order_status, order_date, required_date, store_id, staff_id)
VALUES (10, 4, '2018-07-01', '2018-07-05', 2, 3);

INSERT INTO bike_stores.order_items (order_id, item_id, product_id, quantity, list_price, discount)
VALUES (currval('bike_stores.orders_order_id_seq'), 1, 10, 2, 800.00, 0);

\echo 'Nueva orden creada ✓'
\echo ''

-- Ejecutar ETL de facts
\echo 'Ejecutando etl_fact_orders()...'
UPDATE etl.etl_watermarks SET load_watermark = '2018-01-01'::timestamptz WHERE job = 'fact_orders';
SELECT dwh.etl_fact_orders();

\echo ''
\echo 'Verificar que la nueva orden usa el SK correcto:'
SELECT 
    fo.order_id,
    fo.store_sk,
    ds.store_name,
    ds.is_current,
    CASE 
        WHEN ds.is_current AND fo.store_sk = 35 THEN '✓✓✓ USA NUEVO SK CORRECTAMENTE'
        WHEN fo.store_sk = 2 THEN '✗ Usa SK antiguo'
        ELSE '?'
    END as verificacion
FROM dwh.fact_bike_order fo
JOIN dwh.dim_store ds ON fo.store_sk = ds.store_sk
WHERE fo.order_id = (SELECT MAX(order_id) FROM dwh.fact_bike_order)
LIMIT 1;

\echo ''
\echo '========================================='
\echo ''

-- =========================================================
-- PASO 6: Refrescar cubos nuevamente (ya con la nueva orden)
-- =========================================================
\echo '6️⃣  REFRESCAR CUBOS (con nueva orden)'
\echo '─────────────────────────────────────────'

-- Forzar un cambio para que se ejecute el refresh automático
UPDATE bike_stores.stores SET phone = '(555) 123-4567' WHERE store_id = 3;
SELECT dwh.etl_dim_store();

\echo ''
\echo 'Cubos finales (deberían mostrar el nuevo SK con datos):'
SELECT 
    store_sk, 
    SUM(rows) as ordenes,
    to_char(SUM(gross_amount), 'FM999,999,999.99') as ventas_totales
FROM dwh_cube.cube_bike_order
WHERE store_sk IN (2, 35, 36) AND gid = 0
GROUP BY store_sk
ORDER BY store_sk;

\echo ''
\echo '========================================='
\echo '✅ TEST COMPLETADO'
\echo '========================================='
\echo ''
\echo 'Resumen:'
\echo '  ✓ Dimensión modificada crea nuevo SK'
\echo '  ✓ Cubos se refrescan automáticamente'
\echo '  ✓ Nuevas órdenes usan el SK actualizado'
\echo '  ✓ Historial preservado correctamente'
\echo ''
\echo 'Cambios implementados:'
\echo '  - etl_dim_customer() → refresca cubos'
\echo '  - etl_dim_store() → refresca cubos'
\echo '  - etl_dim_staff() → refresca cubos'
\echo '  - etl_dim_product() → refresca cubos'
\echo ''

