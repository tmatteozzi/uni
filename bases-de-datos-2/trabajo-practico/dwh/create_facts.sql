-- ===========
-- Order Fact
-- ===========
create table dwh.fact_bike_order as
select
	to_char(o.order_date, 'yyyymmdd')::int     as order_date_id
	,to_char(o.required_date, 'yyyymmdd')::int as requirement_date_id
	,dc.customer_sk
	,ds.staff_sk
	,dst.store_sk
	,dp.product_sk
	,o.order_id
	,oi.quantity
	,oi.list_price
	,oi.discount
	,oi.list_price * oi.quantity                 as order_amount 
	,(oi.list_price - oi.discount) * oi.quantity as discounted_order_amount
from bike_stores.orders o 
join bike_stores.order_items oi
	on o.order_id = oi.order_id
join dwh.dim_customer dc
	on o.customer_id = dc.customer_id and dc.is_current = true
join dwh.dim_staff ds
	on o.staff_id = ds.staff_id and ds.is_current = true
join dwh.dim_store dst
	on o.store_id = dst.store_id and dst.is_current = true
join dwh.dim_product dp
	on oi.product_id = dp.product_id and dp.is_current = true
;

alter table dwh.fact_bike_order 
add constraint f_bike_order_date_fk      
		foreign key (order_date_id) references dwh.dim_date(date_id)
;
alter table dwh.fact_bike_order 
add constraint f_bike_order_requirement_d_date_fk      
		foreign key (requirement_date_id) references dwh.dim_date(date_id)
;
alter table dwh.fact_bike_order 
add constraint f_bike_order_d_customer_fk      
		foreign key (customer_sk) references dwh.dim_customer(customer_sk)
;
alter table dwh.fact_bike_order 
add constraint f_bike_order_d_product_fk      
		foreign key (product_sk) references dwh.dim_product(product_sk)
;
alter table dwh.fact_bike_order 
add constraint f_bike_order_d_staff_fk      
		foreign key (staff_sk) references dwh.dim_staff(staff_sk)
;
alter table dwh.fact_bike_order 
add constraint f_bike_order_d_store_fk      
		foreign key (store_sk) references dwh.dim_store(store_sk)
;

-- Add primary key for fact_bike_order
alter table dwh.fact_bike_order 
add constraint f_bike_order_pk primary key (order_id, product_sk)
;

-- ===============
-- Shipment Fact
-- ===============
create table dwh.fact_bike_shipment as
select
	to_char(o.shipped_date, 'yyyymmdd')::int as shipment_date_id
	,dc.customer_sk
	,ds.staff_sk
	,dst.store_sk
	,dp.product_sk
	,o.order_id
	,oi.quantity
	,oi.list_price
	,oi.discount
	,oi.list_price * oi.quantity                 as shipment_amount 
	,(oi.list_price - oi.discount) * oi.quantity as discounted_shipment_amount
from bike_stores.orders o 
join bike_stores.order_items oi
	on o.order_id = oi.order_id
join dwh.dim_customer dc
	on o.customer_id = dc.customer_id and dc.is_current = true
join dwh.dim_staff ds
	on o.staff_id = ds.staff_id and ds.is_current = true
join dwh.dim_store dst
	on o.store_id = dst.store_id and dst.is_current = true
join dwh.dim_product dp
	on oi.product_id = dp.product_id and dp.is_current = true
where o.shipped_date is not null
;

alter table dwh.fact_bike_shipment 
add constraint f_bike_shipment_d_date_fk      
		foreign key (shipment_date_id) references dwh.dim_date(date_id)
;
alter table dwh.fact_bike_shipment 
add constraint f_bike_shipment_d_customer_fk      
		foreign key (customer_sk) references dwh.dim_customer(customer_sk)
;

alter table dwh.fact_bike_shipment 
add constraint f_bike_shipment_d_product_fk      
		foreign key (product_sk) references dwh.dim_product(product_sk)
;
alter table dwh.fact_bike_shipment 
add constraint f_bike_shipment_d_staff_fk      
		foreign key (staff_sk) references dwh.dim_staff(staff_sk)
;
alter table dwh.fact_bike_shipment 
add constraint f_bike_shipment_d_store_fk      
		foreign key (store_sk) references dwh.dim_store(store_sk)
;

-- Add primary key for fact_bike_shipment
alter table dwh.fact_bike_shipment 
add constraint f_bike_shipment_pk primary key (order_id, product_sk)
;

-- ============================
-- Store Stock Fact
-- ============================
create table dwh.fact_store_stock as 
select 
	to_char('2021-06-23'::date , 'yyyymmdd')::int as date_id
	,dst.store_sk
	,dp.product_sk
	,s.quantity
from bike_stores.stocks s
join dwh.dim_store dst
	on s.store_id = dst.store_id and dst.is_current = true
join dwh.dim_product dp
	on s.product_id = dp.product_id and dp.is_current = true
;

alter table dwh.fact_store_stock
add constraint f_store_stock_d_store_fk
	foreign key (store_sk) references dwh.dim_store(store_sk)
;
alter table dwh.fact_store_stock
add constraint f_store_stock_d_product_fk
	foreign key (product_sk) references dwh.dim_product(product_sk)
;
alter table dwh.fact_store_stock
add constraint f_store_stock_d_date_fk
	foreign key (date_id) references dwh.dim_date(date_id)
;

-- Add primary key for fact_store_stock
alter table dwh.fact_store_stock
add constraint f_store_stock_pk primary key (date_id, store_sk, product_sk)
;
