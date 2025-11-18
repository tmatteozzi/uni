-- etl_calculate_metrics.sql
-- =========================================================
-- Función para calcular y guardar métricas históricas
-- Cada ejecución agrega nuevos registros con timestamp actual
-- =========================================================

CREATE OR REPLACE FUNCTION dwh.etl_calculate_metrics()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE
    v_timestamp timestamptz;
    v_rows_processed int := 0;
BEGIN
    v_timestamp := NOW();
    
    RAISE NOTICE 'Calculating metrics at: %', v_timestamp;
    
    -- 1. Stock Value por store_sk y product_sk
    INSERT INTO dwh_metrics.stock_value_history (
        calculated_at, store_sk, product_sk, stock_quantity, stock_value
    )
    SELECT
        v_timestamp,
        store_sk,
        product_sk,
        total_stock,
        stock_value
    FROM dwh_cube.cube_store_stock
    WHERE store_sk IS NOT NULL 
      AND product_sk IS NOT NULL
      AND date_id IS NULL;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Stock value: % rows', v_rows_processed;
    
    -- 2. Store Capacity % por store
    INSERT INTO dwh_metrics.store_capacity_history (
        calculated_at, store_sk, total_stock, max_capacity, capacity_pct
    )
    SELECT
        v_timestamp,
        css.store_sk,
        css.total_stock,
        sc.max_capacity,
        css.stock_capacity_pct
    FROM dwh_cube.cube_store_stock css
    JOIN dwh.store_capacity sc ON css.store_sk = sc.store_sk
    WHERE css.store_sk IS NOT NULL 
      AND css.product_sk IS NULL
      AND css.date_id IS NULL;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Store capacity: % rows', v_rows_processed;
    
    -- 3. Profit Margin % por product_sk
    INSERT INTO dwh_metrics.profit_margin_history (
        calculated_at, product_sk, total_revenue, total_cost, profit_margin_pct
    )
    SELECT
        v_timestamp,
        product_sk,
        final_price as total_revenue,
        final_price * 0.6 as total_cost,  -- Estimated cost
        profit_margin_pct
    FROM dwh_cube.cube_bike_shipment
    WHERE product_sk IS NOT NULL
      AND store_sk IS NULL
      AND shipment_date_id IS NULL
      AND customer_sk IS NULL
      AND staff_sk IS NULL;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Profit margin: % rows', v_rows_processed;
    
    -- 4. Avg Shipping Days por store_sk
    INSERT INTO dwh_metrics.shipping_days_history (
        calculated_at, store_sk, total_shipments, avg_shipping_days
    )
    SELECT
        v_timestamp,
        store_sk,
        rows as total_shipments,
        avg_shipping_days
    FROM dwh_cube.cube_bike_shipment
    WHERE store_sk IS NOT NULL
      AND product_sk IS NULL
      AND shipment_date_id IS NULL
      AND customer_sk IS NULL
      AND staff_sk IS NULL;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Shipping days: % rows', v_rows_processed;
    
    -- 5. Avg Ticket global
    INSERT INTO dwh_metrics.avg_ticket_history (
        calculated_at, total_orders, total_revenue, avg_ticket
    )
    SELECT
        v_timestamp,
        rows as total_orders,
        final_price as total_revenue,
        avg_ticket
    FROM dwh_cube.cube_bike_order
    WHERE order_date_id IS NULL
      AND store_sk IS NULL
      AND product_sk IS NULL
      AND customer_sk IS NULL
      AND staff_sk IS NULL;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Avg ticket: % rows', v_rows_processed;
    
    -- 6. Discount Margin por product_sk y store_sk
    INSERT INTO dwh_metrics.discount_margin_history (
        calculated_at, product_sk, store_sk, gross_amount, 
        discounted_amount, discount_margin_pct
    )
    SELECT
        v_timestamp,
        product_sk,
        store_sk,
        gross_amount,
        final_price as discounted_amount,
        discount_margin_pct
    FROM dwh_cube.cube_bike_order
    WHERE product_sk IS NOT NULL
      AND store_sk IS NOT NULL
      AND order_date_id IS NULL
      AND customer_sk IS NULL
      AND staff_sk IS NULL;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Discount margin: % rows', v_rows_processed;
    
    -- Registrar en etl_runs
    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('calculate_metrics', v_timestamp, 'OK', v_rows_processed, 
            'Metrics calculated and saved successfully');
    
    RAISE NOTICE 'Metrics calculation completed at: %', v_timestamp;
END;
$$;


