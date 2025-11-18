-- Conectarse explícitamente a la base creada por POSTGRES_DB
\connect bike_stores;

\echo '>> [OLTP] creando tablas esquema bike_stores...'
\i /sql/bikeStores/create_bike_stores_tables.sql

\echo '>> [OLTP] cargando datos...'
\i /sql/bikeStores/load_bike_stores_data.sql

\echo '>> [dwh] creando dimensiones...'
\i /sql/dwh/create_dimensions.sql

\echo '>> [dwh] creando hechos...'
\i /sql/dwh/create_facts.sql

\echo '>> [dwh] creando cubos...'
\i /sql/dwh/create_cubes.sql

\echo '>> [dwh] creando vistas históricas...'
\i /sql/dwh/create_views_historicas.sql

\echo '>> [dwh] creando tablas de métricas históricas...'
\i /sql/dwh/create_metrics_tables.sql

\echo '>> [etl] creando etl...'
\i /sql/etl/etl_create_metadata.sql
\i /sql/etl/etl_add_updated_triggers.sql
\i /sql/etl/etl_dim_product.sql
\i /sql/etl/etl_dim_customer_store_staff.sql
\i /sql/etl/etl_fact_orders.sql
\i /sql/etl/etl_refresh_cubes.sql
\i /sql/etl/etl_calculate_metrics.sql
\i /sql/etl/etl_composite.sql


\echo '>> Inicialización completada.'

