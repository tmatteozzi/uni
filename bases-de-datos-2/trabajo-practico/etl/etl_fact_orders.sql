-- etl_fact_orders.sql
-- Updated to use surrogate keys from dimensions
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
    ),
    -- Join with current dimension records to get surrogate keys
    src_with_sk AS (
        SELECT
            src.order_date_id,
            src.requirement_date_id,
            dc.customer_sk,
            ds.staff_sk,
            dst.store_sk,
            dp.product_sk,
            src.order_id,
            src.quantity,
            src.list_price,
            src.discount,
            src.order_amount,
            src.discounted_order_amount
        FROM src
        JOIN dwh.dim_customer dc ON src.customer_id = dc.customer_id AND dc.is_current = true
        JOIN dwh.dim_staff ds ON src.staff_id = ds.staff_id AND ds.is_current = true
        JOIN dwh.dim_store dst ON src.store_id = dst.store_id AND dst.is_current = true
        JOIN dwh.dim_product dp ON src.product_id = dp.product_id AND dp.is_current = true
    ),
    inserted AS (
        INSERT INTO dwh.fact_bike_order (
            order_date_id, requirement_date_id, customer_sk, staff_sk, store_sk, product_sk, 
            order_id, quantity, list_price, discount, order_amount, discounted_order_amount
        )
        SELECT 
            order_date_id, requirement_date_id, customer_sk, staff_sk, store_sk, product_sk,
            order_id, quantity, list_price, discount, order_amount, discounted_order_amount
        FROM src_with_sk
        ON CONFLICT (order_id, product_sk) DO NOTHING
        RETURNING 1
    )
    SELECT count(*) INTO rows_count FROM inserted;

    UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='fact_orders';
    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('fact_orders', now(), 'OK', rows_count, 'Fact orders increment loaded with surrogate keys');
END;
$$;


-- Updated to use surrogate keys from dimensions
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
            to_char(o.order_date,'yyyymmdd')::int as order_date_id,
            o.customer_id, 
            o.staff_id, 
            o.store_id, 
            oi.product_id, 
            o.order_id,
            oi.quantity, 
            oi.list_price, 
            oi.discount,
            oi.list_price * oi.quantity as shipment_amount,
            (oi.list_price - oi.discount) * oi.quantity as discounted_shipment_amount,
            (oi.list_price * 0.6) * oi.quantity as estimated_cost,
            (o.shipped_date - o.order_date)::int as shipping_days
        FROM bike_stores.orders o
        JOIN bike_stores.order_items oi ON o.order_id = oi.order_id
        WHERE o.shipped_date IS NOT NULL
          AND o.shipped_date > coalesce(wm::date, '1970-01-01'::date)
    ),
    -- Join with current dimension records to get surrogate keys
    src_with_sk AS (
        SELECT
            src.shipment_date_id,
            src.order_date_id,
            dc.customer_sk,
            ds.staff_sk,
            dst.store_sk,
            dp.product_sk,
            src.order_id,
            src.quantity,
            src.list_price,
            src.discount,
            src.shipment_amount,
            src.discounted_shipment_amount,
            src.estimated_cost,
            src.shipping_days
        FROM src
        JOIN dwh.dim_customer dc ON src.customer_id = dc.customer_id AND dc.is_current = true
        JOIN dwh.dim_staff ds ON src.staff_id = ds.staff_id AND ds.is_current = true
        JOIN dwh.dim_store dst ON src.store_id = dst.store_id AND dst.is_current = true
        JOIN dwh.dim_product dp ON src.product_id = dp.product_id AND dp.is_current = true
    ),
    inserted AS (
        INSERT INTO dwh.fact_bike_shipment (
            shipment_date_id, order_date_id, customer_sk, staff_sk, store_sk, product_sk, order_id, 
            quantity, list_price, discount, shipment_amount, discounted_shipment_amount, 
            estimated_cost, shipping_days
        )
        SELECT 
            shipment_date_id, order_date_id, customer_sk, staff_sk, store_sk, product_sk, order_id,
            quantity, list_price, discount, shipment_amount, discounted_shipment_amount,
            estimated_cost, shipping_days
        FROM src_with_sk
        ON CONFLICT (order_id, product_sk) DO NOTHING
        RETURNING 1
    )
    SELECT count(*) INTO rows_count FROM inserted;

    UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='fact_shipments';
    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('fact_shipments', now(), 'OK', rows_count, 'Fact shipments increment loaded with surrogate keys');
END;
$$;


-- Updated to use surrogate keys from dimensions
CREATE OR REPLACE FUNCTION dwh.etl_fact_store_stock()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE
    wm timestamptz;
    rows_count int := 0;
BEGIN
    SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='fact_store_stock';

    WITH src AS (
        SELECT 
            to_char('2021-06-23'::date,'yyyymmdd')::int as date_id, 
            s.store_id, 
            s.product_id, 
            s.quantity
        FROM bike_stores.stocks s
        WHERE s.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
    ),
    -- Join with current dimension records to get surrogate keys
    src_with_sk AS (
        SELECT
            src.date_id,
            dst.store_sk,
            dp.product_sk,
            src.quantity
        FROM src
        JOIN dwh.dim_store dst ON src.store_id = dst.store_id AND dst.is_current = true
        JOIN dwh.dim_product dp ON src.product_id = dp.product_id AND dp.is_current = true
    ),
    upsert AS (
        INSERT INTO dwh.fact_store_stock (date_id, store_sk, product_sk, quantity)
        SELECT date_id, store_sk, product_sk, quantity 
        FROM src_with_sk
        ON CONFLICT (date_id, store_sk, product_sk) DO UPDATE
            SET quantity = EXCLUDED.quantity
        RETURNING 1
    )
    SELECT count(*) INTO rows_count FROM upsert;

    UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='fact_store_stock';
    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('fact_store_stock', now(), 'OK', rows_count, 'Fact store stock updated with surrogate keys');
END;
$$;
