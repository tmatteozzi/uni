-- create_cubes.sql (PostgreSQL 16 compatible)
-- =========================================================
-- Cubos OLAP con vistas materializadas usando GROUP BY CUBE
-- y "gid" armado manualmente con GROUPING(...) por columna.
-- =========================================================

create schema if not exists dwh_cube;

-- =========================================================
-- CUBO 1: Ventas / Pedidos (fact_bike_order)
-- Ejes: order_date_id, store_id, product_id, customer_id, staff_id
-- Métricas: filas, cantidad, monto_bruto, monto_neto, descuento_promedio
-- =========================================================

drop materialized view if exists dwh_cube.cube_bike_order cascade;

create materialized view dwh_cube.cube_bike_order as
select
    fo.order_date_id,
    fo.store_id,
    fo.product_id,
    fo.customer_id,
    fo.staff_id,
    -- gid como bitmask (5 bits)
    (
        (grouping(fo.order_date_id)::int << 4) |
        (grouping(fo.store_id)::int      << 3) |
        (grouping(fo.product_id)::int    << 2) |
        (grouping(fo.customer_id)::int   << 1) |
         grouping(fo.staff_id)::int
    ) as gid,
    count(*)                        as filas,
    sum(fo.quantity)                as cantidad,
    sum(fo.order_amount)            as monto_bruto,
    sum(fo.discounted_order_amount) as monto_neto,
    avg(fo.discount)                as descuento_promedio
from dwh.fact_bike_order fo
group by cube (
    fo.order_date_id,
    fo.store_id,
    fo.product_id,
    fo.customer_id,
    fo.staff_id
    );

-- Índice único para refresh concurrente
create unique index if not exists ixu_cube_bike_order_gid_ejes
    on dwh_cube.cube_bike_order (
    gid, order_date_id, store_id, product_id, customer_id, staff_id
    );

-- Índices auxiliares
create index if not exists ix_cube_bike_order_fecha
    on dwh_cube.cube_bike_order (order_date_id);

create index if not exists ix_cube_bike_order_store_product
    on dwh_cube.cube_bike_order (store_id, product_id);

-- =========================================================
-- CUBO 2: Envíos (fact_bike_shipment)
-- Ejes: shipment_date_id, store_id, product_id, customer_id, staff_id
-- =========================================================

drop materialized view if exists dwh_cube.cube_bike_shipment cascade;

create materialized view dwh_cube.cube_bike_shipment as
select
    fs.shipment_date_id,
    fs.store_id,
    fs.product_id,
    fs.customer_id,
    fs.staff_id,
    (
        (grouping(fs.shipment_date_id)::int << 4) |
        (grouping(fs.store_id)::int         << 3) |
        (grouping(fs.product_id)::int       << 2) |
        (grouping(fs.customer_id)::int      << 1) |
         grouping(fs.staff_id)::int
    ) as gid,
    count(*)                             as filas,
    sum(fs.quantity)                     as cantidad,
    sum(fs.shipment_amount)              as monto_bruto,
    sum(fs.discounted_shipment_amount)   as monto_neto,
    avg(fs.discount)                     as descuento_promedio
from dwh.fact_bike_shipment fs
group by cube (
    fs.shipment_date_id,
    fs.store_id,
    fs.product_id,
    fs.customer_id,
    fs.staff_id
    );

create unique index if not exists ixu_cube_bike_shipment_gid_ejes
    on dwh_cube.cube_bike_shipment (
    gid, shipment_date_id, store_id, product_id, customer_id, staff_id
    );

create index if not exists ix_cube_bike_shipment_fecha
    on dwh_cube.cube_bike_shipment (shipment_date_id);

create index if not exists ix_cube_bike_shipment_store_product
    on dwh_cube.cube_bike_shipment (store_id, product_id);

-- =========================================================
-- CUBO 3: Stock de Tiendas (fact_store_stock)
-- Ejes: date_id, store_id, product_id
-- =========================================================

drop materialized view if exists dwh_cube.cube_store_stock cascade;

create materialized view dwh_cube.cube_store_stock as
select
    fss.date_id,
    fss.store_id,
    fss.product_id,
    (
        (grouping(fss.date_id)::int   << 2) |
        (grouping(fss.store_id)::int  << 1) |
         grouping(fss.product_id)::int
    ) as gid,
    count(*)          as filas,
    sum(fss.quantity) as stock_total
from dwh.fact_store_stock fss
group by cube (
    fss.date_id,
    fss.store_id,
    fss.product_id
    );

create unique index if not exists ixu_cube_store_stock_gid_ejes
    on dwh_cube.cube_store_stock (
    gid, date_id, store_id, product_id
    );

create index if not exists ix_cube_store_stock_fecha
    on dwh_cube.cube_store_stock (date_id);

create index if not exists ix_cube_store_stock_store_product
    on dwh_cube.cube_store_stock (store_id, product_id);

-- =========================================================
-- REFRESH inicial
-- =========================================================
refresh materialized view dwh_cube.cube_bike_order;
refresh materialized view dwh_cube.cube_bike_shipment;
refresh materialized view dwh_cube.cube_store_stock;

