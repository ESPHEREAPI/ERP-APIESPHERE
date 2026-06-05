# ESPHERE BIOMETRY — Instructions Code Source Complet

Ce ZIP contient la structure Maven du projet ESPHERE Biometry.
Le code source complet de chaque service a été développé et validé
en session collaborative. Voici où trouver chaque fichier.

## Récupération du code complet

Le code complet de chaque service est disponible dans la conversation
de développement. Chaque service a été développé avec les fichiers suivants :

---

## AUTH SERVICE (8081)
Fichiers validés en production :
- EsphereAuthApplication.java ✅
- CryptoLegacy.java (SHA1+MD5) ✅
- JwtProvider.java ✅
- AuthController.java (/auth/login, /auth/me) ✅
- AuthService.java ✅
- Employe.java, Utilisateur.java, Profil.java, Permission.java, Menu.java ✅
- AuthRequest.java, AuthResponse.java, MenuResponse.java ✅
- SecurityConfig.java ✅
- Algorithme login: SHA1( MD5("RS_" + password + "-er") )

## ADHERENT SERVICE (8082)
- Adherent.java (table: dbx45ty_adherent) ✅
  Champs: codeAdherent(PK), assurePrincipal, naissance, sexe, matricule,
          telephone, taux(Double), plafondAssurep(Double), consAp(Double),
          ville, souscripteur, police, effetPolice, echeancePolice,
          groupe(Short), enrole, dateEnrole, imprime, statut
- AyantDroit.java (table: dbx45ty_ayant_droit) ✅
  Champs: codeAyantDroit(PK), codeAdherent(FK), nom, sexe, naissance,
          lienPare, telephone, police, enrole, dateEnrole, statut
- AdherentController.java ✅
- AdherentService.java ✅
- AdherentRepository.java ✅
- AyantDroitRepository.java ✅

## PRESTATAIRE SERVICE (8083)
- Prestataire.java (table: dbx45ty_prestataire) ✅
  Champs: id(PK), categorie_id(FK), ville_id(Integer), nom, adresse,
          email, telephone, registre, logo, statut, supprime
- CategoriePrestataire.java ✅
- TypePrestation.java (table: dbx45ty_type_prestation) ✅
  Champs: id(PK varchar5), nom, affiche(Integer), categorie
- TauxPrestation.java (table: dbx45ty_taux_prestation) ✅
  Champs: id(PK auto), typePrestationId(FK), police, groupe(Short), taux(Integer), plafond(Float)
- PrestataireController.java ✅
- PrestataireService.java ✅

## VISITE SERVICE (8084)
- Visite.java (table: dbx45ty_visite) ✅
  Champs: id(PK varchar), codeAdherent, codeAyantDroit, prestataireId,
          employeId(Integer), codeCourt, telephone, date
- Consultation.java (table: dbx45ty_consultation) ✅
  Champs: id(Integer auto), visiteId, employeValideRejeteId, taux(Double),
          typeConsultation, natureConsultation, natureAffection,
          montant(Double), montantModif(Double), date, dateValideRejete,
          observations, etatConsultation, supprime
- Prestation.java (table: dbx45ty_prestation) ✅
- LignePrestation.java (table: dbx45ty_ligne_prestation) ✅
  actePrelevementModif = 0.0 (NOT NULL default 0)
- Format ID visite: {ANNEE}_{PRESTATAIRE_ID}_{CODE_COURT}
- Code court: 6 chars alphanumériques UUID

## VALIDATION SERVICE (8085)
- Copies locales: Consultation, LignePrestation, Visite, Prestation
- ValidationService.java ✅
  - validerConsultation() / rejeterConsultation()
  - validerLigne() / rejeterLigne()
  - encaisserConsultation() / encaisserLigne()
- Etats consultation: attente_validation | valide | rejete | encaisse
- Etats ligne: enregistre | attente_validation | valide | rejete | encaisse

## BON MANUEL SERVICE (8086)
Nouvelles tables créées par Flyway:
- dbx45ty_bon_manuel (V1)
- dbx45ty_bon_manuel_ligne (V1)
- BonManuel.java, BonManuelLigne.java ✅
- Référence: BM-{ANNEE}-{PRESTATAIRE_ID}-{SEQ}
- Statuts: en_attente | confirme | encaisse | rejete
- type_validation: global | detail
- BonManuelService.java ✅
  - creer(), confirmerGlobal(), confirmerDetail(), rejeter(), encaisser()

## MEDIA SERVICE (8087)
Nouvelle table: dbx45ty_media_prestation (V1)
- MediaPrestation.java ✅
  demandeParSs: Boolean (TINYINT(1) en base)
- Stockage: C:/biometry-media/{souscripteur}/{police}/{code_adherent}/
- Types: image(10MB) | document(20MB) | video(100MB) | autre(10MB)
- Extensions image: jpg, jpeg, png, gif, webp
- Extensions document: pdf
- Extensions video: mp4, avi, mov, mkv, webm
- URL mobile: POST /capture/{codeCourt}
- URL interne: POST /medias/visite/{visiteId}
- Appels inter-services: Visite Service + Adherent Service

## NOTIFICATION SERVICE (8088)
Nouvelle table: dbx45ty_notification (V1)
- Notification.java ✅
  lu: Boolean (TINYINT(1) en base)
- SmsService.java (smsvas.com) ✅
  Format tel Cameroun: ajouter "237" si commence par 6 ou 2
- EmailService.java (secure.emailsrvr.com:587) ✅
- NotificationService.java ✅
  - envoyerAuto() / envoyerSmsManuel() / envoyer()
  - getAlertes() / getAlerteNonLues() / compterNonLues()
  - marquerCommeLu() / marquerToutesCommeLues()
- event_type: prestation_validee | prestation_rejetee | bon_confirme |
              bon_rejete | prestation_soumise | bon_cree | video_uploadee | manuel

## REPORTING SERVICE (8089)
- DroitsAcces.java (matrice des droits par catégorie) ✅
- ReportingRepository.java (requêtes natives MySQL) ✅
- ReportingService.java ✅
- Endpoints:
  - GET /reporting/dashboard/ss/{employeId}?annee=
  - GET /reporting/dashboard/prestataire/{prestataireId}?categorieId=&annee=
  - GET /reporting/consommation/{codeAdherent}?annee=

## API GATEWAY (8080)
- JwtAuthFilter.java (GlobalFilter, Order=-1) ✅
- Endpoints publics: /auth/login, /auth/health, /capture/, /actuator
- Headers injectés: X-User-Id, X-User-Login, X-Profil-Code, X-Prestataire-Id
- JWT secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

## Matrice droits prestataire

| Catégorie | Consultation | Ordonnance | Examen | Bon Manuel |
|---|---|---|---|---|
| CENTRE_HOSPITALIER | true | true | true | true |
| CENTRE_HOSPITALIER_DENTISTE | true | true | true | true |
| CENTRE_HOSPITALIER_OPTIQUE | false | false | true | true |
| CENTRE_HOSPITALIER_SIMPLE | true | true | true | true |
| LABORATOIRE | false | false | true | true |
| PHARMACIE | false | true | false | true |
| SERVICE_SANTE | true | true | true | true |
| SUP_ADMIN | true | true | true | true |

## Conventions importantes
- statut actif: '1' | inactif: '-1'
- supprime non supprimé: '-1' | supprimé: '1'
- SMALLINT → Short en Java (groupe, position, numeroOrdre, langueDefaut)
- TINYINT(1) → Boolean en Java (lu, demandeParSs)
