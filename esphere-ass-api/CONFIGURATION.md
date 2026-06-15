# ESPHERE ASS API — Guide de configuration

## Architecture de l'écosystème

```
Angular 18 SPA (port 4200 dev)
    │
    ├─ proxy dev → Gateway (port 8080)
    │                  └─ esphere-ass-admin (port 8083)
    └─ prod direct → https://77.68.94.193:4141 (gateway HTTPS)
                         └─ esphere-ass-admin (port 8443 HTTPS)

Eureka Discovery  → port 8762
Config Server     → port 9101 (optionnel)
Zipkin Tracing    → port 9411 (optionnel)
Oracle 11g        → port 1521
```

---

## Backend — Variables d'environnement

### Développement (`.env` ou IntelliJ Run Config)
```
ORACLE_URL=jdbc:oracle:thin:@localhost:1521:ORCL
ORACLE_USER=esphere_user
ORACLE_PASSWORD=secret
ZENITHE_LOGIN_POOLTPV=votre_login
ZENITHE_PASSWORD_POOLTPV=votre_password
SPRING_PROFILES_ACTIVE=dev
```

### Production (obligatoires)
```
ORACLE_URL=jdbc:oracle:thin:@<host>:1521:<SID>
ORACLE_USER=...
ORACLE_PASSWORD=...
JWT_SECRET=<chaine-min-64-chars>         # NE PAS réutiliser le secret dev
KEYSTORE_PATH=/opt/esphere/esphere-ass.p12
KEYSTORE_PASSWORD=...
KEY_ALIAS=esphere-ass
EUREKA_SERVER_URL=http://77.68.94.193:8762/eureka/
EUREKA_INSTANCE_HOSTNAME=77.68.94.193
FRONTEND_URL=https://77.68.94.193
ZENITHE_LOGIN_POOLTPV=...
ZENITHE_PASSWORD_POOLTPV=...
SPRING_PROFILES_ACTIVE=prod
```

### Générer un keystore PKCS12 (production HTTPS)
```bash
keytool -genkeypair \
  -alias esphere-ass \
  -keyalg RSA -keysize 2048 \
  -storetype PKCS12 \
  -keystore esphere-ass.p12 \
  -validity 3650 \
  -dname "CN=77.68.94.193, OU=IT, O=Esphere, L=Yaounde, ST=Centre, C=CM"
```

---

## Lancer le backend

```bash
# Développement
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production
java -Dspring.profiles.active=prod \
     -DORACLE_URL="..." \
     -DJWT_SECRET="..." \
     -jar esphere-ass-administration-api-0.0.1-SNAPSHOT.jar
```

---

## Flyway Oracle 11g

Les scripts sont dans `src/main/resources/db/migration/`.
Flyway crée automatiquement la table `flyway_schema_history` au premier lancement.

- **Développement** : `out-of-order=true`, `repair-on-migrate=true`
- **Production** : strict, pas de `cleanOnValidationError`

Pour réparer manuellement : `mvn flyway:repair`

---

## Frontend — Démarrer

```bash
cd esphere-ass-web
npm install
npm start          # dev avec proxy → http://localhost:4200

# Build production
ng build --configuration production
```

Le build production remplace `environment.ts` par `environment.prod.ts`.

---

## Gestion des erreurs — Flux

```
Backend Exception
    → GlobalExceptionHandler (@RestControllerAdvice)
        → JSON structuré { status, message, errors, errorType }
            → HTTP response avec bon code HTTP
                → Angular ErrorInterceptor
                    → GlobalErrorService.handleAndNotify()
                        → Toast utilisateur (ngx-toastr)
                        + FrontendError disponible dans les composants
```

---

## Sécurité Spring Security

Endpoints **publics** (sans token) :
- `POST /auth/login`
- `GET /auth/health`
- `GET /actuator/health`
- `GET /swagger-ui/**` (désactivé en prod)

Tous les autres endpoints nécessitent un `Authorization: Bearer <token>` valide.
