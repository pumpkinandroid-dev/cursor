#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

echo "Checking Java..."
if ! command -v java >/dev/null 2>&1; then echo "Java not found. Install Java 17+"; exit 1; fi

echo "Checking Maven..."
if ! command -v mvn >/dev/null 2>&1; then echo "Maven not found. Install Maven"; exit 1; fi

echo "Building cnbeta-summary..."
mvn -q -f cnbeta-summary/pom.xml clean package

PORT="${PORT:-8080}"
echo "Running on http://localhost:${PORT}"
exec java -DPORT=${PORT} -jar cnbeta-summary/target/cnbeta-summary-1.0.0-shaded.jar




