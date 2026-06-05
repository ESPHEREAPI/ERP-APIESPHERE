# ESPHERE BIOMETRY — Plateforme Biométrie ZENITHE INSURANCE

## Architecture Microservices Spring Boot 3.3.6

### Services et Ports

| Service | Port | Description |
|---|---|---|
| API Gateway | 8080 | Routage + validation JWT |
| Auth Service | 8081 | Authentification + JWT |
| Adherent Service | 8082 | Assurés + ayants droit |
| Prestataire Service | 8083 | Réseau de soins |
| Visite Service | 8084 | Cœur transactionnel |
| Validation Service | 8085 | Validation SS |
| Bon Manuel Service | 8086 | Bons manuels |
| Media Service | 8087 | Capture photos/vidéos |
| Notification Service | 8088 | SMS + Email + Alertes |
| Reporting Service | 8089 | Tableaux de bord |

### Stack Technique
- Java 17 + Spring Boot 3.3.6
- Spring Cloud 2023.0.3
- MySQL + Flyway
- JWT (jjwt 0.11.5)
- Spring Mail + SMS smsvas.com

### Démarrage (ordre recommandé)
1. Auth Service (8081)
2. Adherent Service (8082)
3. Prestataire Service (8083)
4. Visite Service (8084)
5. Validation Service (8085)
6. Bon Manuel Service (8086)
7. Media Service (8087)
8. Notification Service (8088)
9. Reporting Service (8089)
10. API Gateway (8080)

### Configuration Base de données
- Host: localhost:3306
- Database: biometry
- Username: root

### Droits d'accès par catégorie prestataire

| Catégorie | Consultation | Ordonnance | Examen | Bon Manuel |
|---|---|---|---|---|
| CENTRE_HOSPITALIER | ✅ | ✅ | ✅ | ✅ |
| CENTRE_HOSPITALIER_DENTISTE | ✅ | ✅ | ✅ | ✅ |
| CENTRE_HOSPITALIER_OPTIQUE | ❌ | ❌ | ✅ | ✅ |
| CENTRE_HOSPITALIER_SIMPLE | ✅ | ✅ | ✅ | ✅ |
| LABORATOIRE | ❌ | ❌ | ✅ | ✅ |
| PHARMACIE | ❌ | ✅ | ❌ | ✅ |
| SERVICE_SANTE | ✅ | ✅ | ✅ | ✅ |
| SUP_ADMIN | ✅ | ✅ | ✅ | ✅ |

### Stockage Médias
Chemin : C:/biometry-media/{souscripteur}/{police}/{code_adherent}/

### SMS Provider
smsvas.com — user: info@zenitheinsurance.com

### Email SMTP
Host: secure.emailsrvr.com:587

### Nouvelles tables créées (Flyway)
- dbx45ty_bon_manuel
- dbx45ty_bon_manuel_ligne
- dbx45ty_media_prestation
- dbx45ty_notification

### Fonctionnalités prévues (non implémentées)
- AM-05: Hospitalisation (décision hiérarchique en attente)

### Endpoints principaux

#### Auth
- POST /auth/login
- GET /auth/me

#### Adhérent
- GET /adherents/{code}
- GET /adherents/{code}/ayants-droit

#### Prestataire
- GET /prestataires/{id}
- GET /prestataires/types-prestation
- GET /prestataires/categorie/{categorieId}

#### Visite
- POST /visites
- GET /visites/{id}
- GET /visites/code/{codeCourt}
- GET /visites/prestataire/{prestataireId}

#### Validation
- GET /validations/consultations/en-attente
- GET /validations/lignes/en-attente
- PUT /validations/consultations/{id}
- PUT /validations/lignes/{id}
- PUT /validations/consultations/{id}/encaisser

#### Bon Manuel
- POST /bons-manuels
- PUT /bons-manuels/{id}/confirmer/global
- PUT /bons-manuels/{id}/confirmer/detail
- PUT /bons-manuels/{id}/rejeter
- PUT /bons-manuels/{id}/encaisser

#### Media
- POST /capture/{codeCourt}  (URL mobile prestataire)
- POST /medias/visite/{visiteId}
- GET /medias/visite/{visiteId}
- GET /medias/{id}/telecharger

#### Notification
- POST /notifications/envoyer
- POST /notifications/sms-manuel
- GET /notifications/alertes/{destinataireId}
- GET /notifications/alertes/{destinataireId}/compteur
- PUT /notifications/{id}/lire
- PUT /notifications/alertes/{destinataireId}/lire-tout

#### Reporting
- GET /reporting/dashboard/ss/{employeId}?annee=2026
- GET /reporting/dashboard/prestataire/{prestataireId}?categorieId=&annee=
- GET /reporting/consommation/{codeAdherent}?annee=2026
