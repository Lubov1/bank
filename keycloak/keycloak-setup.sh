# keycloak-setup.sh
#!/bin/bash
set -e

until /opt/keycloak/bin/kcadm.sh config credentials \
        --server http://localhost:8080 \
        --realm master \
        --user  "$KEYCLOAK_ADMIN" \
        --password "$KEYCLOAK_ADMIN_PASSWORD" > /dev/null 2>&1
do
  echo "⏳ Waiting for Keycloak..."
  sleep 5
done
echo "✅ Keycloak is up"

EXISTS=$(/opt/keycloak/bin/kcadm.sh get clients -r master --fields clientId \
          | grep -o '"bank"' || true)

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'bank'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=bank \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=true \
      -s secret=bank-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'bank' already exists"
fi
EXISTS=$(/opt/keycloak/bin/kcadm.sh get clients -r master --fields clientId \
          | grep -o '"accounts"' || true)

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'accounts'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=accounts \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=false \
      -s secret=accounts-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'accounts' already exists"
fi

EXISTS=$(/opt/keycloak/bin/kcadm.sh get clients -r master --fields clientId \
          | grep -o '"front"' || true)

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'front'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=front \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=false \
      -s secret=front-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'front' already exists"
fi