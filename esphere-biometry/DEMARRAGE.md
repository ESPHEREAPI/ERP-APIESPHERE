# ESPHERE Biometry — Guide de démarrage

## Ordre de démarrage obligatoire

```
1. esphere-eureka-server   (port 8761)
2. esphere-config-server   (port 8888)
3. esphere-gateway         (port 8080)
4. esphere-auth-service    (port 8081)
5. esphere-adherent-service (port 8082)
6. esphere-prestataire-service (port 8083)
7. esphere-visite-service  (port 8084)
8. esphere-validation-service (port 8085)
9. esphere-bon-manuel-service (port 8086)
10. esphere-media-service  (port 8087)
11. esphere-notification-service (port 8088)
12. esphere-reporting-service (port 8089)
```

---

## Développement local

### Lancer un service en dev (NetBeans)
VM Options : `-Dspring.profiles.active=dev`

### Lancer via Maven
```bash
cd esphere-auth-service
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Build pour production

### Générer le JAR (depuis chaque dossier service)
```bash
mvn clean package -DskipTests
```

### Générer tous les JARs depuis la racine
```bash
mvn clean package -DskipTests
```

---

## Démarrage en production

### Windows Server — variables d'environnement à définir une seule fois
```cmd
setx SPRING_PROFILES_ACTIVE "prod" /M
setx ESPHERE_HOST "109.228.49.89" /M
setx EUREKA_HOSTNAME "109.228.49.89" /M
setx CONFIG_HOST "109.228.49.89" /M
setx DB_HOST "109.228.49.89" /M
setx DB_PORT "3306" /M
setx DB_NAME "biometry" /M
setx DB_USERNAME "esphere_user" /M
setx DB_PASSWORD "votre_mot_de_passe" /M
setx JWT_SECRET "votre_secret_jwt_64_chars" /M
setx MAIL_PASSWORD "votre_mot_de_passe_email" /M
setx SMS_API_PASSWORD "votre_mot_de_passe_sms" /M
setx LOG_PATH "C:/esphere/logs" /M
setx SSL_KEYSTORE_PASSWORD "votre_mot_de_passe_keystore" /M
```

### Windows Server — lancement d'un service
```cmd
java -jar esphere-auth-service.jar
```

### Windows Server — lancement avec profil explicite (si variable non définie)
```cmd
java -Dspring.profiles.active=prod -jar esphere-auth-service.jar
```

### Linux — export des variables
```bash
export SPRING_PROFILES_ACTIVE=prod
export ESPHERE_HOST=109.228.49.89
export EUREKA_HOSTNAME=109.228.49.89
export CONFIG_HOST=109.228.49.89
export DB_HOST=109.228.49.89
export DB_PORT=3306
export DB_NAME=biometry
export DB_USERNAME=esphere_user
export DB_PASSWORD=votre_mot_de_passe
export JWT_SECRET=votre_secret_jwt_64_chars
export MAIL_PASSWORD=votre_mot_de_passe_email
export SMS_API_PASSWORD=votre_mot_de_passe_sms
export LOG_PATH=/var/log/esphere
export SSL_KEYSTORE_PASSWORD=votre_mot_de_passe_keystore
```

### Linux — lancement d'un service
```bash
java -jar esphere-auth-service.jar
```

### Linux — service systemd (recommandé pour la prod)
```ini
# /etc/systemd/system/esphere-auth.service
[Unit]
Description=ESPHERE Auth Service
After=network.target

[Service]
User=esphere
EnvironmentFile=/etc/esphere/esphere.env
ExecStart=/usr/bin/java -jar /opt/esphere/esphere-auth-service.jar
SuccessExitStatus=143
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable esphere-auth
sudo systemctl start esphere-auth
sudo systemctl status esphere-auth
```

---

## Fichier /etc/esphere/esphere.env (Linux prod)
```
SPRING_PROFILES_ACTIVE=prod
ESPHERE_HOST=109.228.49.89
EUREKA_HOSTNAME=109.228.49.89
DB_HOST=109.228.49.89
DB_USERNAME=esphere_user
DB_PASSWORD=votre_mot_de_passe
JWT_SECRET=votre_secret_64_chars
MAIL_PASSWORD=votre_mot_de_passe_email
SMS_API_PASSWORD=votre_mot_de_passe_sms
LOG_PATH=/var/log/esphere
```
