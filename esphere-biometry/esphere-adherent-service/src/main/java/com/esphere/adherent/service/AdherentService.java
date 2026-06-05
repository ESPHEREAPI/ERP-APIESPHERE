package com.esphere.adherent.service;

import com.esphere.adherent.dto.response.AdherentResponse;
import com.esphere.adherent.dto.response.AyantDroitResponse;
import com.esphere.adherent.entity.Adherent;
import com.esphere.adherent.entity.AyantDroit;
import com.esphere.adherent.exception.AdherentException;
import com.esphere.adherent.repository.AdherentRepository;
import com.esphere.adherent.repository.AyantDroitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdherentService {

    private final AdherentRepository   adherentRepository;
    private final AyantDroitRepository ayantDroitRepository;

    // Fiche complète d'un assuré avec ses ayants droit
    @Transactional(readOnly = true)
    public AdherentResponse getAdherent(String codeAdherent) {
        Adherent adherent = adherentRepository
                .findActiveByCode(codeAdherent)
                .orElseThrow(() -> new AdherentException(
                        "Assuré introuvable : " + codeAdherent, 404));

        List<AyantDroit> ayantsDroit =
                ayantDroitRepository.findActiveByAdherent(codeAdherent);

        return toResponse(adherent, ayantsDroit);
    }

    // Ayants droit uniquement
    @Transactional(readOnly = true)
    public List<AyantDroitResponse> getAyantsDroit(String codeAdherent) {
        adherentRepository.findActiveByCode(codeAdherent)
                .orElseThrow(() -> new AdherentException(
                        "Assuré introuvable : " + codeAdherent, 404));

        return ayantDroitRepository.findActiveByAdherent(codeAdherent)
                .stream()
                .map(this::toAyantDroitResponse)
                .collect(Collectors.toList());
    }

    // Recherche par nom
    @Transactional(readOnly = true)
    public List<AdherentResponse> searchByNom(String nom) {
        return adherentRepository.searchByNom(nom)
                .stream()
                .map(a -> toResponse(a, List.of()))
                .collect(Collectors.toList());
    }

    // Recherche par police
    @Transactional(readOnly = true)
    public List<AdherentResponse> getByPolice(String police) {
        return adherentRepository.findByPolice(police)
                .stream()
                .map(a -> toResponse(a, List.of()))
                .collect(Collectors.toList());
    }

    // Fiche d'un ayant droit
    @Transactional(readOnly = true)
    public AyantDroitResponse getAyantDroit(String codeAyantDroit) {
        return ayantDroitRepository.findActiveByCode(codeAyantDroit)
                .map(this::toAyantDroitResponse)
                .orElseThrow(() -> new AdherentException(
                        "Ayant droit introuvable : " + codeAyantDroit, 404));
    }

    // ── Mappers ──────────────────────────────────────────────────────

private AdherentResponse toResponse(Adherent a, List<AyantDroit> ayantsDroit) {
    return AdherentResponse.builder()
            .codeAdherent(a.getCodeAdherent())
            .assurePrincipal(a.getAssurePrincipal())
            .naissance(a.getNaissance())
            .sexe(a.getSexe())
            .matricule(a.getMatricule())
            .telephone(a.getTelephone())
            .taux(a.getTaux())
            .plafondAssurep(a.getPlafondAssurep())
            .consAp(a.getConsAp())
            .ville(a.getVille())
            .souscripteur(a.getSouscripteur())
            .police(a.getPolice())
            .effetPolice(a.getEffetPolice())
            .echeancePolice(a.getEcheancePolice())
            .groupe(a.getGroupe())
            .statut(a.getStatut())
            .dateEnrole(a.getDateEnrole())
            .ayantsDroit(ayantsDroit.stream()
                    .map(this::toAyantDroitResponse)
                    .collect(Collectors.toList()))
            .build();
}

    private AyantDroitResponse toAyantDroitResponse(AyantDroit a) {
        return AyantDroitResponse.builder()
                .codeAyantDroit(a.getCodeAyantDroit())
                .codeAdherent(a.getAdherent().getCodeAdherent())
                .nom(a.getNom())
                .sexe(a.getSexe())
                .naissance(a.getNaissance())
                .lienPare(a.getLienPare())
                .telephone(a.getTelephone())
                .police(a.getPolice())
                .statut(a.getStatut())
                .build();
    }
}