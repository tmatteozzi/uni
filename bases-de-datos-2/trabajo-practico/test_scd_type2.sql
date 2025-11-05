-- =========================================================
-- TEST: SCD Type 2 Implementation
-- This script tests the Slowly Changing Dimension Type 2
-- implementation by simulating dimension changes
-- =========================================================

\echo '========================================='
\echo 'TEST 1: Initial State - Check Dimensions'
\echo '========================================='

-- Check initial state of dim_store
SELECT 
    store_sk,
    store_id, 
    store_name, 
    city,
    valid_from,
    valid_to,
    is_current
FROM dwh.dim_store
WHERE store_id = 1
ORDER BY valid_from;

\echo ''
\echo 'Initial store count:'
SELECT COUNT(*) as total_stores, 
       COUNT(*) FILTER (WHERE is_current = true) as current_stores,
       COUNT(*) FILTER (WHERE is_current = false) as historical_stores
FROM dwh.dim_store;

\echo ''
\echo '========================================='
\echo 'TEST 2: Simulate Store Name Change'
\echo '========================================='

-- Update a store name in the source system
UPDATE bike_stores.stores
SET store_name = 'Santa Cruz Bikes - UPDATED'
WHERE store_id = 1;

\echo 'Store updated in source system'

-- Wait a moment to ensure timestamp difference
SELECT pg_sleep(1);

-- Run the ETL for dim_store
SELECT dwh.etl_dim_store();

\echo ''
\echo 'After ETL - Store records (should have 2 versions now):'
SELECT 
    store_sk,
    store_id, 
    store_name, 
    city,
    valid_from,
    valid_to,
    is_current
FROM dwh.dim_store
WHERE store_id = 1
ORDER BY valid_from;

\echo ''
\echo 'Store count after update:'
SELECT COUNT(*) as total_stores, 
       COUNT(*) FILTER (WHERE is_current = true) as current_stores,
       COUNT(*) FILTER (WHERE is_current = false) as historical_stores
FROM dwh.dim_store;

\echo ''
\echo '========================================='
\echo 'TEST 3: Simulate Customer Address Change'
\echo '========================================='

-- Update a customer
UPDATE bike_stores.customers
SET street = '123 New Street - UPDATED',
    city = 'New City'
WHERE customer_id = 1;

\echo 'Customer updated in source system'

SELECT pg_sleep(1);

-- Run the ETL for dim_customer
SELECT dwh.etl_dim_customer();

\echo ''
\echo 'After ETL - Customer records (should have 2 versions now):'
SELECT 
    customer_sk,
    customer_id,
    first_name,
    last_name,
    street,
    valid_from,
    valid_to,
    is_current
FROM dwh.dim_customer
WHERE customer_id = 1
ORDER BY valid_from;

\echo ''
\echo '========================================='
\echo 'TEST 4: Simulate Product Price Change'
\echo '========================================='

-- Update a product price
UPDATE bike_stores.products
SET list_price = 999.99
WHERE product_id = 1;

\echo 'Product updated in source system'

SELECT pg_sleep(1);

-- Run the ETL for dim_product
SELECT dwh.etl_dim_product();

\echo ''
\echo 'After ETL - Product records (should have 2 versions now):'
SELECT 
    product_sk,
    product_id,
    product_name,
    list_price,
    valid_from,
    valid_to,
    is_current
FROM dwh.dim_product
WHERE product_id = 1
ORDER BY valid_from;

\echo ''
\echo '========================================='
\echo 'TEST 5: Verify Fact Tables Use Current SKs'
\echo '========================================='

-- Add a new order to see if it uses the current (new) surrogate keys
INSERT INTO bike_stores.orders (customer_id, order_status, order_date, required_date, shipped_date, store_id, staff_id)
VALUES (1, 4, CURRENT_DATE, CURRENT_DATE + 3, CURRENT_DATE + 1, 1, 1);

INSERT INTO bike_stores.order_items (order_id, item_id, product_id, quantity, list_price, discount)
VALUES (currval('bike_stores.orders_order_id_seq'), 1, 1, 1, 999.99, 0);

\echo 'New order created in source system'

SELECT pg_sleep(1);

-- Run the ETL for fact_orders
SELECT dwh.etl_fact_orders();

\echo ''
\echo 'Check if new order uses updated surrogate keys:'
SELECT 
    fo.order_id,
    fo.customer_sk,
    dc.customer_id,
    dc.street as customer_street,
    dc.is_current as customer_is_current,
    fo.store_sk,
    ds.store_id,
    ds.store_name,
    ds.is_current as store_is_current,
    fo.product_sk,
    dp.product_id,
    dp.product_name,
    dp.list_price,
    dp.is_current as product_is_current
FROM dwh.fact_bike_order fo
JOIN dwh.dim_customer dc ON fo.customer_sk = dc.customer_sk
JOIN dwh.dim_store ds ON fo.store_sk = ds.store_sk
JOIN dwh.dim_product dp ON fo.product_sk = dp.product_sk
WHERE fo.order_id = (SELECT MAX(order_id) FROM dwh.fact_bike_order);

\echo ''
\echo '========================================='
\echo 'TEST 6: Historical Query Example'
\echo '========================================='
\echo 'Show all versions of store_id = 1 with their validity periods:'

SELECT 
    store_sk,
    store_id,
    store_name,
    city,
    valid_from,
    COALESCE(valid_to::text, 'CURRENT') as valid_to,
    is_current,
    CASE 
        WHEN is_current THEN 'Active Version'
        ELSE 'Historical Version'
    END as version_status
FROM dwh.dim_store
WHERE store_id = 1
ORDER BY valid_from;

\echo ''
\echo '========================================='
\echo 'TEST 7: Summary Statistics'
\echo '========================================='

\echo 'Dimension Statistics:'
SELECT 
    'dim_customer' as dimension,
    COUNT(*) as total_records,
    COUNT(*) FILTER (WHERE is_current = true) as current_records,
    COUNT(*) FILTER (WHERE is_current = false) as historical_records
FROM dwh.dim_customer
UNION ALL
SELECT 
    'dim_store',
    COUNT(*),
    COUNT(*) FILTER (WHERE is_current = true),
    COUNT(*) FILTER (WHERE is_current = false)
FROM dwh.dim_store
UNION ALL
SELECT 
    'dim_staff',
    COUNT(*),
    COUNT(*) FILTER (WHERE is_current = true),
    COUNT(*) FILTER (WHERE is_current = false)
FROM dwh.dim_staff
UNION ALL
SELECT 
    'dim_product',
    COUNT(*),
    COUNT(*) FILTER (WHERE is_current = true),
    COUNT(*) FILTER (WHERE is_current = false)
FROM dwh.dim_product;

\echo ''
\echo '========================================='
\echo 'TEST COMPLETED SUCCESSFULLY!'
\echo '========================================='
\echo ''
\echo 'Key Findings:'
\echo '- Old records should have is_current = false and valid_to set'
\echo '- New records should have is_current = true and valid_to = NULL'
\echo '- Fact tables should reference current surrogate keys'
\echo '- Historical data is preserved for audit/analysis'

