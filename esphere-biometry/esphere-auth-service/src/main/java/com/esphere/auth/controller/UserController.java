package com.esphere.auth.controller;

import com.esphere.auth.dto.request.CreateEmployeRequest;
import com.esphere.auth.dto.response.UserInfoResponse;
import com.esphere.auth.entity.Employe;
import com.esphere.auth.entity.Profil;
import com.esphere.auth.entity.Utilisateur;
import com.esphere.auth.exception.AuthException;
import com.esphere.auth.repository.EmployeRepository;
import com.esphere.auth.repository.ProfilRepository;
import com.esphere.auth.repository.UtilisateurRepository;
import com.esphere.auth.security.CryptoLegacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller de gestion des utilisateurs.
 * Accessible via : GET /auth/users, POST /auth/users, etc.
 *
 * Sécurité : seul SUP_ADMIN peut gérer les utilisateurs.
 */
@Slf4j
@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class UserController {

    private final UtilisateurRepository utilisateurRepository;
    private final EmployeRepository     employeRepository;
    private final ProfilRepository      profilRepository;

    // ── Liste paginée ─────────────────────────────────────────
    /**
     * GET /auth/users?page=0&size=10&search=dupont
     * Retourne la liste paginée des utilisateurs actifs.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)    String search) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Utilisateur> utilisateurs;
        if (search != null && !search.isBlank()) {
            utilisateurs = utilisateurRepository.searchNonSupprimes(search, pageable);
        } else {
            utilisateurs = utilisateurRepository.findAllNonSupprimes(pageable);
        }

        Page<UserInfoResponse> dtos = utilisateurs.map(this::toResponse);

        return ResponseEntity.ok(Map.of(
            "data",         dtos.getContent(),
            "total",        dtos.getTotalElements(),
            "totalPages",   dtos.getTotalPages(),
            "currentPage",  dtos.getNumber()
        ));
    }

    // ── Détail utilisateur ────────────────────────────────────
    /**
     * GET /auth/users/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<UserInfoResponse> getUserById(@PathVariable Integer id) {
        return utilisateurRepository.findById(id)
            .map(u -> ResponseEntity.ok(toResponse(u)))
            .orElse(ResponseEntity.notFound().build());
    }

    // ── Activation ────────────────────────────────────────────
    /**
     * PATCH /auth/users/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<UserInfoResponse> activate(@PathVariable Integer id) {
        return utilisateurRepository.findById(id).map(u -> {
            u.setStatut("1");
            u.setSupprime("-1");
            utilisateurRepository.save(u);
            log.info("Utilisateur {} activé", u.getLogin());
            return ResponseEntity.ok(toResponse(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Désactivation ─────────────────────────────────────────
    /**
     * PATCH /auth/users/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<UserInfoResponse> deactivate(@PathVariable Integer id) {
        return utilisateurRepository.findById(id).map(u -> {
            u.setStatut("0");
            utilisateurRepository.save(u);
            log.info("Utilisateur {} désactivé", u.getLogin());
            return ResponseEntity.ok(toResponse(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Suppression logique ───────────────────────────────────
    /**
     * DELETE /auth/users/{id}
     * Suppression logique (supprime = '1', statut = '0')
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return utilisateurRepository.findById(id).map(u -> {
            u.setSupprime("1");
            u.setStatut("0");
            utilisateurRepository.save(u);
            log.info("Utilisateur {} supprimé (logique)", u.getLogin());
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Création employé ────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<UserInfoResponse> create(@RequestBody CreateEmployeRequest req) {
        if (req.getNom() == null || req.getNom().isBlank())
            throw new AuthException("Le nom est obligatoire");
        if (req.getEmail() == null || req.getEmail().isBlank())
            throw new AuthException("L'email est obligatoire");
        if (req.getLogin() == null || req.getLogin().isBlank())
            throw new AuthException("Le login est obligatoire");
        if (req.getMotPasse() == null || req.getMotPasse().isBlank())
            throw new AuthException("Le mot de passe est obligatoire");
        if (req.getProfilId() == null)
            throw new AuthException("Le profil est obligatoire");

        if (utilisateurRepository.findByLogin(req.getLogin()).isPresent())
            throw new AuthException("Ce login existe déjà : " + req.getLogin());
        if (utilisateurRepository.findByEmail(req.getEmail()).isPresent())
            throw new AuthException("Cet email existe déjà : " + req.getEmail());

        Profil profil = profilRepository.findById(req.getProfilId())
                .orElseThrow(() -> new AuthException("Profil introuvable"));

        Utilisateur u = Utilisateur.builder()
                .nom(req.getNom())
                .prenom(req.getPrenom())
                .genre(req.getGenre())
                .email(req.getEmail())
                .login(req.getLogin())
                .motPasse(CryptoLegacy.loginBiometrie(req.getMotPasse()))
                .telephone(req.getTelephone())
                .langueDefaut(req.getLangueDefaut() != null ? req.getLangueDefaut() : (short) 2)
                .type("1")
                .statut("1")
                .supprime("-1")
                .dateCreation(LocalDateTime.now())
                .build();
        utilisateurRepository.save(u);

        Employe e = Employe.builder()
                .utilisateur(u)
                .profil(profil)
                .prestataireId(req.getPrestataireId())
                .connexionAppli(req.getConnexionAppli() != null ? req.getConnexionAppli() : "1")
                .serialBiometrie(req.getSerialBiometrie())
                .build();
        employeRepository.save(e);

        u.setEmploye(e);
        log.info("Employé créé : {} (profil={})", u.getLogin(), profil.getCode());
        return ResponseEntity.ok(toResponse(u));
    }

    // ── Modification employé ─────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<UserInfoResponse> update(@PathVariable Integer id,
                                                    @RequestBody CreateEmployeRequest req) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new AuthException("Utilisateur introuvable"));

        if (req.getNom() != null)      u.setNom(req.getNom());
        if (req.getPrenom() != null)    u.setPrenom(req.getPrenom());
        if (req.getGenre() != null)     u.setGenre(req.getGenre());
        if (req.getTelephone() != null) u.setTelephone(req.getTelephone());
        if (req.getLangueDefaut() != null) u.setLangueDefaut(req.getLangueDefaut());

        if (req.getEmail() != null && !req.getEmail().equals(u.getEmail())) {
            if (utilisateurRepository.findByEmail(req.getEmail()).isPresent())
                throw new AuthException("Cet email existe déjà : " + req.getEmail());
            u.setEmail(req.getEmail());
        }

        if (req.getMotPasse() != null && !req.getMotPasse().isBlank()) {
            u.setMotPasse(CryptoLegacy.loginBiometrie(req.getMotPasse()));
        }

        utilisateurRepository.save(u);

        Employe e = u.getEmploye();
        if (e == null) {
            e = employeRepository.findByUtilisateurId(u.getId()).orElse(null);
        }
        if (e != null) {
            if (req.getProfilId() != null) {
                Profil profil = profilRepository.findById(req.getProfilId())
                        .orElseThrow(() -> new AuthException("Profil introuvable"));
                e.setProfil(profil);
            }
            if (req.getPrestataireId() != null) e.setPrestataireId(req.getPrestataireId());
            if (req.getConnexionAppli() != null) e.setConnexionAppli(req.getConnexionAppli());
            if (req.getSerialBiometrie() != null) e.setSerialBiometrie(req.getSerialBiometrie());
            employeRepository.save(e);
            u.setEmploye(e);
        }

        log.info("Employé modifié : {}", u.getLogin());
        return ResponseEntity.ok(toResponse(u));
    }

    // ── Réinitialisation mot de passe ─────────────────────────
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable Integer id) {
        return utilisateurRepository.findById(id).map(u -> {
            String tempPassword = "Esphere@" + (1000 + (int)(Math.random() * 9000));
            u.setMotPasse(CryptoLegacy.loginBiometrie(tempPassword));
            utilisateurRepository.save(u);
            log.info("Mot de passe réinitialisé pour : {}", u.getLogin());
            return ResponseEntity.ok(Map.of("temporaryPassword", tempPassword));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Langue d'un prestataire ─────────────────────────────────
    @GetMapping("/prestataire/{prestataireId}/langue")
    public ResponseEntity<Map<String, Object>> getLanguePrestataire(@PathVariable String prestataireId) {
        List<Employe> employes = employeRepository.findByPrestataireId(prestataireId);
        if (employes.isEmpty()) {
            return ResponseEntity.ok(Map.of("langue", 2));
        }
        Employe emp = employes.get(0);
        Short langue = emp.getUtilisateur() != null ? emp.getUtilisateur().getLangueDefaut() : null;
        return ResponseEntity.ok(Map.of("langue", langue != null ? langue : 2));
    }

    // ── Agents SS actifs (pour notifications) ─────────────────
    @GetMapping("/agents-ss")
    public ResponseEntity<List<Map<String, Object>>> getAgentsSS() {
        List<Profil> profilsSS = profilRepository.findByCode("SERVICE_SANTE")
                .map(List::of).orElse(List.of());
        // Aussi SUP_ADMIN
        profilRepository.findByCode("SUP_ADMIN").ifPresent(profilsSS::add);

        List<Map<String, Object>> agents = new java.util.ArrayList<>();
        for (Profil profil : profilsSS) {
            employeRepository.findByProfilIdAndStatut(profil.getId()).forEach(emp -> {
                Utilisateur u = emp.getUtilisateur();
                if (u != null && "1".equals(u.getStatut()) && "-1".equals(u.getSupprime())
                        && u.getTelephone() != null && !u.getTelephone().isBlank()) {
                    agents.add(Map.of(
                        "telephone", u.getTelephone(),
                        "langue", u.getLangueDefaut() != null ? u.getLangueDefaut() : 2,
                        "nom", u.getNom() != null ? u.getNom() : ""
                    ));
                }
            });
        }
        return ResponseEntity.ok(agents);
    }

    // ── Mapping entité → DTO ──────────────────────────────────
    private UserInfoResponse toResponse(Utilisateur u) {
        Employe employe = u.getEmploye();
        return UserInfoResponse.builder()
            .id(u.getId())
            .login(u.getLogin())
            .nom(u.getNom())
            .prenom(u.getPrenom())
            .email(u.getEmail())
            .genre(u.getGenre())
            .telephone(u.getTelephone())
            .statut(u.getStatut())
            .profilCode(employe != null ? employe.getProfil().getCode() : null)
            .profilLibelle(employe != null ? employe.getProfil().getTypeProfil() : null)
            .connexionAppli(employe != null ? employe.getConnexionAppli() : null)
            .prestataireId(employe != null ? employe.getPrestataireId() : null)
            .profilId(employe != null ? employe.getProfil().getId() : null)
            .langueDefaut(u.getLangueDefaut())
            .serialBiometrie(employe != null ? employe.getSerialBiometrie() : null)
            .build();
    }
}
