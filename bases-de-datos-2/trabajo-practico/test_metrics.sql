-- ========================================
-- Test de Métricas en los Cubos OLAP
-- ========================================

\echo ''
\echo '=== CUBO ORDERS: Ticket Promedio y Margen de Descuento ==='
\echo ''

-- Métricas agregadas por tienda
SELECT 
    dst.store_name,
    co.rows as ordenes,
    co.quantity as productos_vendidos,
    ROUND(co.avg_ticket, 2) as ticket_promedio,
    ROUND(co.discount_margin_pct, 2) as margen_descuento_pct
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_store dst ON co.store_sk = dst.store_sk
WHERE co.store_sk IS NOT NULL 
  AND co.order_date_id IS NULL 
  AND co.product_sk IS NULL 
  AND co.customer_sk IS NULL 
  AND co.staff_sk IS NULL
ORDER BY dst.store_name;

\echo ''
\echo '=== CUBO ORDERS: Top 5 Fechas por Ticket Promedio ==='
\echo ''

SELECT 
    co.order_date_id,
    dd.date,
    co.rows as ordenes,
    ROUND(co.avg_ticket, 2) as ticket_promedio,
    ROUND(co.discount_margin_pct, 2) as margen_descuento_pct
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE co.order_date_id IS NOT NULL 
  AND co.store_sk IS NULL 
  AND co.product_sk IS NULL 
  AND co.customer_sk IS NULL 
  AND co.staff_sk IS NULL
ORDER BY co.avg_ticket DESC
LIMIT 5;

\echo ''
\echo '=== CUBO SHIPMENTS: Profit Margin y Tiempo Promedio de Envío ==='
\echo ''

-- Métricas agregadas por tienda
SELECT 
    dst.store_name,
    cs.rows as envios,
    cs.quantity as productos_enviados,
    ROUND(cs.profit_margin_pct, 2) as profit_margin_pct,
    ROUND(cs.avg_shipping_days, 1) as dias_envio_promedio
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_store dst ON cs.store_sk = dst.store_sk
WHERE cs.store_sk IS NOT NULL 
  AND cs.shipment_date_id IS NULL 
  AND cs.product_sk IS NULL 
  AND cs.customer_sk IS NULL 
  AND cs.staff_sk IS NULL
ORDER BY dst.store_name;

\echo ''
\echo '=== CUBO SHIPMENTS: Últimos 10 Días con Métricas ==='
\echo ''

SELECT 
    cs.shipment_date_id,
    dd.date,
    cs.rows as envios,
    ROUND(cs.profit_margin_pct, 2) as profit_margin_pct,
    ROUND(cs.avg_shipping_days, 1) as dias_envio_promedio
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id
WHERE cs.shipment_date_id IS NOT NULL 
  AND cs.store_sk IS NULL 
  AND cs.product_sk IS NULL 
  AND cs.customer_sk IS NULL 
  AND cs.staff_sk IS NULL
ORDER BY cs.shipment_date_id DESC
LIMIT 10;

\echo ''
\echo '=== CUBO STOCK: % Capacidad Utilizada por Tienda ==='
\echo ''

SELECT 
    dst.store_name,
    sc.max_capacity as capacidad_maxima,
    css.total_stock as stock_actual,
    ROUND(css.stock_capacity_pct, 2) as capacidad_utilizada_pct
FROM dwh_cube.cube_store_stock css
JOIN dwh.dim_store dst ON css.store_sk = dst.store_sk
JOIN dwh.store_capacity sc ON css.store_sk = sc.store_sk
WHERE css.store_sk IS NOT NULL 
  AND css.date_id IS NULL 
  AND css.product_sk IS NULL
ORDER BY dst.store_name;

\echo ''
\echo '=== CUBO STOCK: Top 10 Productos con Mayor Valor en Inventario ==='
\echo ''

SELECT 
    dst.store_name,
    dp.product_name,
    css.total_stock as stock_actual,
    ROUND(dp.list_price, 2) as precio_unitario,
    ROUND(css.stock_value, 2) as valor_inventario
FROM dwh_cube.cube_store_stock css
JOIN dwh.dim_store dst ON css.store_sk = dst.store_sk
JOIN dwh.dim_product dp ON css.product_sk = dp.product_sk
WHERE css.store_sk IS NOT NULL 
  AND css.product_sk IS NOT NULL 
  AND css.date_id IS NOT NULL
ORDER BY css.stock_value DESC
LIMIT 10;

\echo ''
\echo '=== RESUMEN: Métricas Globales ==='
\echo ''

SELECT 
    'Orders' as cubo,
    ROUND(AVG(avg_ticket), 2) as ticket_promedio_global,
    ROUND(AVG(discount_margin_pct), 2) as margen_descuento_pct_global,
    NULL::numeric as profit_margin_pct,
    NULL::numeric as dias_envio_promedio,
    NULL::numeric as capacidad_utilizada_pct
FROM dwh_cube.cube_bike_order
WHERE gid = 31  -- Total global

UNION ALL

SELECT 
    'Shipments' as cubo,
    NULL::numeric,
    NULL::numeric,
    ROUND(AVG(profit_margin_pct), 2) as profit_margin_pct,
    ROUND(AVG(avg_shipping_days), 1) as dias_envio_promedio,
    NULL::numeric
FROM dwh_cube.cube_bike_shipment
WHERE gid = 31  -- Total global

UNION ALL

SELECT 
    'Stock' as cubo,
    NULL::numeric,
    NULL::numeric,
    NULL::numeric,
    NULL::numeric,
    ROUND(AVG(stock_capacity_pct), 2) as capacidad_utilizada_pct
FROM dwh_cube.cube_store_stock
WHERE gid = 7  -- Total por tienda (sin producto ni fecha)
  AND store_sk IS NOT NULL;

\echo ''
\echo '=== VALOR TOTAL DEL INVENTARIO ==='
\echo ''

SELECT 
    ROUND(SUM(stock_value), 2) as valor_total_inventario
FROM dwh_cube.cube_store_stock
WHERE gid = 7  -- Total global
  AND store_sk IS NULL;

\echo ''
\echo '=== Test Completado ==='

