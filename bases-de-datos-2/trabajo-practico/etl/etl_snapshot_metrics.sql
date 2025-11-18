-- etl_snapshot_metrics.sql
-- =========================================================
-- Función para guardar snapshots de métricas desde los cubos
-- Guarda el estado actual de todas las métricas con timestamp
-- =========================================================

CREATE OR REPLACE FUNCTION dwh.etl_snapshot_metrics()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE
    v_rows_processed int := 0;
    v_snapshot_date timestamptz;
BEGIN
    v_snapshot_date := NOW();
    
    RAISE NOTICE 'Creating metrics snapshot at: %', v_snapshot_date;
    
    -- 1. Guardar snapshot del cubo Orders
    INSERT INTO dwh_metrics.metrics_snapshots (
        snapshot_date, cube_name, order_date_id, store_sk, product_sk, 
        customer_sk, staff_sk, gid,
        rows, quantity, gross_amount, final_price, 
        avg_discount, avg_ticket, discount_margin_pct
    )
    SELECT
        v_snapshot_date,
        'cube_bike_order',
        order_date_id,
        store_sk,
        product_sk,
        customer_sk,
        staff_sk,
        gid,
        rows,
        quantity,
        gross_amount,
        final_price,
        average_discount,
        avg_ticket,
        discount_margin_pct
    FROM dwh_cube.cube_bike_order;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Saved % rows from cube_bike_order', v_rows_processed;
    
    -- 2. Guardar snapshot del cubo Shipments
    INSERT INTO dwh_metrics.metrics_snapshots (
        snapshot_date, cube_name, shipment_date_id, store_sk, product_sk, 
        customer_sk, staff_sk, gid,
        rows, quantity, gross_amount, final_price, 
        avg_discount, profit_margin_pct, avg_shipping_days
    )
    SELECT
        v_snapshot_date,
        'cube_bike_shipment',
        shipment_date_id,
        store_sk,
        product_sk,
        customer_sk,
        staff_sk,
        gid,
        rows,
        quantity,
        gross_amount,
        final_price,
        average_discount,
        profit_margin_pct,
        avg_shipping_days
    FROM dwh_cube.cube_bike_shipment;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Saved % rows from cube_bike_shipment', v_rows_processed;
    
    -- 3. Guardar snapshot del cubo Stock
    INSERT INTO dwh_metrics.metrics_snapshots (
        snapshot_date, cube_name, date_id, store_sk, product_sk, gid,
        rows, total_stock, stock_capacity_pct, stock_value
    )
    SELECT
        v_snapshot_date,
        'cube_store_stock',
        date_id,
        store_sk,
        product_sk,
        gid,
        rows,
        total_stock,
        stock_capacity_pct,
        stock_value
    FROM dwh_cube.cube_store_stock;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Saved % rows from cube_store_stock', v_rows_processed;
    
    -- Registrar en etl_runs
    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('snapshot_metrics', v_snapshot_date, 'OK', v_rows_processed, 
            'Metrics snapshot created successfully');
    
    RAISE NOTICE 'Metrics snapshot completed at: %', v_snapshot_date;
END;
$$;

COMMENT ON FUNCTION dwh.etl_snapshot_metrics() IS 
'Crea un snapshot histórico de todas las métricas desde los cubos OLAP. Cada ejecución guarda el estado actual de las métricas con timestamp para análisis temporal.';

