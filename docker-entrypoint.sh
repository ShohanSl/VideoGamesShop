#!/bin/sh
set -eu

if [ -n "${DATABASE_URL:-}" ] && [ -z "${SPRING_DATASOURCE_URL:-}" ]; then
  db_url="${DATABASE_URL#postgresql://}"
  db_url="${db_url#postgres://}"
  credentials="${db_url%@*}"
  host_and_db="${db_url#*@}"
  user="${credentials%%:*}"
  password="${credentials#*:}"
  host_port="${host_and_db%%/*}"
  database_with_params="${host_and_db#*/}"
  database="${database_with_params%%\?*}"

  export SPRING_DATASOURCE_URL="jdbc:postgresql://${host_port}/${database}"
  export SPRING_DATASOURCE_USERNAME="${SPRING_DATASOURCE_USERNAME:-$user}"
  export SPRING_DATASOURCE_PASSWORD="${SPRING_DATASOURCE_PASSWORD:-$password}"
fi

exec java $JAVA_OPTS -jar /app/app.jar
