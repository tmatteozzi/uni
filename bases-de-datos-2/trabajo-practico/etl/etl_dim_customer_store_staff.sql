-- etl_dim_customer_store_staff.sql
-- SCD Type 2 implementation for dim_customer
CREATE OR REPLACE FUNCTION dwh.etl_dim_customer()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE 
    wm timestamptz; 
    rows_count int := 0;
    inserted_count int := 0;
    updated_count int := 0;
BEGIN
    SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='dim_customer';

    -- Step 1: Get source data that has been updated
    WITH src AS (
        SELECT 
            c.customer_id, 
            c.first_name, 
            c.last_name,
            coalesce(c.phone,'Unknown') as phone,
            coalesce(c.email,'Unknown') as email,
            coalesce(c.street,'Unknown') as street,
            coalesce(c.zip_code,'Unknown') as zip_code,
            coalesce(c.state,'Unknown') as state,
            c.updated_at
        FROM bike_stores.customers c
        WHERE c.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
    ),
    -- Step 2: Detect changes by comparing with current dimension records
    changes AS (
        SELECT 
            src.*,
            dim.customer_sk as old_sk
        FROM src
        LEFT JOIN dwh.dim_customer dim 
            ON src.customer_id = dim.customer_id 
            AND dim.is_current = true
        WHERE 
            -- New record (no match in dimension)
            dim.customer_sk IS NULL
            OR
            -- Changed record (attributes differ)
            (
                dim.first_name IS DISTINCT FROM src.first_name OR
                dim.last_name IS DISTINCT FROM src.last_name OR
                dim.phone IS DISTINCT FROM src.phone OR
                dim.email IS DISTINCT FROM src.email OR
                dim.street IS DISTINCT FROM src.street OR
                dim.zip_code IS DISTINCT FROM src.zip_code OR
                dim.state IS DISTINCT FROM src.state
            )
    ),
    -- Step 3: Expire old records (set is_current = false, valid_to = now())
    expired AS (
        UPDATE dwh.dim_customer
        SET 
            is_current = false,
            valid_to = now()
        WHERE customer_sk IN (
            SELECT old_sk FROM changes WHERE old_sk IS NOT NULL
        )
        RETURNING 1
    ),
    -- Step 4: Insert new versions
    inserted AS (
        INSERT INTO dwh.dim_customer (
            customer_id, first_name, last_name, phone, email, street, zip_code, state,
            valid_from, valid_to, is_current
        )
        SELECT 
            customer_id, first_name, last_name, phone, email, street, zip_code, state,
            now() as valid_from,
            null as valid_to,
            true as is_current
        FROM changes
        RETURNING 1
    )
    SELECT count(*) INTO rows_count FROM inserted;

    UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='dim_customer';
    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('dim_customer', now(), 'OK', rows_count, 'Dim customer SCD Type 2 updated');
END;
$$;


-- SCD Type 2 implementation for dim_store
CREATE OR REPLACE FUNCTION dwh.etl_dim_store()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE 
    wm timestamptz; 
    rows_count int := 0;
BEGIN
    SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='dim_store';

    -- Step 1: Get source data that has been updated
    WITH src AS (
        SELECT 
            s.store_id, 
            s.store_name, 
            coalesce(s.phone,'Unknown') as phone, 
            coalesce(s.email,'Unknown') as email, 
            s.street, 
            s.zip_code, 
            s.city, 
            s.updated_at
        FROM bike_stores.stores s
        WHERE s.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
    ),
    -- Step 2: Detect changes by comparing with current dimension records
    changes AS (
        SELECT 
            src.*,
            dim.store_sk as old_sk
        FROM src
        LEFT JOIN dwh.dim_store dim 
            ON src.store_id = dim.store_id 
            AND dim.is_current = true
        WHERE 
            -- New record (no match in dimension)
            dim.store_sk IS NULL
            OR
            -- Changed record (attributes differ)
            (
                dim.store_name IS DISTINCT FROM src.store_name OR
                dim.phone IS DISTINCT FROM src.phone OR
                dim.email IS DISTINCT FROM src.email OR
                dim.street IS DISTINCT FROM src.street OR
                dim.zip_code IS DISTINCT FROM src.zip_code OR
                dim.city IS DISTINCT FROM src.city
            )
    ),
    -- Step 3: Expire old records (set is_current = false, valid_to = now())
    expired AS (
        UPDATE dwh.dim_store
        SET 
            is_current = false,
            valid_to = now()
        WHERE store_sk IN (
            SELECT old_sk FROM changes WHERE old_sk IS NOT NULL
        )
        RETURNING 1
    ),
    -- Step 4: Insert new versions
    inserted AS (
        INSERT INTO dwh.dim_store (
            store_id, store_name, phone, email, street, zip_code, city,
            valid_from, valid_to, is_current
        )
        SELECT 
            store_id, store_name, phone, email, street, zip_code, city,
            now() as valid_from,
            null as valid_to,
            true as is_current
        FROM changes
        RETURNING 1
    )
    SELECT count(*) INTO rows_count FROM inserted;

    UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='dim_store';
    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('dim_store', now(), 'OK', rows_count, 'Dim store SCD Type 2 updated');
END;
$$;


-- SCD Type 2 implementation for dim_staff
CREATE OR REPLACE FUNCTION dwh.etl_dim_staff()
RETURNS void LANGUAGE plpgsql AS $$
DECLARE 
    wm timestamptz; 
    rows_count int := 0;
BEGIN
    SELECT load_watermark INTO wm FROM etl.etl_watermarks WHERE job='dim_staff';

    -- Step 1: Get source data that has been updated
    WITH src AS (
        SELECT 
            s.staff_id, 
            s.first_name, 
            s.last_name, 
            coalesce(s.phone,'Unknown') as phone, 
            coalesce(s.email,'Unknown') as email, 
            s.active, 
            s.manager_id,
            s2.first_name as manager_first_name,
            s2.last_name as manager_last_name,
            s.updated_at
        FROM bike_stores.staffs s
        LEFT JOIN bike_stores.staffs s2 ON s.manager_id = s2.staff_id
        WHERE s.updated_at > coalesce(wm, '1970-01-01'::timestamptz)
    ),
    -- Step 2: Detect changes by comparing with current dimension records
    changes AS (
        SELECT 
            src.*,
            dim.staff_sk as old_sk
        FROM src
        LEFT JOIN dwh.dim_staff dim 
            ON src.staff_id = dim.staff_id 
            AND dim.is_current = true
        WHERE 
            -- New record (no match in dimension)
            dim.staff_sk IS NULL
            OR
            -- Changed record (attributes differ)
            (
                dim.first_name IS DISTINCT FROM src.first_name OR
                dim.last_name IS DISTINCT FROM src.last_name OR
                dim.phone IS DISTINCT FROM src.phone OR
                dim.email IS DISTINCT FROM src.email OR
                dim.active IS DISTINCT FROM src.active OR
                dim.manager_id IS DISTINCT FROM src.manager_id OR
                dim.manager_first_name IS DISTINCT FROM src.manager_first_name OR
                dim.manager_last_name IS DISTINCT FROM src.manager_last_name
            )
    ),
    -- Step 3: Expire old records (set is_current = false, valid_to = now())
    expired AS (
        UPDATE dwh.dim_staff
        SET 
            is_current = false,
            valid_to = now()
        WHERE staff_sk IN (
            SELECT old_sk FROM changes WHERE old_sk IS NOT NULL
        )
        RETURNING 1
    ),
    -- Step 4: Insert new versions
    inserted AS (
        INSERT INTO dwh.dim_staff (
            staff_id, first_name, last_name, phone, email, active, manager_id,
            manager_first_name, manager_last_name,
            valid_from, valid_to, is_current
        )
        SELECT 
            staff_id, first_name, last_name, phone, email, active, manager_id,
            manager_first_name, manager_last_name,
            now() as valid_from,
            null as valid_to,
            true as is_current
        FROM changes
        RETURNING 1
    )
    SELECT count(*) INTO rows_count FROM inserted;

    UPDATE etl.etl_watermarks SET load_watermark = now() WHERE job='dim_staff';
    INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
    VALUES ('dim_staff', now(), 'OK', rows_count, 'Dim staff SCD Type 2 updated');
END;
$$;
