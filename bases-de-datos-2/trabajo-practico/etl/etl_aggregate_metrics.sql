-- etl_aggregate_metrics.sql
-- =========================================================
-- Funciones para calcular métricas agregadas incrementalmente
-- =========================================================

-- Función principal que actualiza todas las métricas agregadas
CREATE OR REPLACE FUNCTION dwh.etl_update_aggregated_metrics()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE
    v_watermark timestamptz;
    v_rows_processed int := 0;
BEGIN
    -- Obtener watermark actual
    SELECT load_watermark INTO v_watermark 
    FROM etl.etl_watermarks 
    WHERE job = 'aggregated_metrics';
    
    RAISE NOTICE 'Starting aggregated metrics calculation from watermark: %', v_watermark;
    
    -- 1. Actualizar métricas del cubo Orders (fact_bike_order)
    INSERT INTO dwh_metrics.aggregated_metrics (
        cube_name, metric_name, order_date_id, store_sk, product_sk, 
        customer_sk, staff_sk, gid,
        rows, quantity, gross_amount, final_price, 
        avg_discount, avg_ticket, discount_margin_pct,
        last_updated, calculation_count
    )
    SELECT
        'cube_bike_order' as cube_name,
        'order_metrics' as metric_name,
        fo.order_date_id,
        fo.store_sk,
        fo.product_sk,
        fo.customer_sk,
        fo.staff_sk,
        (
            (grouping(fo.order_date_id)::int << 4) |
            (grouping(fo.store_sk)::int      << 3) |
            (grouping(fo.product_sk)::int    << 2) |
            (grouping(fo.customer_sk)::int   << 1) |
             grouping(fo.staff_sk)::int
        ) as gid,
        count(*)::bigint as rows,
        sum(fo.quantity)::bigint as quantity,
        sum(fo.order_amount) as gross_amount,
        sum(fo.discounted_order_amount) as final_price,
        avg(fo.discount) as avg_discount,
        CASE 
            WHEN count(distinct fo.order_id) > 0 
            THEN sum(fo.discounted_order_amount) / count(distinct fo.order_id)
            ELSE 0 
        END as avg_ticket,
        CASE 
            WHEN sum(fo.order_amount) > 0 
            THEN ((sum(fo.order_amount) - sum(fo.discounted_order_amount)) / sum(fo.order_amount)) * 100
            ELSE 0 
        END as discount_margin_pct,
        NOW() as last_updated,
        1 as calculation_count
    FROM dwh.fact_bike_order fo
    GROUP BY CUBE (
        fo.order_date_id,
        fo.store_sk,
        fo.product_sk,
        fo.customer_sk,
        fo.staff_sk
    )
    ON CONFLICT (cube_name, 
                 COALESCE(order_date_id, -1),
                 COALESCE(shipment_date_id, -1),
                 COALESCE(date_id, -1),
                 COALESCE(store_sk, -1),
                 COALESCE(product_sk, -1),
                 COALESCE(customer_sk, -1),
                 COALESCE(staff_sk, -1),
                 gid)
    DO UPDATE SET
        rows = dwh_metrics.aggregated_metrics.rows + EXCLUDED.rows,
        quantity = dwh_metrics.aggregated_metrics.quantity + EXCLUDED.quantity,
        gross_amount = dwh_metrics.aggregated_metrics.gross_amount + EXCLUDED.gross_amount,
        final_price = dwh_metrics.aggregated_metrics.final_price + EXCLUDED.final_price,
        -- Para promedios, recalculamos con los nuevos valores
        avg_discount = (dwh_metrics.aggregated_metrics.avg_discount * dwh_metrics.aggregated_metrics.calculation_count + 
                       EXCLUDED.avg_discount) / (dwh_metrics.aggregated_metrics.calculation_count + 1),
        avg_ticket = (dwh_metrics.aggregated_metrics.avg_ticket * dwh_metrics.aggregated_metrics.calculation_count + 
                     EXCLUDED.avg_ticket) / (dwh_metrics.aggregated_metrics.calculation_count + 1),
        discount_margin_pct = (dwh_metrics.aggregated_metrics.discount_margin_pct * dwh_metrics.aggregated_metrics.calculation_count + 
                              EXCLUDED.discount_margin_pct) / (dwh_metrics.aggregated_metrics.calculation_count + 1),
        last_updated = NOW(),
        calculation_count = dwh_metrics.aggregated_metrics.calculation_count + 1;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Updated % rows for cube_bike_order', v_rows_processed;
    
    -- 2. Actualizar métricas del cubo Shipments (fact_bike_shipment)
    INSERT INTO dwh_metrics.aggregated_metrics (
        cube_name, metric_name, shipment_date_id, store_sk, product_sk, 
        customer_sk, staff_sk, gid,
        rows, quantity, gross_amount, final_price, 
        avg_discount, profit_margin_pct, avg_shipping_days,
        last_updated, calculation_count
    )
    SELECT
        'cube_bike_shipment' as cube_name,
        'shipment_metrics' as metric_name,
        fs.shipment_date_id,
        fs.store_sk,
        fs.product_sk,
        fs.customer_sk,
        fs.staff_sk,
        (
            (grouping(fs.shipment_date_id)::int << 4) |
            (grouping(fs.store_sk)::int         << 3) |
            (grouping(fs.product_sk)::int       << 2) |
            (grouping(fs.customer_sk)::int      << 1) |
             grouping(fs.staff_sk)::int
        ) as gid,
        count(*)::bigint as rows,
        sum(fs.quantity)::bigint as quantity,
        sum(fs.shipment_amount) as gross_amount,
        sum(fs.discounted_shipment_amount) as final_price,
        avg(fs.discount) as avg_discount,
        CASE 
            WHEN sum(fs.discounted_shipment_amount) > 0 
            THEN ((sum(fs.discounted_shipment_amount) - sum(fs.estimated_cost)) / sum(fs.discounted_shipment_amount)) * 100
            ELSE 0 
        END as profit_margin_pct,
        avg(fs.shipping_days) as avg_shipping_days,
        NOW() as last_updated,
        1 as calculation_count
    FROM dwh.fact_bike_shipment fs
    GROUP BY CUBE (
        fs.shipment_date_id,
        fs.store_sk,
        fs.product_sk,
        fs.customer_sk,
        fs.staff_sk
    )
    ON CONFLICT (cube_name, 
                 COALESCE(order_date_id, -1),
                 COALESCE(shipment_date_id, -1),
                 COALESCE(date_id, -1),
                 COALESCE(store_sk, -1),
                 COALESCE(product_sk, -1),
                 COALESCE(customer_sk, -1),
                 COALESCE(staff_sk, -1),
                 gid)
    DO UPDATE SET
        rows = dwh_metrics.aggregated_metrics.rows + EXCLUDED.rows,
        quantity = dwh_metrics.aggregated_metrics.quantity + EXCLUDED.quantity,
        gross_amount = dwh_metrics.aggregated_metrics.gross_amount + EXCLUDED.gross_amount,
        final_price = dwh_metrics.aggregated_metrics.final_price + EXCLUDED.final_price,
        avg_discount = (dwh_metrics.aggregated_metrics.avg_discount * dwh_metrics.aggregated_metrics.calculation_count + 
                       EXCLUDED.avg_discount) / (dwh_metrics.aggregated_metrics.calculation_count + 1),
        profit_margin_pct = (dwh_metrics.aggregated_metrics.profit_margin_pct * dwh_metrics.aggregated_metrics.calculation_count + 
                            EXCLUDED.profit_margin_pct) / (dwh_metrics.aggregated_metrics.calculation_count + 1),
        avg_shipping_days = (dwh_metrics.aggregated_metrics.avg_shipping_days * dwh_metrics.aggregated_metrics.calculation_count + 
                            EXCLUDED.avg_shipping_days) / (dwh_metrics.aggregated_metrics.calculation_count + 1),
        last_updated = NOW(),
        calculation_count = dwh_metrics.aggregated_metrics.calculation_count + 1;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Updated % rows for cube_bike_shipment', v_rows_processed;
    
    -- 3. Actualizar métricas del cubo Stock (fact_store_stock)
    INSERT INTO dwh_metrics.aggregated_metrics (
        cube_name, metric_name, date_id, store_sk, product_sk, gid,
        rows, total_stock, stock_capacity_pct, stock_value,
        last_updated, calculation_count
    )
    SELECT
        'cube_store_stock' as cube_name,
        'stock_metrics' as metric_name,
        fss.date_id,
        fss.store_sk,
        fss.product_sk,
        (
            (grouping(fss.date_id)::int   << 2) |
            (grouping(fss.store_sk)::int  << 1) |
             grouping(fss.product_sk)::int
        ) as gid,
        count(*)::bigint as rows,
        sum(fss.quantity)::bigint as total_stock,
        CASE 
            WHEN grouping(fss.store_sk) = 0 THEN
                CASE 
                    WHEN max(sc.max_capacity) > 0 
                    THEN (sum(fss.quantity)::decimal / max(sc.max_capacity)) * 100
                    ELSE 0 
                END
            ELSE null
        END as stock_capacity_pct,
        sum(fss.quantity * dp.list_price) as stock_value,
        NOW() as last_updated,
        1 as calculation_count
    FROM dwh.fact_store_stock fss
    LEFT JOIN dwh.store_capacity sc 
        ON fss.store_sk = sc.store_sk
    LEFT JOIN dwh.dim_product dp
        ON fss.product_sk = dp.product_sk AND dp.is_current = true
    GROUP BY CUBE (
        fss.date_id,
        fss.store_sk,
        fss.product_sk
    )
    ON CONFLICT (cube_name, 
                 COALESCE(order_date_id, -1),
                 COALESCE(shipment_date_id, -1),
                 COALESCE(date_id, -1),
                 COALESCE(store_sk, -1),
                 COALESCE(product_sk, -1),
                 COALESCE(customer_sk, -1),
                 COALESCE(staff_sk, -1),
                 gid)
    DO UPDATE SET
        rows = dwh_metrics.aggregated_metrics.rows + EXCLUDED.rows,
        total_stock = EXCLUDED.total_stock,  -- Stock se reemplaza, no se acumula
        stock_capacity_pct = EXCLUDED.stock_capacity_pct,  -- Porcentaje se recalcula
        stock_value = EXCLUDED.stock_value,  -- Valor se recalcula
        last_updated = NOW(),
        calculation_count = dwh_metrics.aggregated_metrics.calculation_count + 1;
    
    GET DIAGNOSTICS v_rows_processed = ROW_COUNT;
    RAISE NOTICE 'Updated % rows for cube_store_stock', v_rows_processed;
    
    -- Actualizar watermark
    UPDATE etl.etl_watermarks 
    SET load_watermark = NOW() 
    WHERE job = 'aggregated_metrics';
    
    -- Registrar en etl_runs
    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('aggregated_metrics', NOW(), 'OK', v_rows_processed, 
            'Aggregated metrics updated successfully');
    
    RAISE NOTICE 'Aggregated metrics calculation completed';
END;
$$;


