package com.esphere.auth.service;

import com.esphere.auth.dto.request.LoginRequest;
import com.esphere.auth.dto.request.OtpRequest;
import com.esphere.auth.dto.response.LoginResponse;
import com.esphere.auth.dto.response.MenuResponse;
import com.esphere.auth.dto.response.OtpResponse;
import com.esphere.auth.dto.response.UserInfoResponse;
import com.esphere.auth.entity.Employe;
import com.esphere.auth.entity.Menu;
import com.esphere.auth.entity.Permission;
import com.esphere.auth.entity.Utilisateur;
import com.esphere.auth.exception.AuthException;
import com.esphere.auth.repository.PermissionRepository;
import com.esphere.auth.repository.UtilisateurRepository;
import com.esphere.auth.security.CryptoLegacy;
import com.esphere.auth.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PermissionRepository  permissionRepository;
    private final JwtProvider           jwtProvider;
    private final OtpStore              otpStore;

    @Value("${esphere.jwt.expiration}")
    private long jwtExpiration;

    // ── LOGIN ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        Utilisateur utilisateur = utilisateurRepository
            .findActiveByLogin(request.getLogin())
            .orElseThrow(() -> {
                log.warn("Login inconnu : {}",
                         request.getLogin());
                return new AuthException(
                    "Login ou mot de passe incorrect");
            });

        if (!CryptoLegacy.matches(request.getPassword(),
                                   utilisateur.getMotPasse())) {
            log.warn("Mot de passe incorrect : {}",
                     request.getLogin());
            throw new AuthException(
                "Login ou mot de passe incorrect");
        }

        Employe employe = utilisateur.getEmploye();
        if (employe == null) {
            throw new AuthException(
                "Compte non configuré. Contactez l'administrateur.");
        }

        String profilCode     = employe.getProfil().getCode();
        String prestataireId  = employe.getPrestataireId();
        String connexionAppli = employe.getConnexionAppli();

        List<MenuResponse> menus =
            chargerMenus(employe.getProfil().getId(), profilCode);

        String token = jwtProvider.generateToken(
            utilisateur.getId(),
            utilisateur.getLogin(),
            profilCode,
            prestataireId,
            connexionAppli
        );

        log.info("Connexion réussie : {} (profil: {})",
                 utilisateur.getLogin(), profilCode);

        return buildLoginResponse(
            utilisateur, employe, profilCode, token, menus);
    }

    // ── ME ────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public UserInfoResponse getMe(String login) {
        Utilisateur utilisateur = utilisateurRepository
            .findActiveByLogin(login)
            .orElseThrow(() ->
                new AuthException("Utilisateur introuvable"));

        Employe employe = utilisateur.getEmploye();

        return UserInfoResponse.builder()
            .id(utilisateur.getId())
            .login(utilisateur.getLogin())
            .nom(utilisateur.getNom())
            .prenom(utilisateur.getPrenom())
            .email(utilisateur.getEmail())
            .genre(utilisateur.getGenre())
            .telephone(utilisateur.getTelephone())
            .statut(utilisateur.getStatut())
            .profilCode(employe != null
                ? employe.getProfil().getCode() : null)
            .profilLibelle(employe != null
                ? employe.getProfil().getTypeProfil() : null)
            .connexionAppli(employe != null
                ? employe.getConnexionAppli() : null)
            .prestataireId(employe != null
                ? employe.getPrestataireId() : null)
            .build();
    }

    // ── REFRESH TOKEN ─────────────────────────────────────

    @Transactional(readOnly = true)
    public LoginResponse refreshToken(String oldToken) {

        if (!jwtProvider.validateTokenIgnoreExpiry(oldToken)) {
            throw new AuthException("Token invalide");
        }

        String login = jwtProvider.getLoginIgnoreExpiry(oldToken);

        Utilisateur utilisateur = utilisateurRepository
            .findActiveByLogin(login)
            .orElseThrow(() ->
                new AuthException("Utilisateur introuvable"));

        Employe employe = utilisateur.getEmploye();
        if (employe == null) {
            throw new AuthException("Compte non configuré.");
        }

        String profilCode = employe.getProfil().getCode();

        String newToken = jwtProvider.generateToken(
            utilisateur.getId(),
            login,
            profilCode,
            employe.getPrestataireId(),
            employe.getConnexionAppli()
        );

        List<MenuResponse> menus =
            chargerMenus(employe.getProfil().getId(), profilCode);

        log.info("Token rafraîchi pour : {}", login);

        return buildLoginResponse(
            utilisateur, employe, profilCode, newToken, menus);
    }

    // ── GENERATE OTP ──────────────────────────────────────

    @Transactional(readOnly = true)
    public OtpResponse generateOtp(OtpRequest request) {

        // 1. Charger le prestataire (plusieurs comptes possibles → premier actif)
        Utilisateur utilisateur = utilisateurRepository
            .findActiveByPrestataireId(request.getPrestataireId())
            .stream().findFirst()
            .orElseThrow(() -> {
                log.warn("Prestataire inconnu : {}",
                         request.getPrestataireId());
                return new AuthException("Accès refusé");
            });

        Employe employe = utilisateur.getEmploye();

        // 2. Vérifier le serial du lecteur biométrique
        String serialEnBase = employe.getSerialBiometrie();

        if (serialEnBase == null || serialEnBase.isBlank()) {
            log.error(
                "Prestataire {} sans serial biométrie en base",
                request.getPrestataireId());
            throw new AuthException(
                "Équipement biométrie non configuré. " +
                "Contactez l'administrateur ESPHERE.");
        }

        String serialRecu  = request.getSerial() != null
            ? request.getSerial().trim().toUpperCase() : "";
        String serialBase  = serialEnBase.trim().toUpperCase();

        if (!serialBase.equals(serialRecu)) {
            log.warn(
                "Serial invalide — prestataire={} recu={} base={}",
                request.getPrestataireId(),
                masquerSerial(serialRecu),
                masquerSerial(serialBase));
            throw new AuthException("Accès refusé");
        }

        // 3. Nature de prestation
        String nature = (request.getNaturePrestation() != null
            && !request.getNaturePrestation().isBlank())
            ? request.getNaturePrestation() : "consultation";

        // 4. Générer OTP sécurisé
        String otp = generateSecureOtp();

        // 5. Stocker 5 minutes
        otpStore.save(
            otp,
            request.getPrestataireId(),
            request.getCodeVisite(),
            request.getAnnee(),
            nature
        );

        // 6. Construire redirectUrl Angular
        String redirectUrl = String.format(
            "/biometry/public/admin/%s/ajouter/%s_%s_%s?otp=%s",
            nature,
            request.getAnnee(),
            request.getPrestataireId(),
            request.getCodeVisite(),
            otp
        );

        log.info(
            "OTP généré ✓ — prestataire={} serial={} " +
            "visite={} nature={}",
            request.getPrestataireId(),
            masquerSerial(serialRecu),
            request.getCodeVisite(),
            nature);

        return OtpResponse.builder()
            .redirectUrl(redirectUrl)
            .expiresIn(300L)
            .build();
    }

    // ── VALIDATE OTP ──────────────────────────────────────

    @Transactional(readOnly = true)
    public LoginResponse validateOtp(String otp) {

        // 1. Consommer OTP usage unique
        OtpStore.OtpEntry entry = otpStore.consumeIfValid(otp);
        if (entry == null) {
            throw new AuthException("OTP invalide ou expiré");
        }

        // 2. Charger l'utilisateur du prestataire (premier actif)
        Utilisateur utilisateur = utilisateurRepository
            .findActiveByPrestataireId(entry.prestataireId())
            .stream().findFirst()
            .orElseThrow(() ->
                new AuthException("Prestataire introuvable"));

        Employe employe = utilisateur.getEmploye();
        if (employe == null) {
            throw new AuthException("Compte non configuré");
        }

        String profilCode     = employe.getProfil().getCode();
        String prestataireId  = employe.getPrestataireId();
        String connexionAppli = employe.getConnexionAppli();

        // 3. Générer JWT normal
        String token = jwtProvider.generateToken(
            utilisateur.getId(),
            utilisateur.getLogin(),
            profilCode,
            prestataireId,
            connexionAppli
        );

        List<MenuResponse> menus =
            chargerMenus(employe.getProfil().getId(), profilCode);

        log.info(
            "OTP validé ✓ → JWT généré — prestataire={} visite={}",
            entry.prestataireId(), entry.codeVisite());

        // 4. Retourner LoginResponse + infos visite
        return LoginResponse.builder()
            .token(token)
            .tokenType("Bearer")
            .expiresIn(jwtExpiration)
            .userId(utilisateur.getId())
            .login(utilisateur.getLogin())
            .nom(utilisateur.getNom())
            .prenom(utilisateur.getPrenom())
            .email(utilisateur.getEmail())
            .profilCode(profilCode)
            .profilLibelle(employe.getProfil().getTypeProfil())
            .connexionAppli(connexionAppli)
            .prestataireId(prestataireId)
            .menus(menus)
            // Infos visite pour pré-remplir le formulaire Angular
            .codeVisite(entry.codeVisite())
            .annee(entry.annee())
            .naturePrestation(entry.naturePrestation())
            .build();
    }

    // ── MENUS ─────────────────────────────────────────────

    private List<MenuResponse> chargerMenus(Integer profilId,
                                             String profilCode) {
        List<Menu> menus;

        if ("SUP_ADMIN".equals(profilCode)) {
            menus = permissionRepository.findAllActiveMenus();
        } else {
            List<Permission> permissions =
                permissionRepository.findMenusByProfilId(profilId);
            menus = permissions.stream()
                .map(Permission::getMenu)
                .collect(Collectors.toList());
        }

        Map<Integer, List<MenuResponse>> parParent = menus.stream()
            .map(this::toMenuResponse)
            .filter(m -> m.getPereId() != null && m.getPereId() > 0)
            .collect(Collectors.groupingBy(MenuResponse::getPereId));

        List<MenuResponse> arbre = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getPereId() == null || menu.getPereId() == 0) {
                MenuResponse mr = toMenuResponse(menu);
                mr.setSousMenus(parParent.getOrDefault(
                    menu.getId(), List.of()));
                arbre.add(mr);
            }
        }
        return arbre;
    }

    private MenuResponse toMenuResponse(Menu menu) {
        return MenuResponse.builder()
            .id(menu.getId())
            .pereId(menu.getPereId())
            .nomModule(menu.getNomModule())
            .nomAction(menu.getNomAction())
            .nomControlleur(menu.getNomControlleur())
            .classImage(menu.getClassImage())
            .numeroOrdre(menu.getNumeroOrdre())
            .type(menu.getType())
            .apparaitNav(menu.getApparaitNav())
            .apparaitNavBar(menu.getApparaitNavBar())
            .sousMenus(new ArrayList<>())
            .build();
    }

    // ── HELPERS ───────────────────────────────────────────

    private LoginResponse buildLoginResponse(Utilisateur u,
                                              Employe e,
                                              String profilCode,
                                              String token,
                                              List<MenuResponse> menus) {
        return LoginResponse.builder()
            .token(token)
            .tokenType("Bearer")
            .expiresIn(jwtExpiration)
            .userId(u.getId())
            .login(u.getLogin())
            .nom(u.getNom())
            .prenom(u.getPrenom())
            .email(u.getEmail())
            .profilCode(profilCode)
            .profilLibelle(e.getProfil().getTypeProfil())
            .connexionAppli(e.getConnexionAppli())
            .prestataireId(e.getPrestataireId())
            .menus(menus)
            .build();
    }

    private String generateSecureOtp() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder()
            .withoutPadding().encodeToString(bytes);
    }

    private String masquerSerial(String serial) {
        if (serial == null || serial.length() < 8) return "****";
        return serial.substring(0, 4) + "****" +
               serial.substring(serial.length() - 4);
    }
}