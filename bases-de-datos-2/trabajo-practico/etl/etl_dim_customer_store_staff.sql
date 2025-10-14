-- etl_dim_customer_store_staff.sql
CREATE OR REPLACE FUNCTION dwh.etl_dim_customer()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE wm timestamptz; rows_count int := 0;
BEGIN
SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='dim_customer';

WITH src AS (
    SELECT c.customer_id, c.first_name, c.last_name,
           coalesce(c.phone,'Unknown') as phone,
           coalesce(c.email,'Unknown') as email,
           coalesce(c.street,'Unknown') as street,
           coalesce(c.zip_code,'Unknown') as zip_code,
           coalesce(c.state,'Unknown') as state,
           c.updated_at
    FROM bike_stores.customers c
    WHERE c.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
), upsert AS (
INSERT INTO dwh.dim_customer (customer_id, first_name, last_name, phone, email, street, zip_code, state)
SELECT customer_id, first_name, last_name, phone, email, street, zip_code, state FROM src
    ON CONFLICT (customer_id) DO UPDATE SET
    first_name = EXCLUDED.first_name,
                                     last_name = EXCLUDED.last_name,
                                     phone = EXCLUDED.phone,
                                     email = EXCLUDED.email,
                                     street = EXCLUDED.street,
                                     zip_code = EXCLUDED.zip_code,
                                     state = EXCLUDED.state
                                     RETURNING 1
                                     )
SELECT count(*) INTO rows_count FROM upsert;

UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='dim_customer';
INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
VALUES ('dim_customer', now(), 'OK', rows_count, 'Dim customer updated');
END;
$$;


CREATE OR REPLACE FUNCTION dwh.etl_dim_store()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE wm timestamptz; rows_count int := 0;
BEGIN
SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='dim_store';

WITH src AS (
    SELECT s.store_id, s.store_name, coalesce(s.phone,'Unknown') phone, coalesce(s.email,'Unknown') email, s.street, s.zip_code, s.city, s.updated_at
    FROM bike_stores.stores s
    WHERE s.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
), upsert AS (
INSERT INTO dwh.dim_store (store_id, store_name, phone, email, street, zip_code, city)
SELECT store_id, store_name, phone, email, street, zip_code, city FROM src
    ON CONFLICT (store_id) DO UPDATE SET
    store_name = EXCLUDED.store_name,
                                  phone = EXCLUDED.phone,
                                  email = EXCLUDED.email,
                                  street = EXCLUDED.street,
                                  zip_code = EXCLUDED.zip_code,
                                  city = EXCLUDED.city
                                  RETURNING 1
                                  )
SELECT count(*) INTO rows_count FROM upsert;

UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='dim_store';
INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
VALUES ('dim_store', now(), 'OK', rows_count, 'Dim store updated');
END;
$$;


CREATE OR REPLACE FUNCTION dwh.etl_dim_staff()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE wm timestamptz; rows_count int := 0;
BEGIN
SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='dim_staff';

WITH src AS (
    SELECT s.staff_id, s.first_name, s.last_name, coalesce(s.phone,'Unknown') phone, coalesce(s.email,'Unknown') email, s.active, s.manager_id, s.updated_at
    FROM bike_stores.staffs s
    WHERE s.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
), upsert AS (
INSERT INTO dwh.dim_staff (staff_id, first_name, last_name, phone, email, active, manager_id)
SELECT staff_id, first_name, last_name, phone, email, active, manager_id FROM src
    ON CONFLICT (staff_id) DO UPDATE SET
    first_name = EXCLUDED.first_name,
                                  last_name = EXCLUDED.last_name,
                                  phone = EXCLUDED.phone,
                                  email = EXCLUDED.email,
                                  active = EXCLUDED.active,
                                  manager_id = EXCLUDED.manager_id
                                  RETURNING 1
                                  )
SELECT count(*) INTO rows_count FROM upsert;

UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='dim_staff';
INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
VALUES ('dim_staff', now(), 'OK', rows_count, 'Dim staff updated');
END;
$$;
