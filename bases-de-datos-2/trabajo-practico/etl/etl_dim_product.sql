-- etl_dim_product.sql
-- SCD Type 2 implementation for dim_product
CREATE OR REPLACE FUNCTION dwh.etl_dim_product()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE
    wm timestamptz;
    rows_count int := 0;
BEGIN
    SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='dim_product';

    -- Step 1: Get source data that has been updated
    WITH src AS (
        SELECT 
            p.product_id, 
            p.product_name, 
            p.list_price, 
            p.model_year, 
            b.brand_name, 
            c.category_name, 
            p.updated_at
        FROM bike_stores.products p
        JOIN bike_stores.brands b ON p.brand_id = b.brand_id
        JOIN bike_stores.categories c ON p.category_id = c.category_id
        WHERE p.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
    ),
    -- Step 2: Detect changes by comparing with current dimension records
    changes AS (
        SELECT 
            src.*,
            dim.product_sk as old_sk
        FROM src
        LEFT JOIN dwh.dim_product dim 
            ON src.product_id = dim.product_id 
            AND dim.is_current = true
        WHERE 
            -- New record (no match in dimension)
            dim.product_sk IS NULL
            OR
            -- Changed record (attributes differ)
            (
                dim.product_name IS DISTINCT FROM src.product_name OR
                dim.list_price IS DISTINCT FROM src.list_price OR
                dim.model_year IS DISTINCT FROM src.model_year OR
                dim.brand_name IS DISTINCT FROM src.brand_name OR
                dim.category_name IS DISTINCT FROM src.category_name
            )
    ),
    -- Step 3: Expire old records (set is_current = false, valid_to = now())
    expired AS (
        UPDATE dwh.dim_product
        SET 
            is_current = false,
            valid_to = now()
        WHERE product_sk IN (
            SELECT old_sk FROM changes WHERE old_sk IS NOT NULL
        )
        RETURNING 1
    ),
    -- Step 4: Insert new versions
    inserted AS (
        INSERT INTO dwh.dim_product (
            product_id, product_name, list_price, model_year, brand_name, category_name,
            valid_from, valid_to, is_current
        )
        SELECT 
            product_id, product_name, list_price, model_year, brand_name, category_name,
            now() as valid_from,
            null as valid_to,
            true as is_current
        FROM changes
        RETURNING 1
    )
    SELECT count(*) INTO rows_count FROM inserted;

    UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='dim_product';

    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('dim_product', now(), 'OK', rows_count, 'Dim product SCD Type 2 updated');
END;
$$;
