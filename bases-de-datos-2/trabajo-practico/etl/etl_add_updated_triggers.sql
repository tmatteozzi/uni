-- etl_add_updated_triggers.sql

-- trigger function to set updated_at
CREATE OR REPLACE FUNCTION bike_stores.set_updated_at()
RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
  NEW.updated_at = now();
RETURN NEW;
END;
$$;

-- helper to add columns if not exist
DO $$
BEGIN
  -- products
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='products' AND column_name='created_at'
  ) THEN
ALTER TABLE bike_stores.products
    ADD COLUMN created_at timestamptz DEFAULT now();
END IF;
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='products' AND column_name='updated_at'
  ) THEN
ALTER TABLE bike_stores.products
    ADD COLUMN updated_at timestamptz DEFAULT now();
END IF;

  -- orders
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='orders' AND column_name='created_at'
  ) THEN
ALTER TABLE bike_stores.orders
    ADD COLUMN created_at timestamptz DEFAULT now();
END IF;
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='orders' AND column_name='updated_at'
  ) THEN
ALTER TABLE bike_stores.orders
    ADD COLUMN updated_at timestamptz DEFAULT now();
END IF;

  -- order_items
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='order_items' AND column_name='created_at'
  ) THEN
ALTER TABLE bike_stores.order_items
    ADD COLUMN created_at timestamptz DEFAULT now();
END IF;
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='order_items' AND column_name='updated_at'
  ) THEN
ALTER TABLE bike_stores.order_items
    ADD COLUMN updated_at timestamptz DEFAULT now();
END IF;

  -- stocks
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='stocks' AND column_name='created_at'
  ) THEN
ALTER TABLE bike_stores.stocks
    ADD COLUMN created_at timestamptz DEFAULT now();
END IF;
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='stocks' AND column_name='updated_at'
  ) THEN
ALTER TABLE bike_stores.stocks
    ADD COLUMN updated_at timestamptz DEFAULT now();
END IF;

  -- customers
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='customers' AND column_name='created_at'
  ) THEN
ALTER TABLE bike_stores.customers
    ADD COLUMN created_at timestamptz DEFAULT now();
END IF;
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='customers' AND column_name='updated_at'
  ) THEN
ALTER TABLE bike_stores.customers
    ADD COLUMN updated_at timestamptz DEFAULT now();
END IF;

  -- stores
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='stores' AND column_name='created_at'
  ) THEN
ALTER TABLE bike_stores.stores
    ADD COLUMN created_at timestamptz DEFAULT now();
END IF;
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='stores' AND column_name='updated_at'
  ) THEN
ALTER TABLE bike_stores.stores
    ADD COLUMN updated_at timestamptz DEFAULT now();
END IF;

  -- staffs
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='staffs' AND column_name='created_at'
  ) THEN
ALTER TABLE bike_stores.staffs
    ADD COLUMN created_at timestamptz DEFAULT now();
END IF;
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema='bike_stores' AND table_name='staffs' AND column_name='updated_at'
  ) THEN
ALTER TABLE bike_stores.staffs
    ADD COLUMN updated_at timestamptz DEFAULT now();
END IF;

END;
$$;

-- create triggers (if not exists)
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'trg_products_updated_at' AND c.relname = 'products'
  ) THEN
CREATE TRIGGER trg_products_updated_at
    BEFORE UPDATE ON bike_stores.products
    FOR EACH ROW EXECUTE FUNCTION bike_stores.set_updated_at();
END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'trg_orders_updated_at' AND c.relname = 'orders'
  ) THEN
CREATE TRIGGER trg_orders_updated_at
    BEFORE UPDATE ON bike_stores.orders
    FOR EACH ROW EXECUTE FUNCTION bike_stores.set_updated_at();
END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'trg_order_items_updated_at' AND c.relname = 'order_items'
  ) THEN
CREATE TRIGGER trg_order_items_updated_at
    BEFORE UPDATE ON bike_stores.order_items
    FOR EACH ROW EXECUTE FUNCTION bike_stores.set_updated_at();
END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'trg_stocks_updated_at' AND c.relname = 'stocks'
  ) THEN
CREATE TRIGGER trg_stocks_updated_at
    BEFORE UPDATE ON bike_stores.stocks
    FOR EACH ROW EXECUTE FUNCTION bike_stores.set_updated_at();
END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'trg_customers_updated_at' AND c.relname = 'customers'
  ) THEN
CREATE TRIGGER trg_customers_updated_at
    BEFORE UPDATE ON bike_stores.customers
    FOR EACH ROW EXECUTE FUNCTION bike_stores.set_updated_at();
END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'trg_stores_updated_at' AND c.relname = 'stores'
  ) THEN
CREATE TRIGGER trg_stores_updated_at
    BEFORE UPDATE ON bike_stores.stores
    FOR EACH ROW EXECUTE FUNCTION bike_stores.set_updated_at();
END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'trg_staffs_updated_at' AND c.relname = 'staffs'
  ) THEN
CREATE TRIGGER trg_staffs_updated_at
    BEFORE UPDATE ON bike_stores.staffs
    FOR EACH ROW EXECUTE FUNCTION bike_stores.set_updated_at();
END IF;
END;
$$;
