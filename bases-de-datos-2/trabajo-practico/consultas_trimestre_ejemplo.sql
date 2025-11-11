-- ========================================
-- CONSULTAS DE MÉTRICAS POR TRIMESTRE
-- ========================================
-- Este archivo contiene ejemplos de cómo consultar las métricas
-- de un trimestre específico o comparar trimestres

-- ========================================
-- 1. MÉTRICAS DEL PRIMER TRIMESTRE - ORDERS
-- ========================================

-- Q1 de un año específico (ejemplo: 2017)
SELECT 
    'Q1 2017' as periodo,
    SUM(co.rows) as total_items_vendidos,
    COUNT(DISTINCT co.order_date_id) as dias_con_ventas,
    ROUND(SUM(co.final_price), 2) as ventas_totales,
    ROUND(AVG(co.avg_ticket), 2) as ticket_promedio,
    ROUND(AVG(co.discount_margin_pct), 2) as margen_descuento_pct
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE dd.year = 2017
  AND dd.quarter = 1
  AND co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL;

-- ========================================
-- 2. MÉTRICAS DEL PRIMER TRIMESTRE - SHIPMENTS
-- ========================================

SELECT 
    'Q1 2017' as periodo,
    SUM(cs.rows) as total_envios,
    SUM(cs.quantity) as productos_enviados,
    ROUND(SUM(cs.final_price), 2) as valor_enviado,
    ROUND(AVG(cs.profit_margin_pct), 2) as profit_margin_pct,
    ROUND(AVG(cs.avg_shipping_days), 1) as dias_envio_promedio
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id
WHERE dd.year = 2017
  AND dd.quarter = 1
  AND cs.shipment_date_id IS NOT NULL
  AND cs.store_sk IS NULL
  AND cs.product_sk IS NULL
  AND cs.customer_sk IS NULL
  AND cs.staff_sk IS NULL;

-- ========================================
-- 3. COMPARACIÓN DE Q1 A TRAVÉS DE LOS AÑOS
-- ========================================

SELECT 
    dd.year,
    'Q1' as trimestre,
    SUM(co.rows) as items_vendidos,
    ROUND(SUM(co.final_price), 2) as ventas_totales,
    ROUND(AVG(co.avg_ticket), 2) as ticket_promedio,
    ROUND(AVG(co.discount_margin_pct), 2) as margen_descuento
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE dd.quarter = 1  -- Solo Q1 de todos los años
  AND co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dd.year
ORDER BY dd.year;

-- ========================================
-- 4. MÉTRICAS DEL Q1 POR TIENDA
-- ========================================

SELECT 
    dst.store_name,
    SUM(co.rows) as items_vendidos,
    ROUND(SUM(co.final_price), 2) as ventas_totales,
    ROUND(AVG(co.avg_ticket), 2) as ticket_promedio,
    ROUND(AVG(co.discount_margin_pct), 2) as margen_descuento
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
JOIN dwh.dim_store dst ON co.store_sk = dst.store_sk
WHERE dd.year = 2017
  AND dd.quarter = 1
  AND co.order_date_id IS NOT NULL
  AND co.store_sk IS NOT NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dst.store_name
ORDER BY ventas_totales DESC;

-- ========================================
-- 5. COMPARACIÓN DE TODOS LOS TRIMESTRES DE UN AÑO
-- ========================================

SELECT 
    dd.year,
    'Q' || dd.quarter as trimestre,
    SUM(co.rows) as items_vendidos,
    ROUND(SUM(co.final_price), 2) as ventas_totales,
    ROUND(AVG(co.avg_ticket), 2) as ticket_promedio,
    ROUND(AVG(co.discount_margin_pct), 2) as margen_descuento
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE dd.year = 2017
  AND co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dd.year, dd.quarter
ORDER BY dd.quarter;

-- ========================================
-- 6. MÉTRICAS DE SHIPMENTS POR TRIMESTRE Y TIENDA
-- ========================================

SELECT 
    dd.year,
    'Q' || dd.quarter as trimestre,
    dst.store_name,
    SUM(cs.rows) as envios,
    ROUND(AVG(cs.profit_margin_pct), 2) as profit_margin,
    ROUND(AVG(cs.avg_shipping_days), 1) as dias_envio
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id
JOIN dwh.dim_store dst ON cs.store_sk = dst.store_sk
WHERE dd.year = 2017
  AND dd.quarter = 1
  AND cs.shipment_date_id IS NOT NULL
  AND cs.store_sk IS NOT NULL
  AND cs.product_sk IS NULL
  AND cs.customer_sk IS NULL
  AND cs.staff_sk IS NULL
GROUP BY dd.year, dd.quarter, dst.store_name
ORDER BY dst.store_name;

-- ========================================
-- 7. TOP 10 PRODUCTOS VENDIDOS EN Q1
-- ========================================

SELECT 
    dp.product_name,
    dp.brand_name,
    dp.category_name,
    SUM(co.quantity) as cantidad_vendida,
    ROUND(SUM(co.final_price), 2) as ventas_totales,
    ROUND(AVG(co.final_price / co.quantity), 2) as precio_promedio
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
JOIN dwh.dim_product dp ON co.product_sk = dp.product_sk
WHERE dd.year = 2017
  AND dd.quarter = 1
  AND co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NOT NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dp.product_name, dp.brand_name, dp.category_name
ORDER BY cantidad_vendida DESC
LIMIT 10;

-- ========================================
-- 8. EVOLUCIÓN MENSUAL DENTRO DEL Q1
-- ========================================

SELECT 
    dd.year,
    dd.month,
    dd.month_name,
    SUM(co.rows) as items_vendidos,
    ROUND(SUM(co.final_price), 2) as ventas,
    ROUND(AVG(co.avg_ticket), 2) as ticket_promedio
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE dd.year = 2017
  AND dd.quarter = 1
  AND co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dd.year, dd.month, dd.month_name
ORDER BY dd.month;

-- ========================================
-- 9. COMPARACIÓN ENTRE TRIMESTRES (YoY)
-- ========================================
-- Year over Year: Q1 2016 vs Q1 2017 vs Q1 2018

WITH q1_metrics AS (
    SELECT 
        dd.year,
        SUM(co.rows) as items,
        ROUND(SUM(co.final_price), 2) as ventas,
        ROUND(AVG(co.avg_ticket), 2) as ticket,
        ROUND(AVG(co.discount_margin_pct), 2) as margen
    FROM dwh_cube.cube_bike_order co
    JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
    WHERE dd.quarter = 1
      AND co.order_date_id IS NOT NULL
      AND co.store_sk IS NULL
      AND co.product_sk IS NULL
      AND co.customer_sk IS NULL
      AND co.staff_sk IS NULL
    GROUP BY dd.year
)
SELECT 
    year,
    items,
    ventas,
    ticket,
    margen,
    -- Calcular crecimiento vs año anterior
    ROUND(((ventas - LAG(ventas) OVER (ORDER BY year)) / 
           LAG(ventas) OVER (ORDER BY year) * 100), 2) as crecimiento_ventas_pct,
    ROUND(((ticket - LAG(ticket) OVER (ORDER BY year)) / 
           LAG(ticket) OVER (ORDER BY year) * 100), 2) as crecimiento_ticket_pct
FROM q1_metrics
ORDER BY year;

-- ========================================
-- 10. RESUMEN EJECUTIVO DEL TRIMESTRE
-- ========================================

SELECT 
    'Q1 2017 - Resumen Ejecutivo' as reporte,
    -- Orders
    (SELECT SUM(rows) FROM dwh_cube.cube_bike_order co 
     JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id 
     WHERE dd.year = 2017 AND dd.quarter = 1 AND gid = 31) as total_items,
    (SELECT ROUND(SUM(final_price), 2) FROM dwh_cube.cube_bike_order co 
     JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id 
     WHERE dd.year = 2017 AND dd.quarter = 1 AND gid = 31) as ventas_totales,
    (SELECT ROUND(AVG(avg_ticket), 2) FROM dwh_cube.cube_bike_order co 
     JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id 
     WHERE dd.year = 2017 AND dd.quarter = 1 AND gid = 31) as ticket_promedio,
    -- Shipments
    (SELECT ROUND(AVG(profit_margin_pct), 2) FROM dwh_cube.cube_bike_shipment cs 
     JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id 
     WHERE dd.year = 2017 AND dd.quarter = 1 AND gid = 31) as profit_margin,
    (SELECT ROUND(AVG(avg_shipping_days), 1) FROM dwh_cube.cube_bike_shipment cs 
     JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id 
     WHERE dd.year = 2017 AND dd.quarter = 1 AND gid = 31) as dias_envio;

-- ========================================
-- NOTAS DE USO:
-- ========================================
-- 
-- Para cambiar el trimestre, modifica:
--   dd.quarter = 1  →  dd.quarter = 2 (para Q2)
--                   →  dd.quarter = 3 (para Q3)
--                   →  dd.quarter = 4 (para Q4)
--
-- Para cambiar el año, modifica:
--   dd.year = 2017  →  dd.year = 2018
--
-- Para filtrar por tienda específica:
--   Agrega: AND dst.store_id = 1 (o 2, o 3)
--
-- La columna 'gid' en los cubos representa el nivel de agregación:
--   gid = 31 → Agregación total (todas las dimensiones NULL)
--   Valores más bajos → Agregaciones más específicas
--
-- ========================================

