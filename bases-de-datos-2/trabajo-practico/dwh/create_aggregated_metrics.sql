-- create_aggregated_metrics.sql
-- =========================================================
-- Tabla para almacenar métricas agregadas acumulativas
-- =========================================================

CREATE SCHEMA IF NOT EXISTS dwh_metrics;

-- Tabla de métricas agregadas
DROP TABLE IF EXISTS dwh_metrics.aggregated_metrics CASCADE;

CREATE TABLE dwh_metrics.aggregated_metrics (
    metric_id SERIAL PRIMARY KEY,
    cube_name TEXT NOT NULL,
    metric_name TEXT NOT NULL,
    -- Dimensiones (null cuando está agregado)
    order_date_id INT,
    shipment_date_id INT,
    date_id INT,
    store_sk INT,
    product_sk INT,
    customer_sk INT,
    staff_sk INT,
    gid INT NOT NULL,
    -- Métricas acumuladas
    rows BIGINT DEFAULT 0,
    quantity BIGINT DEFAULT 0,
    gross_amount NUMERIC(18,2) DEFAULT 0,
    final_price NUMERIC(18,2) DEFAULT 0,
    avg_discount NUMERIC(10,4) DEFAULT 0,
    avg_ticket NUMERIC(18,2) DEFAULT 0,
    discount_margin_pct NUMERIC(10,4) DEFAULT 0,
    profit_margin_pct NUMERIC(10,4) DEFAULT 0,
    avg_shipping_days NUMERIC(10,2) DEFAULT 0,
    total_stock BIGINT DEFAULT 0,
    stock_capacity_pct NUMERIC(10,2) DEFAULT 0,
    stock_value NUMERIC(18,2) DEFAULT 0,
    -- Metadata
    last_updated TIMESTAMPTZ DEFAULT NOW(),
    calculation_count INT DEFAULT 1
);

-- Índice único para identificar combinaciones únicas
CREATE UNIQUE INDEX idx_aggregated_metrics_unique 
ON dwh_metrics.aggregated_metrics(
    cube_name, 
    COALESCE(order_date_id, -1),
    COALESCE(shipment_date_id, -1),
    COALESCE(date_id, -1),
    COALESCE(store_sk, -1),
    COALESCE(product_sk, -1),
    COALESCE(customer_sk, -1),
    COALESCE(staff_sk, -1),
    gid
);

-- Índices auxiliares
CREATE INDEX idx_aggregated_metrics_cube ON dwh_metrics.aggregated_metrics(cube_name);
CREATE INDEX idx_aggregated_metrics_store ON dwh_metrics.aggregated_metrics(store_sk);
CREATE INDEX idx_aggregated_metrics_updated ON dwh_metrics.aggregated_metrics(last_updated);

