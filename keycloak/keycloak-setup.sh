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

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'exchange'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=exchange \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=false \
      -s secret=exchange-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'exchange' already exists"
fi

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'exchange-generator'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=exchange-generator \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=false \
      -s secret=exchange-generator-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'exchange-generator' already exists"
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

EXISTS=$(/opt/keycloak/bin/kcadm.sh get clients -r master --fields clientId \
          | grep -o '"gateway"' || true)

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'gateway'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=gateway \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=false \
      -s secret=gateway-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'gateway' already exists"
fi

EXISTS=$(/opt/keycloak/bin/kcadm.sh get clients -r master --fields clientId \
          | grep -o '"notifications"' || true)

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'notifications'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=notifications \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=false \
      -s secret=notifications-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'notifications' already exists"
fi

EXISTS=$(/opt/keycloak/bin/kcadm.sh get clients -r master --fields clientId \
          | grep -o '"cash"' || true)

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'cash'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=cash \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=false \
      -s secret=cash-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'cash' already exists"
fi

EXISTS=$(/opt/keycloak/bin/kcadm.sh get clients -r master --fields clientId \
          | grep -o '"blocker"' || true)

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'blocker'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=blocker \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=false \
      -s secret=blocker-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'blocker' already exists"
fi

EXISTS=$(/opt/keycloak/bin/kcadm.sh get clients -r master --fields clientId \
          | grep -o '"transfer"' || true)

if [ -z "$EXISTS" ]; then
  echo "➕ Creating client 'transfer'"
  /opt/keycloak/bin/kcadm.sh create clients -r master \
      -s clientId=transfer \
      -s enabled=true \
      -s protocol=openid-connect \
      -s publicClient=false \
      -s secret=transfer-secret \
      -s serviceAccountsEnabled=true
else
  echo "ℹ️  Client 'transfer' already exists"
fi