-- create_cubes.sql (PostgreSQL 16 compatible)
-- =========================================================
-- OLAP Cubes using materialized views with GROUP BY CUBE
-- and manual "gid" bitmask built using GROUPING(...) per column.
-- =========================================================

create schema if not exists dwh_cube;

-- =========================================================
-- CUBE 1: Sales / Orders (fact_bike_order)
-- Axes: order_date_id, store_sk, product_sk, customer_sk, staff_sk
-- Metrics: rows, quantity, gross_amount, final_price, average_discount
-- =========================================================

drop materialized view if exists dwh_cube.cube_bike_order cascade;

create materialized view dwh_cube.cube_bike_order as
select
    fo.order_date_id,
    fo.store_sk,
    fo.product_sk,
    fo.customer_sk,
    fo.staff_sk,
    -- gid as bitmask (5 bits)
    (
        (grouping(fo.order_date_id)::int << 4) |
        (grouping(fo.store_sk)::int      << 3) |
        (grouping(fo.product_sk)::int    << 2) |
        (grouping(fo.customer_sk)::int   << 1) |
         grouping(fo.staff_sk)::int
    ) as gid,
    count(*)                        as rows,
    sum(fo.quantity)                as quantity,
    sum(fo.order_amount)            as gross_amount,
    sum(fo.discounted_order_amount) as final_price,
    avg(fo.discount)                as average_discount
from dwh.fact_bike_order fo
group by cube (
    fo.order_date_id,
    fo.store_sk,
    fo.product_sk,
    fo.customer_sk,
    fo.staff_sk
    );

-- Unique index for concurrent refresh
create unique index if not exists ixu_cube_bike_order_gid_axes
    on dwh_cube.cube_bike_order (
    gid, order_date_id, store_sk, product_sk, customer_sk, staff_sk
    );

-- Auxiliary indexes
create index if not exists ix_cube_bike_order_date
    on dwh_cube.cube_bike_order (order_date_id);

create index if not exists ix_cube_bike_order_store_product
    on dwh_cube.cube_bike_order (store_sk, product_sk);

-- =========================================================
-- CUBE 2: Shipments (fact_bike_shipment)
-- Axes: shipment_date_id, store_sk, product_sk, customer_sk, staff_sk
-- Metrics: rows, quantity, gross_amount, final_price, average_discount
-- =========================================================

drop materialized view if exists dwh_cube.cube_bike_shipment cascade;

create materialized view dwh_cube.cube_bike_shipment as
select
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
    count(*)                             as rows,
    sum(fs.quantity)                     as quantity,
    sum(fs.shipment_amount)              as gross_amount,
    sum(fs.discounted_shipment_amount)   as final_price,
    avg(fs.discount)                     as average_discount
from dwh.fact_bike_shipment fs
group by cube (
    fs.shipment_date_id,
    fs.store_sk,
    fs.product_sk,
    fs.customer_sk,
    fs.staff_sk
    );

create unique index if not exists ixu_cube_bike_shipment_gid_axes
    on dwh_cube.cube_bike_shipment (
    gid, shipment_date_id, store_sk, product_sk, customer_sk, staff_sk
    );

create index if not exists ix_cube_bike_shipment_date
    on dwh_cube.cube_bike_shipment (shipment_date_id);

create index if not exists ix_cube_bike_shipment_store_product
    on dwh_cube.cube_bike_shipment (store_sk, product_sk);

-- =========================================================
-- CUBE 3: Store Stock (fact_store_stock)
-- Axes: date_id, store_sk, product_sk
-- Metrics: rows, total_stock
-- =========================================================

drop materialized view if exists dwh_cube.cube_store_stock cascade;

create materialized view dwh_cube.cube_store_stock as
select
    fss.date_id,
    fss.store_sk,
    fss.product_sk,
    (
        (grouping(fss.date_id)::int   << 2) |
        (grouping(fss.store_sk)::int  << 1) |
         grouping(fss.product_sk)::int
    ) as gid,
    count(*)          as rows,
    sum(fss.quantity) as total_stock
from dwh.fact_store_stock fss
group by cube (
    fss.date_id,
    fss.store_sk,
    fss.product_sk
    );

create unique index if not exists ixu_cube_store_stock_gid_axes
    on dwh_cube.cube_store_stock (
    gid, date_id, store_sk, product_sk
    );

create index if not exists ix_cube_store_stock_date
    on dwh_cube.cube_store_stock (date_id);

create index if not exists ix_cube_store_stock_store_product
    on dwh_cube.cube_store_stock (store_sk, product_sk);

-- =========================================================
-- Initial REFRESH
-- =========================================================
refresh materialized view dwh_cube.cube_bike_order;
refresh materialized view dwh_cube.cube_bike_shipment;
refresh materialized view dwh_cube.cube_store_stock;
