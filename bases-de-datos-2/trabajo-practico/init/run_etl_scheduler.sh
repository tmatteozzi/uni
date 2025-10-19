#!/usr/bin/env bash
set -euo pipefail

# Variables de conexión a PostgreSQL
: "${PGHOST:=localhost}"
: "${PGPORT:=5432}"
: "${PGUSER:=postgres}"
: "${PGPASSWORD:=postgres}"
: "${PGDATABASE:=bike_stores}"

export PGPASSWORD

echo ">>> Iniciando scheduler de ETL (ejecución cada 30 segundos)"

# Esperar a que PostgreSQL esté listo
echo ">>> Esperando a que PostgreSQL esté disponible..."
until psql -h "$PGHOST" -p "$PGPORT" -U "$PGUSER" -d "$PGDATABASE" -c "SELECT 1" > /dev/null 2>&1; do
  echo ">>> PostgreSQL no está listo, esperando 2 segundos..."
  sleep 2
done

echo ">>> PostgreSQL está listo. Iniciando scheduler..."

# Loop infinito que ejecuta el ETL cada 30 segundos
while true; do
  echo ">>> [$(date '+%Y-%m-%d %H:%M:%S')] Ejecutando ETL..."
  psql -h "$PGHOST" -p "$PGPORT" -U "$PGUSER" -d "$PGDATABASE" -c "SELECT dwh.run_full_etl();" || echo ">>> Error en ETL, continuando..."
  echo ">>> [$(date '+%Y-%m-%d %H:%M:%S')] ETL finalizado. Esperando 30 segundos..."
  sleep 30
done

