#!/bin/sh
set -eu

CONSUL_ADDR="${CONSUL_ADDR:-http://consul:8500}"
CONSUL_KV_PREFIX="${CONSUL_KV_PREFIX:-config}"
CONSUL_DATA_KEY="${CONSUL_DATA_KEY:-data}"

echo "Seeding Consul KV at $CONSUL_ADDR ..."

# Список файлов/контекстов: имя файла = имя контекста в KV
# application.yaml  -> config/application/data
# orders.yaml       -> config/orders/data
# orders,prod.yaml  -> config/orders,prod/data
for f in /config/kv/*.yml; do
  [ -f "$f" ] || continue
  base="$(basename "$f" .yml)"
  key="${CONSUL_KV_PREFIX}/${base}/${CONSUL_DATA_KEY}"
  echo "PUT $key  <=  $f"
  curl -sS -X PUT --data-binary @"$f" "${CONSUL_ADDR}/v1/kv/${key}" > /dev/null
done

echo "Done."
