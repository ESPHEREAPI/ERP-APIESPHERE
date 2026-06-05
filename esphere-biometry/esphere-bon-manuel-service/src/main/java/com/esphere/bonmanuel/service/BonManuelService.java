package com.esphere.bonmanuel.service;

import com.esphere.bonmanuel.dto.request.*;
import com.esphere.bonmanuel.dto.response.*;
import com.esphere.bonmanuel.entity.*;
import com.esphere.bonmanuel.exception.BonManuelException;
import com.esphere.bonmanuel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BonManuelService {

    private final BonManuelRepository      bonManuelRepository;
    private final BonManuelLigneRepository ligneRepository;

    // ── CRÉATION ─────────────────────────────────────────────────

    @Transactional
    public BonManuelResponse creer(BonManuelRequest request) {

        // Vérifier unicité du numéro proforma
        if (bonManuelRepository.findByNumeroProforma(
                request.getNumeroProforma()).isPresent()) {
            throw new BonManuelException(
                    "Ce numéro de proforma existe déjà : "
                    + request.getNumeroProforma(), 409);
        }

        String reference = genererReference(request.getPrestataireId());

        BonManuel bon = BonManuel.builder()
                .reference(reference)
                .numeroProforma(request.getNumeroProforma())
                .visiteId(request.getVisiteId())
                .prestataireId(request.getPrestataireId())
                .codeAdherent(request.getCodeAdherent())
                .codeAyantDroit(request.getCodeAyantDroit())
                .employeId(request.getEmployeId())
                .montantProforma(request.getMontantProforma())
                .statut("en_attente")
                .observations(request.getObservations())
                .dateCreation(LocalDateTime.now())
                .supprime("-1")
                .build();

        bonManuelRepository.save(bon);
        log.info("Bon manuel créé : {}", reference);

        return toResponse(bon);
    }

    // ── CONFIRMATION GLOBALE ─────────────────────────────────────

    @Transactional
    public BonManuelResponse confirmerGlobal(
            Integer id,
            ConfirmationGlobaleRequest request) {

        BonManuel bon = getBonOuErreur(id);
        verifierStatut(bon, "en_attente");

        bon.setMontantConfirme(request.getMontantConfirme());
        bon.setTypeValidation("global");
        bon.setStatut("confirme");
        bon.setEmployeId(request.getEmployeId());
        bon.setObservations(request.getObservations());
        bon.setDateConfirmation(LocalDateTime.now());

        bonManuelRepository.save(bon);
        log.info("Bon manuel {} confirmé en global. Montant : {}",
                bon.getReference(), request.getMontantConfirme());

        return toResponse(bon);
    }

    // ── CONFIRMATION DÉTAILLÉE ────────────────────────────────────

    @Transactional
    public BonManuelResponse confirmerDetail(
            Integer id,
            ConfirmationDetailRequest request) {

        BonManuel bon = getBonOuErreur(id);
        verifierStatut(bon, "en_attente");

        // Calculer le montant total des lignes
        List<BonManuelLigne> lignes = request.getLignes().stream()
                .map(l -> {
                    double total = l.getQuantite() * l.getMontantUnitaire();
                    return BonManuelLigne.builder()
                            .bonManuel(bon)
                            .nom(l.getNom())
                            .codification(l.getCodification())
                            .quantite(l.getQuantite())
                            .montantUnitaire(l.getMontantUnitaire())
                            .montantTotal(total)
                            .observations(l.getObservations())
                            .build();
                })
                .collect(Collectors.toList());

        double montantTotal = lignes.stream()
                .mapToDouble(BonManuelLigne::getMontantTotal)
                .sum();

        bon.setTypeValidation("detail");
        bon.setMontantConfirme(montantTotal);
        bon.setStatut("confirme");
        bon.setEmployeId(request.getEmployeId());
        bon.setObservations(request.getObservations());
        bon.setDateConfirmation(LocalDateTime.now());

        bonManuelRepository.save(bon);
        ligneRepository.saveAll(lignes);

        log.info("Bon manuel {} confirmé en détail. {} lignes. Total : {}",
                bon.getReference(), lignes.size(), montantTotal);

        return toResponse(bon);
    }

    // ── REJET ─────────────────────────────────────────────────────

    @Transactional
    public BonManuelResponse rejeter(Integer id, String observations, Integer employeId) {

        BonManuel bon = getBonOuErreur(id);
        verifierStatut(bon, "en_attente");

        bon.setStatut("rejete");
        bon.setObservations(observations);
        bon.setEmployeId(employeId);
        bon.setDateConfirmation(LocalDateTime.now());

        bonManuelRepository.save(bon);
        log.info("Bon manuel {} rejeté", bon.getReference());

        return toResponse(bon);
    }

    // ── ENCAISSEMENT (par le prestataire) ────────────────────────

    @Transactional
    public BonManuelResponse encaisser(Integer id, Integer employeEncaisseId) {

        BonManuel bon = getBonOuErreur(id);
        verifierStatut(bon, "confirme");

        bon.setStatut("encaisse");
        bon.setEmployeEncaisseId(employeEncaisseId);
        bon.setDateEncaissement(LocalDateTime.now());

        bonManuelRepository.save(bon);
        log.info("Bon manuel {} encaissé", bon.getReference());

        return toResponse(bon);
    }

    // ── LECTURES ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public BonManuelResponse getById(Integer id) {
        return toResponse(getBonOuErreur(id));
    }

    @Transactional(readOnly = true)
    public BonManuelResponse getByReference(String reference) {
        return bonManuelRepository.findByReference(reference)
                .map(this::toResponse)
                .orElseThrow(() -> new BonManuelException(
                        "Bon manuel introuvable : " + reference, 404));
    }

    @Transactional(readOnly = true)
    public List<BonManuelResponse> getEnAttente() {
        return bonManuelRepository.findByStatut("en_attente")
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BonManuelResponse> getByPrestataire(String prestataireId) {
        return bonManuelRepository.findByPrestataire(prestataireId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BonManuelResponse> getConfirmesParPrestataire(String prestataireId) {
        return bonManuelRepository.findConfirmesParPrestataire(prestataireId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BonManuelResponse> getByAdherent(String codeAdherent) {
        return bonManuelRepository.findByAdherent(codeAdherent)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Helpers ──────────────────────────────────────────────────

    private BonManuel getBonOuErreur(Integer id) {
        return bonManuelRepository.findById(id)
                .orElseThrow(() -> new BonManuelException(
                        "Bon manuel introuvable : " + id, 404));
    }

    private void verifierStatut(BonManuel bon, String statutAttendu) {
        if (!statutAttendu.equals(bon.getStatut())) {
            throw new BonManuelException(
                    "Action impossible. Statut actuel : " + bon.getStatut()
                    + ". Statut attendu : " + statutAttendu, 400);
        }
    }

    private String genererReference(String prestataireId) {
        String annee = String.valueOf(Year.now().getValue());
        long seq = bonManuelRepository.countByPrestataire(prestataireId) + 1;
        return String.format("BM-%s-%s-%05d", annee, prestataireId, seq);
    }

    // ── Mapper ───────────────────────────────────────────────────

    private BonManuelResponse toResponse(BonManuel b) {
        List<BonManuelLigneResponse> lignes = ligneRepository
                .findByBonManuel(b.getId())
                .stream()
                .map(l -> BonManuelLigneResponse.builder()
                        .id(l.getId())
                        .nom(l.getNom())
                        .codification(l.getCodification())
                        .quantite(l.getQuantite())
                        .montantUnitaire(l.getMontantUnitaire())
                        .montantTotal(l.getMontantTotal())
                        .observations(l.getObservations())
                        .build())
                .collect(Collectors.toList());

        return BonManuelResponse.builder()
                .id(b.getId())
                .reference(b.getReference())
                .numeroProforma(b.getNumeroProforma())
                .visiteId(b.getVisiteId())
                .prestataireId(b.getPrestataireId())
                .codeAdherent(b.getCodeAdherent())
                .codeAyantDroit(b.getCodeAyantDroit())
                .employeId(b.getEmployeId())
                .montantProforma(b.getMontantProforma())
                .montantConfirme(b.getMontantConfirme())
                .typeValidation(b.getTypeValidation())
                .statut(b.getStatut())
                .observations(b.getObservations())
                .dateCreation(b.getDateCreation())
                .dateConfirmation(b.getDateConfirmation())
                .dateEncaissement(b.getDateEncaissement())
                .lignes(lignes)
                .build();
    }
}