-- etl_composite.sql
CREATE OR REPLACE FUNCTION dwh.run_full_etl()
RETURNS void LANGUAGE plpgsql AS $$
BEGIN
  -- dimensions
  PERFORM dwh.etl_dim_product();
  PERFORM dwh.etl_dim_customer();
  PERFORM dwh.etl_dim_store();
  PERFORM dwh.etl_dim_staff();

  -- facts
  PERFORM dwh.etl_fact_orders();
  PERFORM dwh.etl_fact_shipments();
  PERFORM dwh.etl_fact_store_stock();

  -- refresh cubes
  PERFORM dwh.etl_refresh_cubes();
END;
$$;
