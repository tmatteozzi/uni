-- =========================================================
-- VISTAS HISTÓRICAS DE MÉTRICAS
-- Facilitan el análisis temporal de las métricas en los cubos
-- =========================================================

-- =========================================================
-- VISTAS PARA CUBE_BIKE_ORDER (ÓRDENES)
-- =========================================================

-- Vista: Métricas diarias de órdenes
CREATE OR REPLACE VIEW dwh_cube.v_orders_daily AS
SELECT 
    co.order_date_id,
    dd.date,
    dd.day_name,
    dd.day_of_month,
    dd.week_of_month,
    dd.week_of_year,
    dd.month,
    dd.month_name,
    dd.quarter,
    dd.year,
    dd.is_weekend,
    co.rows as total_items,
    co.quantity as total_quantity,
    co.gross_amount,
    co.final_price,
    ROUND(co.avg_ticket, 2) as avg_ticket,
    ROUND(co.discount_margin_pct, 2) as discount_margin_pct
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL;

COMMENT ON VIEW dwh_cube.v_orders_daily IS 'Métricas diarias agregadas de órdenes con información de fecha';

-- Vista: Métricas mensuales de órdenes
CREATE OR REPLACE VIEW dwh_cube.v_orders_monthly AS
SELECT 
    dd.year,
    dd.month,
    dd.month_name,
    COUNT(DISTINCT co.order_date_id) as dias_con_ventas,
    SUM(co.rows) as total_items,
    SUM(co.quantity) as total_quantity,
    ROUND(SUM(co.gross_amount), 2) as gross_amount,
    ROUND(SUM(co.final_price), 2) as final_price,
    ROUND(AVG(co.avg_ticket), 2) as avg_ticket,
    ROUND(AVG(co.discount_margin_pct), 2) as discount_margin_pct
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dd.year, dd.month, dd.month_name;

COMMENT ON VIEW dwh_cube.v_orders_monthly IS 'Métricas mensuales agregadas de órdenes';

-- Vista: Métricas trimestrales de órdenes
CREATE OR REPLACE VIEW dwh_cube.v_orders_quarterly AS
SELECT 
    dd.year,
    dd.quarter,
    'Q' || dd.quarter as quarter_label,
    COUNT(DISTINCT co.order_date_id) as dias_con_ventas,
    SUM(co.rows) as total_items,
    SUM(co.quantity) as total_quantity,
    ROUND(SUM(co.gross_amount), 2) as gross_amount,
    ROUND(SUM(co.final_price), 2) as final_price,
    ROUND(AVG(co.avg_ticket), 2) as avg_ticket,
    ROUND(AVG(co.discount_margin_pct), 2) as discount_margin_pct
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dd.year, dd.quarter;

COMMENT ON VIEW dwh_cube.v_orders_quarterly IS 'Métricas trimestrales agregadas de órdenes';

-- Vista: Métricas anuales de órdenes
CREATE OR REPLACE VIEW dwh_cube.v_orders_yearly AS
SELECT 
    dd.year,
    COUNT(DISTINCT co.order_date_id) as dias_con_ventas,
    SUM(co.rows) as total_items,
    SUM(co.quantity) as total_quantity,
    ROUND(SUM(co.gross_amount), 2) as gross_amount,
    ROUND(SUM(co.final_price), 2) as final_price,
    ROUND(AVG(co.avg_ticket), 2) as avg_ticket,
    ROUND(AVG(co.discount_margin_pct), 2) as discount_margin_pct
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
WHERE co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dd.year;

COMMENT ON VIEW dwh_cube.v_orders_yearly IS 'Métricas anuales agregadas de órdenes';

-- Vista: Métricas de órdenes por tienda y mes
CREATE OR REPLACE VIEW dwh_cube.v_orders_store_monthly AS
SELECT 
    dst.store_id,
    dst.store_name,
    dst.city,
    dd.year,
    dd.month,
    dd.month_name,
    COUNT(DISTINCT co.order_date_id) as dias_con_ventas,
    SUM(co.rows) as total_items,
    SUM(co.quantity) as total_quantity,
    ROUND(SUM(co.final_price), 2) as final_price,
    ROUND(AVG(co.avg_ticket), 2) as avg_ticket,
    ROUND(AVG(co.discount_margin_pct), 2) as discount_margin_pct
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
JOIN dwh.dim_store dst ON co.store_sk = dst.store_sk
WHERE co.order_date_id IS NOT NULL
  AND co.store_sk IS NOT NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
  AND dst.is_current = true
GROUP BY dst.store_id, dst.store_name, dst.city, dd.year, dd.month, dd.month_name;

COMMENT ON VIEW dwh_cube.v_orders_store_monthly IS 'Métricas mensuales de órdenes por tienda';

-- =========================================================
-- VISTAS PARA CUBE_BIKE_SHIPMENT (ENVÍOS)
-- =========================================================

-- Vista: Métricas diarias de envíos
CREATE OR REPLACE VIEW dwh_cube.v_shipments_daily AS
SELECT 
    cs.shipment_date_id,
    dd.date,
    dd.day_name,
    dd.day_of_month,
    dd.week_of_month,
    dd.week_of_year,
    dd.month,
    dd.month_name,
    dd.quarter,
    dd.year,
    dd.is_weekend,
    cs.rows as total_shipments,
    cs.quantity as total_quantity,
    cs.gross_amount,
    cs.final_price,
    ROUND(cs.profit_margin_pct, 2) as profit_margin_pct,
    ROUND(cs.avg_shipping_days, 1) as avg_shipping_days
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id
WHERE cs.shipment_date_id IS NOT NULL
  AND cs.store_sk IS NULL
  AND cs.product_sk IS NULL
  AND cs.customer_sk IS NULL
  AND cs.staff_sk IS NULL;

COMMENT ON VIEW dwh_cube.v_shipments_daily IS 'Métricas diarias agregadas de envíos con información de fecha';

-- Vista: Métricas mensuales de envíos
CREATE OR REPLACE VIEW dwh_cube.v_shipments_monthly AS
SELECT 
    dd.year,
    dd.month,
    dd.month_name,
    COUNT(DISTINCT cs.shipment_date_id) as dias_con_envios,
    SUM(cs.rows) as total_shipments,
    SUM(cs.quantity) as total_quantity,
    ROUND(SUM(cs.gross_amount), 2) as gross_amount,
    ROUND(SUM(cs.final_price), 2) as final_price,
    ROUND(AVG(cs.profit_margin_pct), 2) as profit_margin_pct,
    ROUND(AVG(cs.avg_shipping_days), 1) as avg_shipping_days
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id
WHERE cs.shipment_date_id IS NOT NULL
  AND cs.store_sk IS NULL
  AND cs.product_sk IS NULL
  AND cs.customer_sk IS NULL
  AND cs.staff_sk IS NULL
GROUP BY dd.year, dd.month, dd.month_name;

COMMENT ON VIEW dwh_cube.v_shipments_monthly IS 'Métricas mensuales agregadas de envíos';

-- Vista: Métricas trimestrales de envíos
CREATE OR REPLACE VIEW dwh_cube.v_shipments_quarterly AS
SELECT 
    dd.year,
    dd.quarter,
    'Q' || dd.quarter as quarter_label,
    COUNT(DISTINCT cs.shipment_date_id) as dias_con_envios,
    SUM(cs.rows) as total_shipments,
    SUM(cs.quantity) as total_quantity,
    ROUND(SUM(cs.gross_amount), 2) as gross_amount,
    ROUND(SUM(cs.final_price), 2) as final_price,
    ROUND(AVG(cs.profit_margin_pct), 2) as profit_margin_pct,
    ROUND(AVG(cs.avg_shipping_days), 1) as avg_shipping_days
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id
WHERE cs.shipment_date_id IS NOT NULL
  AND cs.store_sk IS NULL
  AND cs.product_sk IS NULL
  AND cs.customer_sk IS NULL
  AND cs.staff_sk IS NULL
GROUP BY dd.year, dd.quarter;

COMMENT ON VIEW dwh_cube.v_shipments_quarterly IS 'Métricas trimestrales agregadas de envíos';

-- Vista: Métricas anuales de envíos
CREATE OR REPLACE VIEW dwh_cube.v_shipments_yearly AS
SELECT 
    dd.year,
    COUNT(DISTINCT cs.shipment_date_id) as dias_con_envios,
    SUM(cs.rows) as total_shipments,
    SUM(cs.quantity) as total_quantity,
    ROUND(SUM(cs.gross_amount), 2) as gross_amount,
    ROUND(SUM(cs.final_price), 2) as final_price,
    ROUND(AVG(cs.profit_margin_pct), 2) as profit_margin_pct,
    ROUND(AVG(cs.avg_shipping_days), 1) as avg_shipping_days
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id
WHERE cs.shipment_date_id IS NOT NULL
  AND cs.store_sk IS NULL
  AND cs.product_sk IS NULL
  AND cs.customer_sk IS NULL
  AND cs.staff_sk IS NULL
GROUP BY dd.year;

COMMENT ON VIEW dwh_cube.v_shipments_yearly IS 'Métricas anuales agregadas de envíos';

-- Vista: Métricas de envíos por tienda y mes
CREATE OR REPLACE VIEW dwh_cube.v_shipments_store_monthly AS
SELECT 
    dst.store_id,
    dst.store_name,
    dst.city,
    dd.year,
    dd.month,
    dd.month_name,
    COUNT(DISTINCT cs.shipment_date_id) as dias_con_envios,
    SUM(cs.rows) as total_shipments,
    SUM(cs.quantity) as total_quantity,
    ROUND(SUM(cs.final_price), 2) as final_price,
    ROUND(AVG(cs.profit_margin_pct), 2) as profit_margin_pct,
    ROUND(AVG(cs.avg_shipping_days), 1) as avg_shipping_days
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_date dd ON cs.shipment_date_id = dd.date_id
JOIN dwh.dim_store dst ON cs.store_sk = dst.store_sk
WHERE cs.shipment_date_id IS NOT NULL
  AND cs.store_sk IS NOT NULL
  AND cs.product_sk IS NULL
  AND cs.customer_sk IS NULL
  AND cs.staff_sk IS NULL
  AND dst.is_current = true
GROUP BY dst.store_id, dst.store_name, dst.city, dd.year, dd.month, dd.month_name;

COMMENT ON VIEW dwh_cube.v_shipments_store_monthly IS 'Métricas mensuales de envíos por tienda';

-- =========================================================
-- VISTAS PARA CUBE_STORE_STOCK (INVENTARIO)
-- =========================================================

-- Vista: Stock actual por tienda
CREATE OR REPLACE VIEW dwh_cube.v_stock_current_by_store AS
SELECT 
    dst.store_id,
    dst.store_name,
    dst.city,
    css.total_stock,
    ROUND(css.stock_capacity_pct, 2) as stock_capacity_pct,
    ROUND(css.stock_value, 2) as stock_value,
    sc.max_capacity
FROM dwh_cube.cube_store_stock css
JOIN dwh.dim_store dst ON css.store_sk = dst.store_sk
JOIN dwh.store_capacity sc ON css.store_sk = sc.store_sk
WHERE css.store_sk IS NOT NULL
  AND css.date_id IS NULL
  AND css.product_sk IS NULL
  AND dst.is_current = true;

COMMENT ON VIEW dwh_cube.v_stock_current_by_store IS 'Stock actual agregado por tienda con métricas de capacidad y valor';

-- Vista: Stock por producto y tienda
CREATE OR REPLACE VIEW dwh_cube.v_stock_by_product_store AS
SELECT 
    dst.store_id,
    dst.store_name,
    dp.product_id,
    dp.product_name,
    dp.brand_name,
    dp.category_name,
    dp.list_price,
    css.total_stock,
    ROUND(css.stock_value, 2) as stock_value
FROM dwh_cube.cube_store_stock css
JOIN dwh.dim_store dst ON css.store_sk = dst.store_sk
JOIN dwh.dim_product dp ON css.product_sk = dp.product_sk
WHERE css.store_sk IS NOT NULL
  AND css.date_id IS NOT NULL
  AND css.product_sk IS NOT NULL
  AND dst.is_current = true
  AND dp.is_current = true;

COMMENT ON VIEW dwh_cube.v_stock_by_product_store IS 'Stock por producto y tienda con información detallada del producto';

-- Vista: Resumen global de stock
CREATE OR REPLACE VIEW dwh_cube.v_stock_summary AS
SELECT 
    COUNT(DISTINCT dst.store_id) as total_stores,
    SUM(css.total_stock) as total_stock_units,
    ROUND(SUM(css.stock_value), 2) as total_stock_value,
    ROUND(AVG(css.stock_capacity_pct), 2) as avg_capacity_pct
FROM dwh_cube.cube_store_stock css
LEFT JOIN dwh.dim_store dst ON css.store_sk = dst.store_sk
WHERE css.store_sk IS NOT NULL
  AND css.date_id IS NULL
  AND css.product_sk IS NULL
  AND (dst.is_current = true OR dst.is_current IS NULL);

COMMENT ON VIEW dwh_cube.v_stock_summary IS 'Resumen global del inventario con totales y promedios';

-- =========================================================
-- VISTAS COMPARATIVAS Y DE TENDENCIAS
-- =========================================================

-- Vista: Comparación Year over Year - Órdenes
CREATE OR REPLACE VIEW dwh_cube.v_orders_yoy_comparison AS
WITH monthly_data AS (
    SELECT 
        dd.year,
        dd.month,
        dd.month_name,
        SUM(co.rows) as total_items,
        ROUND(SUM(co.final_price), 2) as final_price,
        ROUND(AVG(co.avg_ticket), 2) as avg_ticket
    FROM dwh_cube.cube_bike_order co
    JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
    WHERE co.order_date_id IS NOT NULL
      AND co.store_sk IS NULL
      AND co.product_sk IS NULL
      AND co.customer_sk IS NULL
      AND co.staff_sk IS NULL
    GROUP BY dd.year, dd.month, dd.month_name
)
SELECT 
    year,
    month,
    month_name,
    total_items,
    final_price,
    avg_ticket,
    LAG(final_price) OVER (PARTITION BY month ORDER BY year) as final_price_prev_year,
    ROUND(
        ((final_price - LAG(final_price) OVER (PARTITION BY month ORDER BY year)) / 
         NULLIF(LAG(final_price) OVER (PARTITION BY month ORDER BY year), 0) * 100),
    2) as yoy_growth_pct
FROM monthly_data;

COMMENT ON VIEW dwh_cube.v_orders_yoy_comparison IS 'Comparación mensual Year over Year de órdenes con crecimiento porcentual';

-- Vista: Tendencia trimestral - Todas las métricas
CREATE OR REPLACE VIEW dwh_cube.v_metrics_quarterly_trend AS
SELECT 
    dd.year,
    dd.quarter,
    'Q' || dd.quarter as quarter_label,
    -- Métricas de Orders
    SUM(co.rows) as orders_items,
    ROUND(SUM(co.final_price), 2) as orders_revenue,
    ROUND(AVG(co.avg_ticket), 2) as orders_avg_ticket,
    ROUND(AVG(co.discount_margin_pct), 2) as orders_discount_margin,
    -- Métricas de Shipments
    SUM(cs.rows) as shipments_count,
    ROUND(AVG(cs.profit_margin_pct), 2) as shipments_profit_margin,
    ROUND(AVG(cs.avg_shipping_days), 1) as shipments_avg_days
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_date dd ON co.order_date_id = dd.date_id
LEFT JOIN dwh_cube.cube_bike_shipment cs ON 
    cs.shipment_date_id = co.order_date_id AND
    cs.store_sk IS NULL AND cs.product_sk IS NULL AND 
    cs.customer_sk IS NULL AND cs.staff_sk IS NULL
WHERE co.order_date_id IS NOT NULL
  AND co.store_sk IS NULL
  AND co.product_sk IS NULL
  AND co.customer_sk IS NULL
  AND co.staff_sk IS NULL
GROUP BY dd.year, dd.quarter;

COMMENT ON VIEW dwh_cube.v_metrics_quarterly_trend IS 'Tendencia trimestral consolidada de todas las métricas principales';

-- =========================================================
-- Vista consolidada para dashboards
-- =========================================================

CREATE OR REPLACE VIEW dwh_cube.v_dashboard_kpis AS
SELECT 
    'Global' as scope,
    -- Orders KPIs
    (SELECT SUM(rows) FROM dwh_cube.cube_bike_order WHERE gid = 31) as total_orders_items,
    (SELECT ROUND(SUM(final_price), 2) FROM dwh_cube.cube_bike_order WHERE gid = 31) as total_orders_revenue,
    (SELECT ROUND(AVG(avg_ticket), 2) FROM dwh_cube.cube_bike_order WHERE gid = 31) as global_avg_ticket,
    (SELECT ROUND(AVG(discount_margin_pct), 2) FROM dwh_cube.cube_bike_order WHERE gid = 31) as global_discount_margin,
    -- Shipments KPIs
    (SELECT SUM(rows) FROM dwh_cube.cube_bike_shipment WHERE gid = 31) as total_shipments,
    (SELECT ROUND(AVG(profit_margin_pct), 2) FROM dwh_cube.cube_bike_shipment WHERE gid = 31) as global_profit_margin,
    (SELECT ROUND(AVG(avg_shipping_days), 1) FROM dwh_cube.cube_bike_shipment WHERE gid = 31) as global_shipping_days,
    -- Stock KPIs
    (SELECT SUM(total_stock) FROM dwh_cube.cube_store_stock WHERE gid = 7 AND store_sk IS NULL) as total_stock_units,
    (SELECT ROUND(SUM(stock_value), 2) FROM dwh_cube.cube_store_stock WHERE gid = 7 AND store_sk IS NULL) as total_stock_value,
    (SELECT ROUND(AVG(stock_capacity_pct), 2) FROM dwh_cube.cube_store_stock WHERE gid = 7 AND store_sk IS NOT NULL) as avg_capacity_utilization;

COMMENT ON VIEW dwh_cube.v_dashboard_kpis IS 'KPIs globales consolidados para dashboards ejecutivos';

-- =========================================================
-- Fin del script
-- =========================================================

