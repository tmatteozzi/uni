-- etl_fact_orders.sql
CREATE OR REPLACE FUNCTION dwh.etl_fact_orders()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE
wm timestamptz;
  rows_count int := 0;
BEGIN
SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='fact_orders';

WITH src AS (
    SELECT
        to_char(o.order_date,'yyyymmdd')::int as order_date_id,
            to_char(o.required_date,'yyyymmdd')::int as requirement_date_id,
            o.customer_id,
        o.staff_id,
        o.store_id,
        oi.product_id,
        o.order_id,
        oi.quantity,
        oi.list_price,
        oi.discount,
        oi.list_price * oi.quantity as order_amount,
        (oi.list_price - oi.discount) * oi.quantity as discounted_order_amount
    FROM bike_stores.orders o
             JOIN bike_stores.order_items oi ON o.order_id = oi.order_id
    WHERE o.order_date > coalesce(wm::date, '1970-01-01'::date)
), inserted AS (
INSERT INTO dwh.fact_bike_order (order_date_id, requirement_date_id, customer_id, staff_id, store_id, product_id, order_id, quantity, list_price, discount, order_amount, discounted_order_amount)
SELECT order_date_id, requirement_date_id, customer_id, staff_id, store_id, product_id, order_id, quantity, list_price, discount, order_amount, discounted_order_amount
FROM src
    ON CONFLICT (order_id, product_id) DO NOTHING
    RETURNING 1
  )
SELECT count(*) INTO rows_count FROM inserted;

UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='fact_orders';
INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
VALUES ('fact_orders', now(), 'OK', rows_count, 'Fact orders increment loaded');
END;
$$;


CREATE OR REPLACE FUNCTION dwh.etl_fact_shipments()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE
wm timestamptz;
  rows_count int := 0;
BEGIN
SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='fact_shipments';

WITH src AS (
    SELECT
        to_char(o.shipped_date,'yyyymmdd')::int as shipment_date_id,
            o.customer_id, o.staff_id, o.store_id, oi.product_id, o.order_id,
        oi.quantity, oi.list_price, oi.discount,
        oi.list_price * oi.quantity as shipment_amount,
        (oi.list_price - oi.discount) * oi.quantity as discounted_shipment_amount
    FROM bike_stores.orders o
             JOIN bike_stores.order_items oi ON o.order_id = oi.order_id
    WHERE o.shipped_date IS NOT NULL
      AND o.shipped_date > coalesce(wm::date, '1970-01-01'::date)
), inserted AS (
INSERT INTO dwh.fact_bike_shipment (shipment_date_id, customer_id, staff_id, store_id, product_id, order_id, quantity, list_price, discount, shipment_amount, discounted_shipment_amount)
SELECT shipment_date_id, customer_id, staff_id, store_id, product_id, order_id, quantity, list_price, discount, shipment_amount, discounted_shipment_amount
FROM src
    ON CONFLICT (order_id, product_id) DO NOTHING
    RETURNING 1
  )
SELECT count(*) INTO rows_count FROM inserted;

UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='fact_shipments';
INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
VALUES ('fact_shipments', now(), 'OK', rows_count, 'Fact shipments increment loaded');
END;
$$;


CREATE OR REPLACE FUNCTION dwh.etl_fact_store_stock()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE
wm timestamptz;
  rows_count int := 0;
BEGIN
SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='fact_store_stock';

WITH src AS (
    SELECT to_char('2021-06-23'::date,'yyyymmdd')::int as date_id, s.store_id, s.product_id, s.quantity
    FROM bike_stores.stocks s
    WHERE s.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
), upsert AS (
INSERT INTO dwh.fact_store_stock (date_id, store_id, product_id, quantity)
SELECT date_id, store_id, product_id, quantity FROM src
    ON CONFLICT (date_id, store_id, product_id) DO UPDATE
                                                       SET quantity = EXCLUDED.quantity
                                                       RETURNING 1
                                                       )
SELECT count(*) INTO rows_count FROM upsert;

UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='fact_store_stock';
INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
VALUES ('fact_store_stock', now(), 'OK', rows_count, 'Fact store stock updated');
END;
$$;
