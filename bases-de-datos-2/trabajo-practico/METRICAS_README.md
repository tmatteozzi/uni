# Métricas OLAP - Cubos de Análisis

Este documento describe las métricas implementadas en cada cubo OLAP para análisis y reporting.

## 📊 CUBO 1: Orders (Órdenes)

### Métricas Implementadas

#### 1. **Ticket Promedio** (`avg_ticket`)
- **Descripción**: Valor promedio de cada orden (precio final por orden)
- **Fórmula**: `SUM(discounted_order_amount) / COUNT(DISTINCT order_id)`
- **Unidad**: Moneda (USD)
- **Uso**: Mide el valor promedio de compra por cliente/orden

#### 2. **Margen de Descuento** (`discount_margin_pct`)
- **Descripción**: Porcentaje total de descuento aplicado sobre el precio de lista
- **Fórmula**: `((SUM(order_amount) - SUM(discounted_order_amount)) / SUM(order_amount)) * 100`
- **Unidad**: Porcentaje (%)
- **Uso**: Mide la agresividad de la estrategia de descuentos

### Ejemplos de Consulta

```sql
-- Ticket promedio por tienda
SELECT 
    dst.store_name,
    ROUND(co.avg_ticket, 2) as ticket_promedio,
    ROUND(co.discount_margin_pct, 2) as margen_descuento_pct
FROM dwh_cube.cube_bike_order co
JOIN dwh.dim_store dst ON co.store_sk = dst.store_sk
WHERE co.store_sk IS NOT NULL 
  AND co.order_date_id IS NULL 
  AND co.product_sk IS NULL 
  AND co.customer_sk IS NULL 
  AND co.staff_sk IS NULL;
```

---

## 📦 CUBO 2: Shipments (Envíos)

### Métricas Implementadas

#### 1. **Profit Margin** (`profit_margin_pct`)
- **Descripción**: Margen de ganancia calculado sobre el precio de venta final
- **Fórmula**: `((SUM(discounted_shipment_amount) - SUM(estimated_cost)) / SUM(discounted_shipment_amount)) * 100`
- **Unidad**: Porcentaje (%)
- **Nota**: Usa un costo estimado del 60% del precio de lista
- **Uso**: Mide la rentabilidad de los productos enviados

#### 2. **Tiempo Promedio de Envío** (`avg_shipping_days`)
- **Descripción**: Días promedio entre la fecha de orden y la fecha de envío
- **Fórmula**: `AVG(shipped_date - order_date)`
- **Unidad**: Días
- **Uso**: Mide la eficiencia logística y cumplimiento de tiempos de entrega

### Ejemplos de Consulta

```sql
-- Métricas de envío por tienda
SELECT 
    dst.store_name,
    ROUND(cs.profit_margin_pct, 2) as profit_margin_pct,
    ROUND(cs.avg_shipping_days, 1) as dias_envio_promedio
FROM dwh_cube.cube_bike_shipment cs
JOIN dwh.dim_store dst ON cs.store_sk = dst.store_sk
WHERE cs.store_sk IS NOT NULL 
  AND cs.shipment_date_id IS NULL 
  AND cs.product_sk IS NULL;
```

---

## 🏪 CUBO 3: Stock (Inventario)

### Métricas Implementadas

#### 1. **Porcentaje de Capacidad Utilizada** (`stock_capacity_pct`)
- **Descripción**: Porcentaje del inventario actual vs. capacidad máxima de la tienda
- **Fórmula**: `(SUM(quantity) / MAX(store_capacity.max_capacity)) * 100`
- **Unidad**: Porcentaje (%)
- **Nota**: Solo se calcula cuando `store_sk` no está agregado
- **Uso**: Mide cuánto espacio de almacenamiento está siendo utilizado

**Capacidades por Tienda:**
- Santa Cruz Bikes: 15,000 unidades
- Baldwin Bikes: 12,000 unidades
- Rowlett Bikes: 10,000 unidades

#### 2. **Valor del Inventario** (`stock_value`)
- **Descripción**: Valor monetario total del inventario basado en precios de lista
- **Fórmula**: `SUM(quantity * list_price)`
- **Unidad**: Moneda (USD)
- **Uso**: Mide cuánto capital está invertido en inventario por tienda/producto

### Ejemplos de Consulta

```sql
-- Capacidad utilizada por tienda
SELECT 
    dst.store_name,
    sc.max_capacity as capacidad_maxima,
    css.total_stock as stock_actual,
    ROUND(css.stock_capacity_pct, 2) as capacidad_utilizada_pct
FROM dwh_cube.cube_store_stock css
JOIN dwh.dim_store dst ON css.store_sk = dst.store_sk
JOIN dwh.store_capacity sc ON css.store_sk = sc.store_sk
WHERE css.store_sk IS NOT NULL 
  AND css.date_id IS NULL 
  AND css.product_sk IS NULL;

-- Productos con mayor valor en inventario
SELECT 
    dst.store_name,
    dp.product_name,
    css.total_stock as stock_actual,
    ROUND(dp.list_price, 2) as precio_unitario,
    ROUND(css.stock_value, 2) as valor_inventario
FROM dwh_cube.cube_store_stock css
JOIN dwh.dim_store dst ON css.store_sk = dst.store_sk
JOIN dwh.dim_product dp ON css.product_sk = dp.product_sk
WHERE css.store_sk IS NOT NULL 
  AND css.product_sk IS NOT NULL 
ORDER BY css.stock_value DESC
LIMIT 10;
```

---

## 🔄 Actualización de Métricas

### ETL Automático

Las métricas se actualizan automáticamente mediante el proceso ETL:

1. **Frecuencia**: Según el scheduler configurado en `run_etl_scheduler.sh`
2. **Proceso**:
   - Actualización de dimensiones (SCD Type 2)
   - Carga incremental de hechos con nuevas columnas calculadas
   - Refresh de cubos materializados con todas las métricas

### Ejecución Manual del ETL

```sql
-- Ejecutar ETL completo
SELECT dwh.run_full_etl();

-- Verificar ejecuciones
SELECT * FROM etl.etl_runs ORDER BY finished_at DESC LIMIT 10;
```

### Refresh de Cubos

```sql
-- Refresh individual
REFRESH MATERIALIZED VIEW CONCURRENTLY dwh_cube.cube_bike_order;
REFRESH MATERIALIZED VIEW CONCURRENTLY dwh_cube.cube_bike_shipment;
REFRESH MATERIALIZED VIEW CONCURRENTLY dwh_cube.cube_store_stock;

-- Refresh mediante función ETL
SELECT dwh.etl_refresh_cubes();
```

---

## 📈 Historicidad

Todas las métricas mantienen historicidad mediante:

1. **Dimensiones SCD Type 2**: Mantienen el historial de cambios con `valid_from`, `valid_to`, e `is_current`
2. **Hechos con timestamp**: Todas las tablas de hechos incluyen `date_id` para análisis temporal
3. **Cubos OLAP**: Permiten análisis multidimensional a través del tiempo con agregaciones precalculadas

---

## 🧪 Testing

Para probar todas las métricas, ejecutar:

```bash
docker exec pg_dw psql -U postgres -d bike_stores -f /tmp/test_metrics.sql
```

O copiar el archivo primero:

```bash
docker cp test_metrics.sql pg_dw:/tmp/test_metrics.sql
docker exec pg_dw psql -U postgres -d bike_stores -f /tmp/test_metrics.sql
```

---

## 📊 Resultados de Ejemplo

### Cubo Orders
- **Ticket Promedio Global**: $5,311.61
- **Margen de Descuento Global**: 0.01%

### Cubo Shipments
- **Profit Margin Global**: 39.99%
- **Días de Envío Promedio**: 2.0 días

### Cubo Stock
- **Santa Cruz Bikes**: 30.21% capacidad utilizada (4,532 / 15,000)
- **Baldwin Bikes**: 36.33% capacidad utilizada (4,359 / 12,000)
- **Rowlett Bikes**: 46.20% capacidad utilizada (4,620 / 10,000)

---

## 🔧 Estructura de Datos

### Nuevas Columnas en Hechos

**fact_bike_shipment**:
- `order_date_id`: Para calcular días de envío
- `estimated_cost`: Costo estimado (60% del precio de lista)
- `shipping_days`: Días entre orden y envío

**dim_store** + **store_capacity**:
- Nueva tabla `dwh.store_capacity` con capacidad máxima por tienda

### Índices

Todos los cubos mantienen:
- Índice único en `gid` + todas las dimensiones
- Índices en fechas
- Índices en combinaciones frecuentes (store + product)

---

## 📝 Notas Importantes

1. Las métricas de **stock** (`stock_capacity_pct` y `days_of_coverage`) solo se calculan en niveles de agregación específicos para evitar valores sin sentido
2. El **profit margin** usa un costo estimado del 60% del precio de lista. Ajustar en `create_facts.sql` si se tienen costos reales
3. Los **días de cobertura** se calculan basados en los últimos 30 días de ventas
4. Todas las métricas son **compatibles con herramientas de BI** (Tableau, Power BI, Metabase, etc.)

