package com.esphere.prestataire.service;

import com.esphere.prestataire.dto.request.PrestataireRequest;
import com.esphere.prestataire.dto.response.PrestataireResponse;
import com.esphere.prestataire.entity.CategoriePrestataire;
import com.esphere.prestataire.entity.Prestataire;
import com.esphere.prestataire.exception.PrestataireException;
import com.esphere.prestataire.repository.CategorieRepository;
import com.esphere.prestataire.repository.PrestataireRepository;
import com.esphere.prestataire.repository.VilleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminPrestataireService {

    private final PrestataireRepository prestataireRepository;
    private final CategorieRepository   categorieRepository;
    private final VilleRepository        villeRepository;

    @Value("${esphere.upload.dir:C:/esphere/uploads/logos}")
    private String uploadDir;

    @Value("${esphere.upload.url-prefix:/biometry/logos}")
    private String urlPrefix;

    // ── Liste paginée ────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Map<String, Object> lister(String statut, String categorieId, Integer villeId,
                                      String search, int page, int size) {
        Page<Prestataire> p = prestataireRepository.findAllAdmin(
                statut.isBlank()      ? null : statut,
                categorieId.isBlank() ? null : categorieId,
                villeId,
                search.isBlank()      ? null : search,
                PageRequest.of(page, size));

        return Map.of(
                "data",        p.getContent().stream().map(this::toResponse).toList(),
                "total",       p.getTotalElements(),
                "totalPages",  p.getTotalPages(),
                "currentPage", p.getNumber()
        );
    }

    // ── Catégories ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Map<String, String>> listerCategories() {
        return categorieRepository.findAllActives().stream()
                .map(c -> Map.of("id", c.getId(), "nom", c.getNom()))
                .toList();
    }

    // ── Villes ────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listerVilles() {
        return villeRepository.findAllFrench().stream()
                .map(vl -> Map.<String, Object>of("id", vl.getVilleId(), "nom", vl.getNom()))
                .toList();
    }

    // ── Créer ────────────────────────────────────────────────────────

    @Transactional
    public PrestataireResponse creer(PrestataireRequest req) {
        if (req.getId() == null || req.getId().isBlank())
            throw new PrestataireException("L'identifiant est obligatoire", 400);
        if (prestataireRepository.existsById(req.getId()))
            throw new PrestataireException("Un prestataire avec cet identifiant existe déjà : " + req.getId(), 409);

        CategoriePrestataire cat = categorieRepository.findById(req.getCategorieId())
                .orElseThrow(() -> new PrestataireException("Catégorie introuvable : " + req.getCategorieId(), 404));

        Prestataire p = Prestataire.builder()
                .id(req.getId().trim().toUpperCase())
                .categorie(cat)
                .villeId(req.getVilleId())
                .nom(req.getNom())
                .adresse(req.getAdresse())
                .email(req.getEmail())
                .telephone(req.getTelephone())
                .registre(req.getRegistre())
                .statut("1")
                .supprime("-1")
                .build();

        return toResponse(prestataireRepository.save(p));
    }

    // ── Modifier ─────────────────────────────────────────────────────

    @Transactional
    public PrestataireResponse modifier(String id, PrestataireRequest req) {
        Prestataire p = findById(id);

        if (req.getCategorieId() != null && !req.getCategorieId().isBlank()) {
            CategoriePrestataire cat = categorieRepository.findById(req.getCategorieId())
                    .orElseThrow(() -> new PrestataireException("Catégorie introuvable : " + req.getCategorieId(), 404));
            p.setCategorie(cat);
        }

        if (req.getNom()      != null) p.setNom(req.getNom());
        if (req.getAdresse()  != null) p.setAdresse(req.getAdresse());
        if (req.getEmail()    != null) p.setEmail(req.getEmail());
        if (req.getTelephone()!= null) p.setTelephone(req.getTelephone());
        if (req.getRegistre() != null) p.setRegistre(req.getRegistre());
        if (req.getVilleId()  != null) p.setVilleId(req.getVilleId());

        return toResponse(prestataireRepository.save(p));
    }

    // ── Activer / Désactiver ─────────────────────────────────────────

    @Transactional
    public PrestataireResponse changerStatut(String id, String statut) {
        Prestataire p = findById(id);
        p.setStatut(statut);
        return toResponse(prestataireRepository.save(p));
    }

    // ── Supprimer (soft delete) ───────────────────────────────────────

    @Transactional
    public void supprimer(String id) {
        Prestataire p = findById(id);
        p.setSupprime("1");
        p.setStatut("-1");
        prestataireRepository.save(p);
    }

    // ── Upload logo ──────────────────────────────────────────────────

    @Transactional
    public PrestataireResponse uploadLogo(String id, MultipartFile file) {
        Prestataire p = findById(id);

        String ext      = getExtension(file.getOriginalFilename());
        String filename = id + "_" + UUID.randomUUID().toString().substring(0, 8) + ext;

        try {
            Path dir  = Paths.get(uploadDir);
            Files.createDirectories(dir);
            Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new PrestataireException("Erreur lors de l'upload du logo : " + e.getMessage(), 500);
        }

        p.setLogo(urlPrefix + "/" + filename);
        return toResponse(prestataireRepository.save(p));
    }

    // ── Helpers ──────────────────────────────────────────────────────

    private Prestataire findById(String id) {
        return prestataireRepository.findById(id)
                .filter(p -> "-1".equals(p.getSupprime()))
                .orElseThrow(() -> new PrestataireException("Prestataire introuvable : " + id, 404));
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return ".png";
        return filename.substring(filename.lastIndexOf("."));
    }

    private PrestataireResponse toResponse(Prestataire p) {
        String villeNom = null;
        if (p.getVilleId() != null) {
            villeNom = villeRepository.findNomByVilleId(p.getVilleId()).orElse(null);
        }
        return PrestataireResponse.builder()
                .id(p.getId())
                .categorieId(p.getCategorie().getId())
                .categorieNom(p.getCategorie().getNom())
                .villeId(p.getVilleId())
                .villeNom(villeNom)
                .nom(p.getNom())
                .adresse(p.getAdresse())
                .email(p.getEmail())
                .telephone(p.getTelephone())
                .registre(p.getRegistre())
                .logo(p.getLogo())
                .statut(p.getStatut())
                .build();
    }
}
