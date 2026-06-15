///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package service_administration_api.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.time.LocalDate;
//import java.util.List;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import service_administration_api.entite.pooltpv.Categorie_PoolTPV;
//import service_administration_api.entite.pooltpv.Civilite_PoolTPV;
//import service_administration_api.entite.pooltpv.Duree_PoolTPV;
//import service_administration_api.entite.pooltpv.Energie_PoolTPV;
//import service_administration_api.entite.pooltpv.Garantie_PoolTPV;
//import service_administration_api.entite.pooltpv.Genre_PoolTPV;
//import service_administration_api.entite.pooltpv.Police_PoolTPV;
//import service_administration_api.entite.pooltpv.Quittance_PoolTPV;
//import service_administration_api.entite.pooltpv.Risque_PoolTPV;
//import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.LoginRequest;
//import service_administration_api.repository.poolTPV.*;
//import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.*;
//
///**
// *
// * @author USER01
// */
//@Service
////@AllArgsConstructor
//@RequiredArgsConstructor
//public class PoolTPVServiceImpl implements PoolTPVService {
//
//    @Value("${app.url.pooltpv.login}")
//    private String url_login;
//    @Value("${app.login.pooltpv}")
//    private String username;
//    @Value("${ZENITHE_PASSWORD_POOLTPV}")
//    private String password;
//    @Value("${app.url.pooltpv.data}")
//    private String url_data_config;
//    @Value("${app.url.pooltpv.police}")
//    private String url_police;
//    @Value("${app.url.pooltpv.risque}")
//    private String url_risque;
//    @Value("${app.url.pooltpv.encaissement}")
//    private String url_encaissement;
//    private String token_final = null;
//    private final Duree_PoolTPVRepository duree_PoolTPVRepository;
//    private final Garantie_PoolTPVRepository garantie_PoolTPVRepository;
//    private final Genre_PoolTPVRepository  genre_PoolTPVRepository;
//    private final Civilite_PoolTPVRepository civilite_PoolTPVRepository;
//    private final Energie_PoolTPVRepository energie_PoolTPVRepository;
//    private final Categorie_PoolTPVRepository categorie_PoolTPVRepository;
//
//    @Override
//    public String JwtLoginPoolTpv() throws Exception {
//        // 1. Construire le body JSON
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername(username);
//        loginRequest.setPassword(password);
//
//        String requestBody = objectMapper.writeValueAsString(loginRequest);
//
//        // 2. Créer et envoyer la requête HTTP POST
//        HttpClient client = HttpClient.newHttpClient();
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url_login))
//                .header("Content-Type", "application/json;charset=UTF-8")
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        // Afficher la réponse brute
//        System.out.println("Réponse brute : " + response.body());
//
//        // 3. Lire le token avec objectReader.readValue
//        // ✅ Extraire uniquement le champ "token"
//        String token = objectMapper.readTree(response.body())
//                .get("token")
//                .asText();
//        this.token_final = token;
//
//        return token;
//    }
//
//    @Override
//    public List<Duree_PoolTPV> listeDurees(String duree) throws Exception {
//        // Récupérer un token si on n'en a pas encore
//        if (this.token_final == null) {
//            this.token_final = this.JwtLoginPoolTpv();
//        }
//
//        HttpResponse<String> response = envoyerRequete(this.token_final, duree);
//
//        // 401 → token expiré : on relogin et on réessaie UNE seule fois
//        if (response.statusCode() == 401) {
//            System.out.println("Token expiré, renouvellement en cours...");
//            this.token_final = this.JwtLoginPoolTpv();
//            response = envoyerRequete(this.token_final, duree);
//        }
//
//        // Si toujours 401 après relogin → identifiants invalides
//        if (response.statusCode() == 401) {
//            throw new RuntimeException("Authentification échouée : identifiants invalides.");
//        }
//
//        // Autres erreurs HTTP
//        if (response.statusCode() != 200) {
//            throw new RuntimeException("Erreur HTTP " + response.statusCode() + " : " + response.body());
//        }
//
//        // Mapper la réponse JSON → List<Duree_PoolTPV>
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<Duree_PoolTPV> listDuree = objectMapper.reader()
//                .forType(objectMapper.getTypeFactory()
//                        .constructCollectionType(List.class, Duree_PoolTPV.class))
//                .readValue(response.body());
//
//        return listDuree.stream()
//                .filter(d -> duree_PoolTPVRepository.existsByCode(d.getCode()) == Boolean.FALSE)
//                .map(dr -> duree_PoolTPVRepository.save(dr))
//                .toList();
//    }
//
//    @Override
//    public List<Genre_PoolTPV> listGenres(String genre) throws Exception {
//        // Récupérer un token si on n'en a pas encore
//        if (this.token_final == null) {
//            this.token_final = this.JwtLoginPoolTpv();
//        }
//
//        HttpResponse<String> response = envoyerRequete(this.token_final, genre);
//
//        // 401 → token expiré : on relogin et on réessaie UNE seule fois
//        if (response.statusCode() == 401) {
//            System.out.println("Token expiré, renouvellement en cours...");
//            this.token_final = this.JwtLoginPoolTpv();
//            response = envoyerRequete(this.token_final, genre);
//        }
//
//        // Si toujours 401 après relogin → identifiants invalides
//        if (response.statusCode() == 401) {
//            throw new RuntimeException("Authentification échouée : identifiants invalides.");
//        }
//
//        // Autres erreurs HTTP
//        if (response.statusCode() != 200) {
//            throw new RuntimeException("Erreur HTTP " + response.statusCode() + " : " + response.body());
//        }
//
//        // Mapper la réponse JSON → List<Duree_PoolTPV>
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<Genre_PoolTPV> listDuree = objectMapper.reader()
//                .forType(objectMapper.getTypeFactory()
//                        .constructCollectionType(List.class, Genre_PoolTPV.class))
//                .readValue(response.body());
//
//        return listDuree.stream()
//                .filter(d -> genre_PoolTPVRepository.existsByCode(d.getCode()) == Boolean.FALSE)
//                .map(dr -> genre_PoolTPVRepository.save(dr))
//                .toList();
//    
//    }
//
//    @Override
//    public List<Categorie_PoolTPV> listCategorie(String categorie) throws Exception {
//    // Récupérer un token si on n'en a pas encore
//        if (this.token_final == null) {
//            this.token_final = this.JwtLoginPoolTpv();
//        }
//
//        HttpResponse<String> response = envoyerRequete(this.token_final, categorie);
//
//        // 401 → token expiré : on relogin et on réessaie UNE seule fois
//        if (response.statusCode() == 401) {
//            System.out.println("Token expiré, renouvellement en cours...");
//            this.token_final = this.JwtLoginPoolTpv();
//            response = envoyerRequete(this.token_final, categorie);
//        }
//
//        // Si toujours 401 après relogin → identifiants invalides
//        if (response.statusCode() == 401) {
//            throw new RuntimeException("Authentification échouée : identifiants invalides.");
//        }
//
//        // Autres erreurs HTTP
//        if (response.statusCode() != 200) {
//            throw new RuntimeException("Erreur HTTP " + response.statusCode() + " : " + response.body());
//        }
//
//        // Mapper la réponse JSON → List<Duree_PoolTPV>
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<Categorie_PoolTPV> listDuree = objectMapper.reader()
//                .forType(objectMapper.getTypeFactory()
//                        .constructCollectionType(List.class, Categorie_PoolTPV.class))
//                .readValue(response.body());
//
//        return listDuree.stream()
//                .filter(d -> categorie_PoolTPVRepository.existsByCode(d.getCode()) == Boolean.FALSE)
//                .map(dr -> categorie_PoolTPVRepository.save(dr))
//                .toList();
//    
//    }
//
//    @Override
//    public List<Garantie_PoolTPV> listGaranties(String garantie) throws Exception {
//      // Récupérer un token si on n'en a pas encore
//        if (this.token_final == null) {
//            this.token_final = this.JwtLoginPoolTpv();
//        }
//
//        HttpResponse<String> response = envoyerRequete(this.token_final, garantie);
//
//        // 401 → token expiré : on relogin et on réessaie UNE seule fois
//        if (response.statusCode() == 401) {
//            System.out.println("Token expiré, renouvellement en cours...");
//            this.token_final = this.JwtLoginPoolTpv();
//            response = envoyerRequete(this.token_final, garantie);
//        }
//
//        // Si toujours 401 après relogin → identifiants invalides
//        if (response.statusCode() == 401) {
//            throw new RuntimeException("Authentification échouée : identifiants invalides.");
//        }
//
//        // Autres erreurs HTTP
//        if (response.statusCode() != 200) {
//            throw new RuntimeException("Erreur HTTP " + response.statusCode() + " : " + response.body());
//        }
//
//        // Mapper la réponse JSON → List<Duree_PoolTPV>
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<Garantie_PoolTPV> listDuree = objectMapper.reader()
//                .forType(objectMapper.getTypeFactory()
//                        .constructCollectionType(List.class, Garantie_PoolTPV.class))
//                .readValue(response.body());
//
//        return listDuree.stream()
//                .filter(d -> garantie_PoolTPVRepository.existsByCode(d.getCode()) == Boolean.FALSE)
//                .map(dr -> garantie_PoolTPVRepository.save(dr))
//                .toList();
//    
//    }
//
//    @Override
//    public List<Civilite_PoolTPV> listCivilite(String civilite) throws Exception {
//      // Récupérer un token si on n'en a pas encore
//        if (this.token_final == null) {
//            this.token_final = this.JwtLoginPoolTpv();
//        }
//
//        HttpResponse<String> response = envoyerRequete(this.token_final, civilite);
//
//        // 401 → token expiré : on relogin et on réessaie UNE seule fois
//        if (response.statusCode() == 401) {
//            System.out.println("Token expiré, renouvellement en cours...");
//            this.token_final = this.JwtLoginPoolTpv();
//            response = envoyerRequete(this.token_final, civilite);
//        }
//
//        // Si toujours 401 après relogin → identifiants invalides
//        if (response.statusCode() == 401) {
//            throw new RuntimeException("Authentification échouée : identifiants invalides.");
//        }
//
//        // Autres erreurs HTTP
//        if (response.statusCode() != 200) {
//            throw new RuntimeException("Erreur HTTP " + response.statusCode() + " : " + response.body());
//        }
//
//        // Mapper la réponse JSON → List<Duree_PoolTPV>
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<Civilite_PoolTPV> listDuree = objectMapper.reader()
//                .forType(objectMapper.getTypeFactory()
//                        .constructCollectionType(List.class, Civilite_PoolTPV.class))
//                .readValue(response.body());
//
//        return listDuree.stream()
//                .filter(d -> civilite_PoolTPVRepository.existsByCode(d.getCode()) == Boolean.FALSE)
//                .map(dr -> civilite_PoolTPVRepository.save(dr))
//                .toList();
//    
//    }
//
//    @Override
//    public List<Energie_PoolTPV> listEnergie_PoolTPVs(String energie) throws Exception {
//       // Récupérer un token si on n'en a pas encore
//        if (this.token_final == null) {
//            this.token_final = this.JwtLoginPoolTpv();
//        }
//
//        HttpResponse<String> response = envoyerRequete(this.token_final, energie);
//
//        // 401 → token expiré : on relogin et on réessaie UNE seule fois
//        if (response.statusCode() == 401) {
//            System.out.println("Token expiré, renouvellement en cours...");
//            this.token_final = this.JwtLoginPoolTpv();
//            response = envoyerRequete(this.token_final, energie);
//        }
//
//        // Si toujours 401 après relogin → identifiants invalides
//        if (response.statusCode() == 401) {
//            throw new RuntimeException("Authentification échouée : identifiants invalides.");
//        }
//
//        // Autres erreurs HTTP
//        if (response.statusCode() != 200) {
//            throw new RuntimeException("Erreur HTTP " + response.statusCode() + " : " + response.body());
//        }
//
//        // Mapper la réponse JSON → List<Duree_PoolTPV>
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<Energie_PoolTPV> listDuree = objectMapper.reader()
//                .forType(objectMapper.getTypeFactory()
//                        .constructCollectionType(List.class, Energie_PoolTPV.class))
//                .readValue(response.body());
//
//        return listDuree.stream()
//                .filter(d -> energie_PoolTPVRepository.existsByCode(d.getCode()) == Boolean.FALSE)
//                .map(dr -> energie_PoolTPVRepository.save(dr))
//                .toList();
//    }
//
//    @Override
//    public Police_PoolTPV findPoliceByApiPoolTPV(String code_demandeur, LocalDate date_debut, LocalDate date_fin) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    @Override
//    public ReferenceAutoPageDto reference_vehicule(String reference_vehicule)  throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    @Override
//    public List<Risque_PoolTPV> listRisqueePooLTPV(String code_demandeur, LocalDate date_debut, LocalDate date_fin) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    @Override
//    public List<Quittance_PoolTPV> listEncaissementPooLTPV(String code_demandeur, LocalDate date_debut, LocalDate date_fin) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    // ─── Envoi de la requête GET ──────────────────────────────────────────────
//    private HttpResponse<String> envoyerRequete(String bearerToken, String libelle) throws Exception {
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(this.url_data_config + "" + libelle))
//                .header("Content-Type", "application/json;charset=UTF-8")
//                .header("Authorization", "Bearer " + bearerToken)
//                .GET()
//                .build();
//
//        return client.send(request, HttpResponse.BodyHandlers.ofString());
//    }
//}
package service_administration_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.LoginRequest;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.ReferenceAutoPageDto;
import service_administration_api.entite.pooltpv.*;
import service_administration_api.repository.poolTPV.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoolTPVServiceImpl implements PoolTPVService {

    // ─── Config ───────────────────────────────────────────────────────────────
    @Value("${app.url.pooltpv.login}")      private String url_login;
    @Value("${app.login.pooltpv}")          private String username;
    @Value("${ZENITHE_PASSWORD_POOLTPV}")   private String password;
    @Value("${app.url.pooltpv.data}")       private String url_data_config;
    @Value("${app.url.pooltpv.police}")     private String url_police;
    @Value("${app.url.pooltpv.risque}")     private String url_risque;
    @Value("${app.url.pooltpv.encaissement}") private String url_encaissement;

    // ─── Repositories ─────────────────────────────────────────────────────────
    private final Duree_PoolTPVRepository     duree_PoolTPVRepository;
    private final Garantie_PoolTPVRepository  garantie_PoolTPVRepository;
    private final Genre_PoolTPVRepository     genre_PoolTPVRepository;
    private final Civilite_PoolTPVRepository  civilite_PoolTPVRepository;
    private final Energie_PoolTPVRepository   energie_PoolTPVRepository;
    private final Categorie_PoolTPVRepository categorie_PoolTPVRepository;

    // ─── State ────────────────────────────────────────────────────────────────
    private String token_final = null;

    // HttpClient unique et réutilisable (avec timeout)
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ─── Login ────────────────────────────────────────────────────────────────
    @Override
    public String JwtLoginPoolTpv() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        String requestBody = objectMapper.writeValueAsString(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url_login))
                .header("Content-Type", "application/json;charset=UTF-8")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Login échoué [" + response.statusCode() + "] : " + response.body());
        }

        log.info("Login PoolTPV réussi");
        this.token_final = objectMapper.readTree(response.body()).get("token").asText();
        return this.token_final;
    }

    // ─── Méthodes de liste (refactorisées) ────────────────────────────────────
    @Override
    public List<Duree_PoolTPV> listeDurees(String duree) throws Exception {
        return fetchAndSave(
                duree,
                Duree_PoolTPV.class,
                d -> !duree_PoolTPVRepository.existsByCode(d.getCode()),
                duree_PoolTPVRepository::save
        );
    }

    @Override
    public List<Genre_PoolTPV> listGenres(String genre) throws Exception {
        return fetchAndSave(
                genre,
                Genre_PoolTPV.class,
                d -> !genre_PoolTPVRepository.existsByCode(d.getCode()),
                genre_PoolTPVRepository::save
        );
    }

    @Override
    public List<Categorie_PoolTPV> listCategorie(String categorie) throws Exception {
        return fetchAndSave(
                categorie,
                Categorie_PoolTPV.class,
                d -> !categorie_PoolTPVRepository.existsByCode(d.getCode()),
                categorie_PoolTPVRepository::save
        );
    }

    @Override
    public List<Garantie_PoolTPV> listGaranties(String garantie) throws Exception {
        return fetchAndSave(
                garantie,
                Garantie_PoolTPV.class,
                d -> !garantie_PoolTPVRepository.existsByCode(d.getCode()),
                garantie_PoolTPVRepository::save
        );
    }

    @Override
    public List<Civilite_PoolTPV> listCivilite(String civilite) throws Exception {
        return fetchAndSave(
                civilite,
                Civilite_PoolTPV.class,
                d -> !civilite_PoolTPVRepository.existsByCode(d.getCode()),
                civilite_PoolTPVRepository::save
        );
    }

    @Override
    public List<Energie_PoolTPV> listEnergie_PoolTPVs(String energie) throws Exception {
        return fetchAndSave(
                energie,
                Energie_PoolTPV.class,
                d -> !energie_PoolTPVRepository.existsByCode(d.getCode()),
                energie_PoolTPVRepository::save
        );
    }

    // ─── Méthodes non implémentées ────────────────────────────────────────────
    @Override
    public Police_PoolTPV findPoliceByApiPoolTPV(String code_demandeur, LocalDate date_debut, LocalDate date_fin) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ReferenceAutoPageDto reference_vehicule(String reference_vehicule) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Risque_PoolTPV> listRisqueePooLTPV(String code_demandeur, LocalDate date_debut, LocalDate date_fin) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Quittance_PoolTPV> listEncaissementPooLTPV(String code_demandeur, LocalDate date_debut, LocalDate date_fin) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // ─── Méthode générique — remplace les 6 méthodes dupliquées ──────────────
    private <T> List<T> fetchAndSave(
            String endpoint,
            Class<T> type,
            java.util.function.Predicate<T> filtreDoublon,
            Function<T, T> sauvegarder
    ) throws Exception {

        // 1. Authentification si pas encore de token
        if (this.token_final == null) {
            this.token_final = this.JwtLoginPoolTpv();
        }

        // 2. Premier appel
        HttpResponse<String> response = envoyerRequete(this.token_final, endpoint);

        // 3. Token expiré → relogin + réessai
        if (response.statusCode() == 401) {
            log.warn("Token expiré pour [{}], renouvellement...", endpoint);
            this.token_final = this.JwtLoginPoolTpv();
            response = envoyerRequete(this.token_final, endpoint);
        }

        // 4. Toujours 401 → identifiants invalides
        if (response.statusCode() == 401) {
            throw new RuntimeException("Authentification échouée : identifiants invalides.");
        }

        // 5. Autres erreurs HTTP
        if (response.statusCode() != 200) {
            throw new RuntimeException("Erreur HTTP " + response.statusCode() + " : " + response.body());
        }

        // 6. Désérialisation JSON → List<T>
        List<T> liste = objectMapper.readValue(
                response.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, type)
        );

        log.info("[{}] {} éléments reçus", endpoint, liste.size());

        // 7. Filtrer les doublons et sauvegarder
        return liste.stream()
                .filter(filtreDoublon)
                .map(sauvegarder)
                .toList();
    }

    // ─── Envoi requête GET avec Bearer token ──────────────────────────────────
    private HttpResponse<String> envoyerRequete(String bearerToken, String libelle) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.url_data_config + libelle))
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Authorization", "Bearer " + bearerToken)
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}