#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

export PGPASSWORD="password"
export PGUSER="postgres"
export PGHOST=localhost
export PGDATABASE="cd"

psql -a -f ./data.sql