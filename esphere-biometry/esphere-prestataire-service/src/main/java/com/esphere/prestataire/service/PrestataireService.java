package com.esphere.prestataire.service;

import com.esphere.prestataire.dto.response.*;
import com.esphere.prestataire.entity.*;
import com.esphere.prestataire.exception.PrestataireException;
import com.esphere.prestataire.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrestataireService {

    private final PrestataireRepository     prestataireRepository;
    private final TypePrestationRepository  typePrestationRepository;
    private final TauxPrestationRepository  tauxPrestationRepository;

    @Transactional(readOnly = true)
    public PrestataireResponse getPrestataire(String id) {
        return prestataireRepository.findActiveById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new PrestataireException("Prestataire introuvable : " + id, 404));
    }

    @Transactional(readOnly = true)
    public List<PrestataireResponse> getByCategorie(String categorieId) {
        return prestataireRepository.findActiveByCategorie(categorieId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrestataireResponse> search(String nom) {
        return prestataireRepository.searchByNom(nom)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TypePrestationResponse> getTypesPrestation() {
        return typePrestationRepository.findAllVisible()
                .stream().map(this::toTypeResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TypePrestationResponse> getTypesByCategorie(String categorie) {
        return typePrestationRepository.findByCategorie(categorie)
                .stream().map(this::toTypeResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TauxPrestationResponse> getTauxByPolice(String police) {
        return tauxPrestationRepository.findByPolice(police)
                .stream().map(this::toTauxResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TauxPrestationResponse getTaux(String typePrestationId, String police, Short groupe) {
        return tauxPrestationRepository.findTaux(typePrestationId, police, groupe)
                .map(this::toTauxResponse)
                .orElseThrow(() -> new PrestataireException(
                        "Taux introuvable pour : " + typePrestationId + " / " + police, 404));
    }

    // ── Mappers ──────────────────────────────────────────────────────

    private PrestataireResponse toResponse(Prestataire p) {
        return PrestataireResponse.builder()
                .id(p.getId())
                .categorieId(p.getCategorie().getId())
                .categorieNom(p.getCategorie().getNom())
                .villeId(p.getVilleId())
                .nom(p.getNom())
                .adresse(p.getAdresse())
                .email(p.getEmail())
                .telephone(p.getTelephone())
                .registre(p.getRegistre())
                .logo(p.getLogo())
                .statut(p.getStatut())
                .build();
    }

    private TypePrestationResponse toTypeResponse(TypePrestation t) {
        return TypePrestationResponse.builder()
                .id(t.getId())
                .nom(t.getNom())
                .affiche(t.getAffiche())
                .categorie(t.getCategorie())
                .build();
    }

    private TauxPrestationResponse toTauxResponse(TauxPrestation t) {
        return TauxPrestationResponse.builder()
                .id(t.getId())
                .typePrestationId(t.getTypePrestation().getId())
                .typePrestationNom(t.getTypePrestation().getNom())
                .police(t.getPolice())
                .groupe(t.getGroupe())
                .taux(t.getTaux())
                .plafond(t.getPlafond())
                .build();
    }
}