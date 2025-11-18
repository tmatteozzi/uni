-- create_metrics_tables.sql
-- =========================================================
-- Tablas históricas para métricas específicas
-- Cada tabla guarda la evolución temporal de una métrica
-- =========================================================

CREATE SCHEMA IF NOT EXISTS dwh_metrics;

-- 1. Stock Value por store_sk y product_sk (histórico)
DROP TABLE IF EXISTS dwh_metrics.stock_value_history CASCADE;
CREATE TABLE dwh_metrics.stock_value_history (
    id SERIAL PRIMARY KEY,
    calculated_at TIMESTAMPTZ DEFAULT NOW(),
    store_sk INT NOT NULL,
    product_sk INT NOT NULL,
    stock_quantity BIGINT,
    stock_value NUMERIC(18,2),
    FOREIGN KEY (store_sk) REFERENCES dwh.dim_store(store_sk),
    FOREIGN KEY (product_sk) REFERENCES dwh.dim_product(product_sk)
);
CREATE INDEX idx_stock_value_history_date ON dwh_metrics.stock_value_history(calculated_at);
CREATE INDEX idx_stock_value_history_store_product ON dwh_metrics.stock_value_history(store_sk, product_sk);

-- 2. Store Capacity % por store (histórico)
DROP TABLE IF EXISTS dwh_metrics.store_capacity_history CASCADE;
CREATE TABLE dwh_metrics.store_capacity_history (
    id SERIAL PRIMARY KEY,
    calculated_at TIMESTAMPTZ DEFAULT NOW(),
    store_sk INT NOT NULL,
    total_stock BIGINT,
    max_capacity INT,
    capacity_pct NUMERIC(10,2),
    FOREIGN KEY (store_sk) REFERENCES dwh.dim_store(store_sk)
);
CREATE INDEX idx_store_capacity_history_date ON dwh_metrics.store_capacity_history(calculated_at);
CREATE INDEX idx_store_capacity_history_store ON dwh_metrics.store_capacity_history(store_sk);

-- 3. Profit Margin % por product_sk (histórico)
DROP TABLE IF EXISTS dwh_metrics.profit_margin_history CASCADE;
CREATE TABLE dwh_metrics.profit_margin_history (
    id SERIAL PRIMARY KEY,
    calculated_at TIMESTAMPTZ DEFAULT NOW(),
    product_sk INT NOT NULL,
    total_revenue NUMERIC(18,2),
    total_cost NUMERIC(18,2),
    profit_margin_pct NUMERIC(10,4),
    FOREIGN KEY (product_sk) REFERENCES dwh.dim_product(product_sk)
);
CREATE INDEX idx_profit_margin_history_date ON dwh_metrics.profit_margin_history(calculated_at);
CREATE INDEX idx_profit_margin_history_product ON dwh_metrics.profit_margin_history(product_sk);

-- 4. Avg Shipping Days por store_sk a lo largo del tiempo (histórico)
DROP TABLE IF EXISTS dwh_metrics.shipping_days_history CASCADE;
CREATE TABLE dwh_metrics.shipping_days_history (
    id SERIAL PRIMARY KEY,
    calculated_at TIMESTAMPTZ DEFAULT NOW(),
    store_sk INT NOT NULL,
    total_shipments BIGINT,
    avg_shipping_days NUMERIC(10,2),
    FOREIGN KEY (store_sk) REFERENCES dwh.dim_store(store_sk)
);
CREATE INDEX idx_shipping_days_history_date ON dwh_metrics.shipping_days_history(calculated_at);
CREATE INDEX idx_shipping_days_history_store ON dwh_metrics.shipping_days_history(store_sk);

-- 5. Avg Ticket a lo largo del tiempo (histórico global)
DROP TABLE IF EXISTS dwh_metrics.avg_ticket_history CASCADE;
CREATE TABLE dwh_metrics.avg_ticket_history (
    id SERIAL PRIMARY KEY,
    calculated_at TIMESTAMPTZ DEFAULT NOW(),
    total_orders BIGINT,
    total_revenue NUMERIC(18,2),
    avg_ticket NUMERIC(18,2)
);
CREATE INDEX idx_avg_ticket_history_date ON dwh_metrics.avg_ticket_history(calculated_at);

-- 6. Discount Margin por product_sk y store_sk (histórico)
DROP TABLE IF EXISTS dwh_metrics.discount_margin_history CASCADE;
CREATE TABLE dwh_metrics.discount_margin_history (
    id SERIAL PRIMARY KEY,
    calculated_at TIMESTAMPTZ DEFAULT NOW(),
    product_sk INT NOT NULL,
    store_sk INT NOT NULL,
    gross_amount NUMERIC(18,2),
    discounted_amount NUMERIC(18,2),
    discount_margin_pct NUMERIC(10,4),
    FOREIGN KEY (product_sk) REFERENCES dwh.dim_product(product_sk),
    FOREIGN KEY (store_sk) REFERENCES dwh.dim_store(store_sk)
);
CREATE INDEX idx_discount_margin_history_date ON dwh_metrics.discount_margin_history(calculated_at);
CREATE INDEX idx_discount_margin_history_product_store ON dwh_metrics.discount_margin_history(product_sk, store_sk);


