#!/usr/bin/env bash
set -euo pipefail

# Variables: dentro del contenedor docker entrypoint normalmente ya existen
: "${PGHOST:=localhost}"
: "${PGPORT:=5432}"
: "${PGUSER:=postgres}"
: "${PGPASSWORD:=postgres}"
: "${PGDATABASE:=bike_stores}"

export PGPASSWORD

echo ">>> Ejecutando ETL: funciones dwh.run_full_etl()"
psql -v ON_ERROR_STOP=1 -h "$PGHOST" -p "$PGPORT" -U "$PGUSER" -d "$PGDATABASE" -c "SELECT dwh.run_full_etl();"

echo ">>> ETL finalizado."
