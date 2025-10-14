-- etl_create_metadata.sql
create schema if not exists etl;

-- table to store watermarks per job
create table if not exists etl.etl_watermarks (
                                                  job text primary key,
                                                  load_watermark timestamptz not null
);

-- initialize common jobs with far past watermark if not exists
insert into etl.etl_watermarks(job, load_watermark)
values
    ('dim_product', '1970-01-01'::timestamptz),
    ('dim_customer', '1970-01-01'::timestamptz),
    ('dim_store', '1970-01-01'::timestamptz),
    ('dim_staff', '1970-01-01'::timestamptz),
    ('fact_orders', '1970-01-01'::timestamptz),
    ('fact_shipments', '1970-01-01'::timestamptz),
    ('fact_store_stock', '1970-01-01'::timestamptz)
    ON CONFLICT (job) DO NOTHING;

-- table to log ETL runs
create table if not exists etl.etl_runs (
                                            id serial primary key,
                                            job text not null,
                                            started_at timestamptz not null default now(),
    finished_at timestamptz,
    status text,
    rows_processed int,
    message text
    );
