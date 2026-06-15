#!/bin/bash
# ==============================================================================
# Génère un certificat SSL auto-signé PKCS12 pour la gateway ESPHERE-ASS
#
# Usage :
#   bash generate-ssl-cert.sh
#
# Résultat : ./certs/keystore.p12
#
# ATTENTION : Un certificat auto-signé provoque un avertissement dans le
# navigateur. Pour la production, utilisez Let's Encrypt (certbot) ou un
# certificat commercial et importez-le dans le keystore.
# ==============================================================================

set -e

# Charger les variables depuis .env si disponible
if [ -f .env ]; then
  export $(grep -v '^#' .env | grep -v '^$' | xargs)
fi

CERTS_DIR="./certs"
KEYSTORE="$CERTS_DIR/keystore.p12"
ALIAS="${KEY_ALIAS:-gateway}"
PASSWORD="${KEYSTORE_PASSWORD:-change_me_keystore_password}"
SERVER_IP="${SERVER_IP:-77.68.94.193}"
VALIDITY_DAYS=365

mkdir -p "$CERTS_DIR"

echo "──────────────────────────────────────────────"
echo " Génération du keystore PKCS12"
echo " Alias    : $ALIAS"
echo " Serveur  : $SERVER_IP"
echo " Validité : $VALIDITY_DAYS jours"
echo " Sortie   : $KEYSTORE"
echo "──────────────────────────────────────────────"

# Vérifier que keytool est disponible
if ! command -v keytool &> /dev/null; then
  echo "ERREUR : 'keytool' introuvable. Installez le JDK (Java) sur le serveur."
  exit 1
fi

keytool -genkeypair \
  -alias "$ALIAS" \
  -keyalg RSA \
  -keysize 2048 \
  -sigalg SHA256withRSA \
  -validity "$VALIDITY_DAYS" \
  -storetype PKCS12 \
  -keystore "$KEYSTORE" \
  -storepass "$PASSWORD" \
  -dname "CN=$SERVER_IP, OU=ESPHERE, O=Zenithe, L=Paris, ST=IDF, C=FR" \
  -ext "SAN=IP:$SERVER_IP,DNS:localhost"

echo ""
echo "✓ Keystore généré : $KEYSTORE"
echo ""
echo "Prochaine étape : définir KEYSTORE_PASSWORD=$PASSWORD dans .env"
