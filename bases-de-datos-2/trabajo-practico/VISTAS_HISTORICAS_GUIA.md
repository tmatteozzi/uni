# 📊 Guía de Vistas Históricas de Métricas

## 🎯 Resumen

Se han creado **16 vistas** que facilitan el análisis temporal de las métricas OLAP sin necesidad de escribir consultas complejas con JOINs y GROUP BY.

---

## 📋 Lista de Vistas Creadas

### **ÓRDENES (Orders) - 5 vistas**
1. `v_orders_daily` - Métricas diarias
2. `v_orders_monthly` - Métricas mensuales  
3. `v_orders_quarterly` - Métricas trimestrales
4. `v_orders_yearly` - Métricas anuales
5. `v_orders_store_monthly` - Métricas mensuales por tienda

### **ENVÍOS (Shipments) - 5 vistas**
6. `v_shipments_daily` - Métricas diarias
7. `v_shipments_monthly` - Métricas mensuales
8. `v_shipments_quarterly` - Métricas trimestrales
9. `v_shipments_yearly` - Métricas anuales
10. `v_shipments_store_monthly` - Métricas mensuales por tienda

### **INVENTARIO (Stock) - 3 vistas**
11. `v_stock_current_by_store` - Stock actual por tienda
12. `v_stock_by_product_store` - Stock por producto y tienda
13. `v_stock_summary` - Resumen global de inventario

### **COMPARATIVAS Y DASHBOARDS - 3 vistas**
14. `v_orders_yoy_comparison` - Comparación Year over Year
15. `v_metrics_quarterly_trend` - Tendencia trimestral consolidada
16. `v_dashboard_kpis` - KPIs globales para dashboards

---

## 🔍 Uso de las Vistas

### 1. **v_orders_quarterly** - Métricas Trimestrales

**Uso:** Ver métricas históricas por trimestre (como pediste)

```sql
-- Ver todos los trimestres
SELECT * FROM dwh_cube.v_orders_quarterly 
ORDER BY year, quarter;
```

**Resultado:**
```
 year | quarter | quarter_label | dias_con_ventas | total_items | final_price | avg_ticket | discount_margin_pct 
------+---------+---------------+-----------------+-------------+-------------+------------+---------------------
 2016 |       1 | Q1            |              75 |         435 |   619039.06 |    4006.33 |                0.01
 2016 |       2 | Q2            |              73 |         394 |   646983.96 |    4940.24 |                0.01
 2017 |       1 | Q1            |              74 |         533 |  1013789.47 |    5873.87 |                0.01
 2017 |       2 | Q2            |              78 |         522 |   971666.50 |    5112.84 |                0.01
...
```

**Filtrar Q1 de todos los años:**
```sql
SELECT * FROM dwh_cube.v_orders_quarterly 
WHERE quarter = 1 
ORDER BY year;
```

---

### 2. **v_orders_monthly** - Métricas Mensuales

**Uso:** Análisis detallado mes a mes

```sql
-- Ver todos los meses de 2017
SELECT year, month_name, dias_con_ventas, total_items, final_price, avg_ticket 
FROM dwh_cube.v_orders_monthly 
WHERE year = 2017 
ORDER BY month;
```

**Resultado:**
```
 year | month_name | dias_con_ventas | total_items | final_price | avg_ticket 
------+------------+-----------------+-------------+-------------+------------
 2017 | January    |              25 |         157 |   316931.65 |    6218.16
 2017 | February   |              23 |         174 |   348711.69 |    5850.93
 2017 | March      |              26 |         202 |   348146.13 |    5563.10
...
```

---

### 3. **v_orders_yearly** - Métricas Anuales

**Uso:** Comparación entre años

```sql
-- Ver evolución anual
SELECT * FROM dwh_cube.v_orders_yearly 
ORDER BY year;
```

---

### 4. **v_orders_daily** - Métricas Diarias

**Uso:** Análisis día a día con información de calendario

```sql
-- Ver las mejores 10 días de ventas
SELECT date, day_name, is_weekend, total_items, final_price, avg_ticket
FROM dwh_cube.v_orders_daily 
ORDER BY final_price DESC 
LIMIT 10;
```

**Columnas disponibles:**
- `order_date_id`, `date`, `day_name`, `day_of_month`
- `week_of_month`, `week_of_year`
- `month`, `month_name`, `quarter`, `year`
- `is_weekend`
- `total_items`, `total_quantity`, `gross_amount`, `final_price`
- `avg_ticket`, `discount_margin_pct`

---

### 5. **v_orders_store_monthly** - Por Tienda y Mes

**Uso:** Comparar performance de tiendas a través del tiempo

```sql
-- Comparar tiendas en Q1 2017
SELECT store_name, month_name, total_items, final_price, avg_ticket
FROM dwh_cube.v_orders_store_monthly
WHERE year = 2017 AND month <= 3
ORDER BY store_name, month;
```

---

### 6. **v_shipments_quarterly** - Envíos por Trimestre

**Uso:** Analizar métricas de envíos y logística

```sql
-- Ver profit margin y tiempos de envío por trimestre
SELECT year, quarter_label, 
       total_shipments, 
       profit_margin_pct, 
       avg_shipping_days
FROM dwh_cube.v_shipments_quarterly
ORDER BY year, quarter;
```

---

### 7. **v_stock_current_by_store** - Stock Actual

**Uso:** Ver estado actual del inventario por tienda

```sql
-- Ver stock actual con capacidad y valor
SELECT * FROM dwh_cube.v_stock_current_by_store 
ORDER BY store_name;
```

**Resultado:**
```
 store_id | store_name       | city       | total_stock | stock_capacity_pct | stock_value | max_capacity
----------+------------------+------------+-------------+--------------------+-------------+--------------
        2 | Baldwin Bikes    | Baldwin    |        4359 |              36.33 |  6473126.28 |        12000
        3 | Rowlett Bikes    | Rowlett    |        4620 |              46.20 |  6859214.21 |        10000
        1 | Santa Cruz Bikes | Santa Cruz |        4532 |              30.21 |  6487242.16 |        15000
```

---

### 8. **v_stock_by_product_store** - Stock Detallado

**Uso:** Ver inventario por producto y tienda

```sql
-- Top 10 productos con más valor en inventario
SELECT store_name, product_name, brand_name, 
       total_stock, stock_value
FROM dwh_cube.v_stock_by_product_store
ORDER BY stock_value DESC
LIMIT 10;
```

---

### 9. **v_dashboard_kpis** - KPIs Globales

**Uso:** Dashboard ejecutivo con todas las métricas clave

```sql
-- Ver todos los KPIs en una sola consulta
SELECT * FROM dwh_cube.v_dashboard_kpis;
```

**Resultado:**
```
 scope  | total_orders_items | total_orders_revenue | global_avg_ticket | global_discount_margin | total_shipments | global_profit_margin | global_shipping_days | total_stock_units | total_stock_value | avg_capacity_utilization
--------+--------------------+----------------------+-------------------+------------------------+-----------------+----------------------+----------------------+-------------------+-------------------+--------------------------
 Global |               4722 |           8578243.80 |           5311.61 |                   0.01 |            4214 |                39.99 |                  2.0 |             13511 |       19819582.65 |                         
```

---

### 10. **v_orders_yoy_comparison** - Year over Year

**Uso:** Comparar crecimiento entre años

```sql
-- Ver crecimiento mensual year over year
SELECT year, month_name, 
       final_price, 
       final_price_prev_year,
       yoy_growth_pct
FROM dwh_cube.v_orders_yoy_comparison
WHERE year >= 2017 AND month <= 3
ORDER BY year, month;
```

**Muestra:**
- Ventas del mes actual
- Ventas del mismo mes año anterior
- % de crecimiento

---

### 11. **v_metrics_quarterly_trend** - Tendencia Consolidada

**Uso:** Ver todas las métricas juntas por trimestre

```sql
-- Ver evolución de todas las métricas
SELECT quarter_label, 
       orders_revenue, 
       orders_avg_ticket,
       shipments_profit_margin,
       shipments_avg_days
FROM dwh_cube.v_metrics_quarterly_trend
WHERE year = 2017;
```

---

## 🎨 Ejemplos de Análisis Comunes

### **Análisis 1: Mejor Trimestre del Año**

```sql
SELECT year, quarter_label, final_price, avg_ticket
FROM dwh_cube.v_orders_quarterly
WHERE year = 2017
ORDER BY final_price DESC;
```

### **Análisis 2: Estacionalidad (Ventas por Mes)**

```sql
SELECT month_name, 
       ROUND(AVG(final_price), 2) as avg_revenue,
       ROUND(AVG(avg_ticket), 2) as avg_ticket
FROM dwh_cube.v_orders_monthly
GROUP BY month, month_name
ORDER BY month;
```

### **Análisis 3: Performance de Tiendas**

```sql
SELECT store_name,
       SUM(final_price) as total_revenue,
       ROUND(AVG(avg_ticket), 2) as avg_ticket,
       SUM(total_items) as total_items
FROM dwh_cube.v_orders_store_monthly
WHERE year = 2017
GROUP BY store_name
ORDER BY total_revenue DESC;
```

### **Análisis 4: Días de la Semana Más Vendedores**

```sql
SELECT day_name,
       COUNT(*) as days_count,
       ROUND(AVG(final_price), 2) as avg_daily_revenue,
       SUM(total_items) as total_items
FROM dwh_cube.v_orders_daily
GROUP BY day_name, EXTRACT(dow FROM date::date)
ORDER BY EXTRACT(dow FROM date::date);
```

### **Análisis 5: Eficiencia Logística**

```sql
SELECT year, quarter_label,
       ROUND(AVG(profit_margin_pct), 2) as profit_margin,
       ROUND(AVG(avg_shipping_days), 1) as shipping_days
FROM dwh_cube.v_shipments_quarterly
GROUP BY year, quarter, quarter_label
ORDER BY year, quarter;
```

### **Análisis 6: Utilización de Capacidad**

```sql
SELECT store_name,
       total_stock,
       max_capacity,
       stock_capacity_pct,
       max_capacity - total_stock as available_capacity,
       stock_value
FROM dwh_cube.v_stock_current_by_store
ORDER BY stock_capacity_pct DESC;
```

---

## 🔄 Actualización de las Vistas

Las vistas se actualizan **automáticamente** porque leen directamente de los cubos materializados. Cuando ejecutas:

```sql
SELECT dwh.run_full_etl();
```

El proceso:
1. Actualiza las tablas de hechos
2. Refresca los cubos materializados
3. Las vistas automáticamente muestran los datos actualizados

---

## 📊 Conectar con Herramientas de BI

Estas vistas son perfectas para conectar con herramientas como:

- **Tableau**
- **Power BI**
- **Metabase**
- **Looker**
- **Grafana**

Simplemente conecta la herramienta a PostgreSQL y selecciona las vistas del schema `dwh_cube`.

---

## 💡 Ventajas de Usar las Vistas

✅ **Simplicidad**: No necesitas escribir JOINs complejos  
✅ **Consistencia**: Todos usan las mismas definiciones  
✅ **Performance**: Las vistas leen de cubos pre-calculados  
✅ **Flexibilidad**: Puedes filtrar y agregar según necesites  
✅ **Mantenibilidad**: Un solo lugar para actualizar la lógica  
✅ **Documentación**: Cada vista tiene un propósito claro  

---

## 📝 Listar Todas las Vistas

```sql
-- Ver todas las vistas disponibles
\dv dwh_cube.*

-- Ver descripción de una vista
\d+ dwh_cube.v_orders_quarterly
```

---

## 🎯 Casos de Uso por Rol

### **CFO / Finanzas**
- `v_orders_quarterly` - Resultados trimestrales
- `v_orders_yearly` - Evolución anual
- `v_dashboard_kpis` - KPIs ejecutivos

### **Gerente de Tienda**
- `v_orders_store_monthly` - Performance de su tienda
- `v_stock_current_by_store` - Estado de inventario
- `v_shipments_store_monthly` - Métricas de envíos

### **Analista de Datos**
- `v_orders_daily` - Análisis detallado día a día
- `v_orders_yoy_comparison` - Análisis de crecimiento
- `v_metrics_quarterly_trend` - Tendencias consolidadas

### **Gerente de Inventario**
- `v_stock_current_by_store` - Estado actual
- `v_stock_by_product_store` - Detalle por producto
- `v_stock_summary` - Resumen global

---

## 🚀 Próximos Pasos

1. **Explorar las vistas** con las consultas de ejemplo
2. **Crear dashboards** en tu herramienta de BI favorita
3. **Agregar filtros** según tus necesidades específicas
4. **Combinar vistas** para análisis más complejos

---

## 📚 Documentos Relacionados

- `METRICAS_README.md` - Descripción de las métricas
- `HISTORICO_METRICAS.md` - Cómo funciona el histórico
- `consultas_trimestre_ejemplo.sql` - Más ejemplos de consultas
- `test_metrics.sql` - Script de prueba de métricas


