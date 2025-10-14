-- etl_dim_product.sql
CREATE OR REPLACE FUNCTION dwh.etl_dim_product()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE
wm timestamptz;
  rows_count int := 0;
BEGIN
SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='dim_product';

WITH src AS (
    SELECT p.product_id, p.product_name, p.list_price, p.model_year, b.brand_name, c.category_name, p.updated_at
    FROM bike_stores.products p
             JOIN bike_stores.brands b ON p.brand_id = b.brand_id
             JOIN bike_stores.categories c ON p.category_id = c.category_id
    WHERE p.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
), upsert AS (
INSERT INTO dwh.dim_product(product_id, product_name, list_price, model_year, brand_name, category_name)
SELECT product_id, product_name, list_price, model_year, brand_name, category_name FROM src
    ON CONFLICT (product_id) DO UPDATE
                                    SET product_name = EXCLUDED.product_name,
                                    list_price = EXCLUDED.list_price,
                                    model_year = EXCLUDED.model_year,
                                    brand_name = EXCLUDED.brand_name,
                                    category_name = EXCLUDED.category_name
                                    RETURNING 1
                                    )
SELECT count(*) INTO rows_count FROM upsert;

UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='dim_product';

INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
VALUES ('dim_product', now(), 'OK', rows_count, 'Dim product updated');
END;
$$;
