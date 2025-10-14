-- etl_refresh_cubes.sql
CREATE OR REPLACE FUNCTION dwh.etl_refresh_cubes()
RETURNS void LANGUAGE plpgsql AS $$
BEGIN
  -- try concurrent refresh (requires unique index and no locks)
  PERFORM 1;
BEGIN
EXECUTE 'REFRESH MATERIALIZED VIEW CONCURRENTLY dwh_cube.cube_bike_order';
EXCEPTION WHEN others THEN
    RAISE NOTICE 'Concurrent refresh cube_bike_order failed, trying non-concurrent... %', SQLERRM;
EXECUTE 'REFRESH MATERIALIZED VIEW dwh_cube.cube_bike_order';
END;

BEGIN
EXECUTE 'REFRESH MATERIALIZED VIEW CONCURRENTLY dwh_cube.cube_bike_shipment';
EXCEPTION WHEN others THEN
    RAISE NOTICE 'Concurrent refresh cube_bike_shipment failed, trying non-concurrent... %', SQLERRM;
EXECUTE 'REFRESH MATERIALIZED VIEW dwh_cube.cube_bike_shipment';
END;

BEGIN
EXECUTE 'REFRESH MATERIALIZED VIEW CONCURRENTLY dwh_cube.cube_store_stock';
EXCEPTION WHEN others THEN
    RAISE NOTICE 'Concurrent refresh cube_store_stock failed, trying non-concurrent... %', SQLERRM;
EXECUTE 'REFRESH MATERIALIZED VIEW dwh_cube.cube_store_stock';
END;

INSERT INTO etl.etl_runs(job, finished_at, status, rows_processed, message)
VALUES ('refresh_cubes', now(), 'OK', 0, 'Cubes refreshed');
END;
$$;
