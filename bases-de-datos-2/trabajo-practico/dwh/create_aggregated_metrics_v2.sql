-- create_aggregated_metrics_v2.sql
-- =========================================================
-- Tabla para almacenar snapshots históricos de métricas
-- Las métricas se calculan y se guardan con timestamp para
-- ver la evolución en el tiempo
-- =========================================================

CREATE SCHEMA IF NOT EXISTS dwh_metrics;

-- Tabla de snapshots de métricas (histórico)
DROP TABLE IF EXISTS dwh_metrics.metrics_snapshots CASCADE;

CREATE TABLE dwh_metrics.metrics_snapshots (
    snapshot_id SERIAL PRIMARY KEY,
    snapshot_date TIMESTAMPTZ DEFAULT NOW(),
    cube_name TEXT NOT NULL,
    -- Dimensiones (null cuando está agregado)
    order_date_id INT,
    shipment_date_id INT,
    date_id INT,
    store_sk INT,
    product_sk INT,
    customer_sk INT,
    staff_sk INT,
    gid INT NOT NULL,
    -- Métricas del snapshot
    rows BIGINT,
    quantity BIGINT,
    gross_amount NUMERIC(18,2),
    final_price NUMERIC(18,2),
    avg_discount NUMERIC(10,4),
    avg_ticket NUMERIC(18,2),
    discount_margin_pct NUMERIC(10,4),
    profit_margin_pct NUMERIC(10,4),
    avg_shipping_days NUMERIC(10,2),
    total_stock BIGINT,
    stock_capacity_pct NUMERIC(10,2),
    stock_value NUMERIC(18,2)
);

-- Índices
CREATE INDEX idx_metrics_snapshots_date ON dwh_metrics.metrics_snapshots(snapshot_date);
CREATE INDEX idx_metrics_snapshots_cube ON dwh_metrics.metrics_snapshots(cube_name);
CREATE INDEX idx_metrics_snapshots_store ON dwh_metrics.metrics_snapshots(store_sk);

-- Vista para obtener el último snapshot (métricas actuales)
CREATE OR REPLACE VIEW dwh_metrics.v_current_metrics AS
SELECT DISTINCT ON (cube_name, COALESCE(order_date_id, -1), COALESCE(shipment_date_id, -1), 
                    COALESCE(date_id, -1), COALESCE(store_sk, -1), COALESCE(product_sk, -1),
                    COALESCE(customer_sk, -1), COALESCE(staff_sk, -1), gid)
    snapshot_date,
    cube_name,
    order_date_id,
    shipment_date_id,
    date_id,
    store_sk,
    product_sk,
    customer_sk,
    staff_sk,
    gid,
    rows,
    quantity,
    gross_amount,
    final_price,
    avg_discount,
    avg_ticket,
    discount_margin_pct,
    profit_margin_pct,
    avg_shipping_days,
    total_stock,
    stock_capacity_pct,
    stock_value
FROM dwh_metrics.metrics_snapshots
ORDER BY cube_name, COALESCE(order_date_id, -1), COALESCE(shipment_date_id, -1),
         COALESCE(date_id, -1), COALESCE(store_sk, -1), COALESCE(product_sk, -1),
         COALESCE(customer_sk, -1), COALESCE(staff_sk, -1), gid, snapshot_date DESC;

COMMENT ON VIEW dwh_metrics.v_current_metrics IS 
'Vista que muestra el último snapshot de cada métrica (estado actual)';

-- Vista para comparar evolución de métricas
CREATE OR REPLACE VIEW dwh_metrics.v_metrics_evolution AS
WITH current AS (
    SELECT * FROM dwh_metrics.v_current_metrics
),
previous AS (
    SELECT DISTINCT ON (cube_name, COALESCE(order_date_id, -1), COALESCE(shipment_date_id, -1),
                        COALESCE(date_id, -1), COALESCE(store_sk, -1), COALESCE(product_sk, -1),
                        COALESCE(customer_sk, -1), COALESCE(staff_sk, -1), gid)
        cube_name,
        order_date_id,
        shipment_date_id,
        date_id,
        store_sk,
        product_sk,
        customer_sk,
        staff_sk,
        gid,
        rows as prev_rows,
        quantity as prev_quantity,
        final_price as prev_final_price
    FROM dwh_metrics.metrics_snapshots
    WHERE snapshot_date < (SELECT MAX(snapshot_date) FROM dwh_metrics.metrics_snapshots)
    ORDER BY cube_name, COALESCE(order_date_id, -1), COALESCE(shipment_date_id, -1),
             COALESCE(date_id, -1), COALESCE(store_sk, -1), COALESCE(product_sk, -1),
             COALESCE(customer_sk, -1), COALESCE(staff_sk, -1), gid, snapshot_date DESC
)
SELECT 
    c.snapshot_date,
    c.cube_name,
    c.store_sk,
    c.product_sk,
    c.rows as current_rows,
    p.prev_rows,
    c.rows - COALESCE(p.prev_rows, 0) as rows_delta,
    c.quantity as current_quantity,
    p.prev_quantity,
    c.quantity - COALESCE(p.prev_quantity, 0) as quantity_delta,
    c.final_price as current_final_price,
    p.prev_final_price,
    c.final_price - COALESCE(p.prev_final_price, 0) as final_price_delta
FROM current c
LEFT JOIN previous p 
    ON c.cube_name = p.cube_name 
    AND COALESCE(c.order_date_id, -1) = COALESCE(p.order_date_id, -1)
    AND COALESCE(c.shipment_date_id, -1) = COALESCE(p.shipment_date_id, -1)
    AND COALESCE(c.date_id, -1) = COALESCE(p.date_id, -1)
    AND COALESCE(c.store_sk, -1) = COALESCE(p.store_sk, -1)
    AND COALESCE(c.product_sk, -1) = COALESCE(p.product_sk, -1)
    AND COALESCE(c.customer_sk, -1) = COALESCE(p.customer_sk, -1)
    AND COALESCE(c.staff_sk, -1) = COALESCE(p.staff_sk, -1)
    AND c.gid = p.gid;

COMMENT ON VIEW dwh_metrics.v_metrics_evolution IS 
'Vista que muestra la evolución de métricas comparando el snapshot actual con el anterior';


