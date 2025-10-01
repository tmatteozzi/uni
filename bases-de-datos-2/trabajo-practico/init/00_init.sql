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

\echo '>> Inicialización completada.'
