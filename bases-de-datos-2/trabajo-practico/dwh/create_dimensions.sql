
drop schema if exists dwh cascade;
create schema dwh;

-- ==================
-- Product Dimension
-- ==================
create sequence if not exists dwh.dim_product_sk_seq;

create table dwh.dim_product (
	product_sk bigint default nextval('dwh.dim_product_sk_seq') primary key
	,product_id int not null
	,product_name varchar(255) not null
	,list_price decimal(10,2) not null
	,model_year int not null
	,brand_name varchar(255) not null
	,category_name varchar(255) not null
	,valid_from timestamptz not null default now()
	,valid_to timestamptz
	,is_current boolean not null default true
);

insert into dwh.dim_product (product_id, product_name, list_price, model_year, brand_name, category_name)
select 
	p.product_id
	,p.product_name 
	,p.list_price
	,p.model_year
	,b.brand_name
	,c.category_name
from bike_stores.products p
join bike_stores.brands b
	on p.brand_id = b.brand_id 
join bike_stores.categories c
	on p.category_id = c.category_id 
;

create index idx_dim_product_id_current on dwh.dim_product(product_id, is_current);

-- ===================
-- Customer Dimension
-- ===================
create sequence if not exists dwh.dim_customer_sk_seq;

create table dwh.dim_customer (
	customer_sk bigint default nextval('dwh.dim_customer_sk_seq') primary key
	,customer_id int not null
	,first_name varchar(255) not null
	,last_name varchar(255) not null
	,phone varchar(25)
	,email varchar(255)
	,street varchar(255)
	,zip_code varchar(5)
	,state varchar(25)
	,valid_from timestamptz not null default now()
	,valid_to timestamptz
	,is_current boolean not null default true
);

insert into dwh.dim_customer (customer_id, first_name, last_name, phone, email, street, zip_code, state)
select
	c.customer_id
	,c.first_name
	,c.last_name
	,coalesce (c.phone, 'Unknown')    as phone
	,coalesce (c.email, 'Unknown')    as email
	,coalesce (c.street, 'Unknown')   as street
	,coalesce (c.zip_code, 'Unknown') as zip_code
	,coalesce (c.state, 'Unknown')    as state
from bike_stores.customers c
;

create index idx_dim_customer_id_current on dwh.dim_customer(customer_id, is_current);

-- ================
-- Store Dimension
-- ================
create sequence if not exists dwh.dim_store_sk_seq;

create table dwh.dim_store (
	store_sk bigint default nextval('dwh.dim_store_sk_seq') primary key
	,store_id int not null
	,store_name varchar(255) not null
	,phone varchar(25)
	,email varchar(255)
	,street varchar(255)
	,zip_code varchar(5)
	,city varchar(255)
	,valid_from timestamptz not null default now()
	,valid_to timestamptz
	,is_current boolean not null default true
);

insert into dwh.dim_store (store_id, store_name, phone, email, street, zip_code, city)
select 
	s.store_id 
	,s.store_name
	,coalesce (s.phone, 'Unknown') as phone
	,coalesce (s.email, 'Unknown') as email
	,s.street
	,s.zip_code
	,s.city
from bike_stores.stores s
;

create index idx_dim_store_id_current on dwh.dim_store(store_id, is_current);

-- ================
-- Staff Dimension
-- ================
create sequence if not exists dwh.dim_staff_sk_seq;

create table dwh.dim_staff (
	staff_sk bigint default nextval('dwh.dim_staff_sk_seq') primary key
	,staff_id int not null
	,first_name varchar(50) not null
	,last_name varchar(50) not null
	,phone varchar(25)
	,email varchar(255)
	,active int not null
	,manager_id int
	,manager_first_name varchar(50)
	,manager_last_name varchar(50)
	,valid_from timestamptz not null default now()
	,valid_to timestamptz
	,is_current boolean not null default true
);

insert into dwh.dim_staff (staff_id, first_name, last_name, phone, email, active, manager_id, manager_first_name, manager_last_name)
select 
	s.staff_id
	,s.first_name
	,s.last_name
	,coalesce (s.phone, 'Unknown') as phone
	,coalesce (s.email, 'Unknown') as email
	,s.active
	,s.manager_id
	,s2.first_name as manager_first_name
	,s2.last_name  as manager_last_name
from bike_stores.staffs s
left join bike_stores.staffs s2
	on s.manager_id = s2.staff_id
;

create index idx_dim_staff_id_current on dwh.dim_staff(staff_id, is_current);

-- ==========================
-- Staff Hierarchy Bridge
-- ==========================
-- Note: This bridge table uses business keys (staff_id) for the hierarchy logic
-- but will be joined to current staff records via dim_staff
create table dwh.staff_hierarchy as
with recursive sh(staff_id, subordinate_id, hierarchy_depth) as
(
	select
		staff_id
		,staff_id as subordinate_id
		,0        as hierarchy_depth
	from bike_stores.staffs
	union all
	select 
		sh.staff_id              as staff_id
		,s.staff_id              as subordinate_id
		,sh.hierarchy_depth + 1  as hierarchy_depth
	from sh
	join bike_stores.staffs s
		on sh.subordinate_id = s.manager_id
)
select 
	staff_id
	,subordinate_id
	,hierarchy_depth
from sh
;

-- Create indexes for performance
create index idx_staff_hierarchy_staff on dwh.staff_hierarchy(staff_id);
create index idx_staff_hierarchy_subordinate on dwh.staff_hierarchy(subordinate_id);

-- ===============
-- Date Dimension
-- ===============
create table dwh.dim_date (
	date_id        int     not null
	,date          date    not null
	,day_name      text    not null
	,day_of_month  int     not null
	,week_of_month int     not null
	,week_of_year  int     not null
	,month         int     not null
	,month_name    text    not null
	,quarter       int     not null
	,year          int     not null
	,is_weekend    boolean not null
);

insert into dwh.dim_date
select 
	to_char(d, 'yyyymmdd')::int as date_id
	,d                         as date
	,to_char(d, 'FMDay')       as day_name
	,extract(day from d)       as day_of_month
	,to_char(d, 'W')::int      as week_of_month
	,extract(week from d)      as week_of_year
	,extract(month from d)     as month
	,to_char(d, 'FMMonth')     as month_name
	,extract(quarter from d)   as quarter 
	,extract(year from d)      as year
	,case 
		when extract(isodow from d) in (6, 7) then true
		else false
	end as is_weekend
from (select '2016-01-01'::date + sequence.day as d
	  from generate_series(0, 2000) as sequence(day)
	 ) date_seq
order by 1;

alter table dwh.dim_date
add constraint dim_date_pk primary key (date_id)
;
alter table dwh.dim_date
add constraint dim_date_date_u unique(date)
;

-- ===========================
-- Store Capacity Configuration
-- ===========================
-- This table defines the storage capacity for each store
create table dwh.store_capacity (
	store_sk bigint not null
	,max_capacity int not null default 10000
	,constraint store_capacity_pk primary key (store_sk)
	,constraint store_capacity_store_fk foreign key (store_sk) references dwh.dim_store(store_sk)
);

-- Insert default capacities for each store
insert into dwh.store_capacity (store_sk, max_capacity)
select 
	store_sk,
	case 
		when store_id = 1 then 15000  -- Santa Cruz Bikes
		when store_id = 2 then 12000  -- Baldwin Bikes
		when store_id = 3 then 10000  -- Rowlett Bikes
		else 10000
	end as max_capacity
from dwh.dim_store
where is_current = true
;