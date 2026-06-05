package com.esphere.validation.controller;

import com.esphere.validation.dto.request.ConsultationSoumissionRequest;
import com.esphere.validation.dto.request.ExamenDTO;
import com.esphere.validation.dto.request.MedicamentDTO;
import com.esphere.validation.dto.request.MedicamentRechercheRequest;
import com.esphere.validation.dto.request.PrestationSoumissionRequest;
import com.esphere.validation.dto.request.TypePrestationDTO;
import com.esphere.validation.dto.request.ValidationConsultationRequest;
import com.esphere.validation.dto.request.ValidationLigneRequest;
import com.esphere.validation.dto.response.ConsommationResponse;
import com.esphere.validation.dto.response.ConsultationEnAttenteResponse;
import com.esphere.validation.dto.response.LigneEnAttenteResponse;
import com.esphere.validation.dto.response.VisiteInfoResponse;
import com.esphere.validation.repository.ConsultationRepository;
import com.esphere.validation.repository.LignePrestationRepository;
import com.esphere.validation.service.ConsommationService;
import com.esphere.validation.service.ExamenService;
import com.esphere.validation.service.MedicamentService;
import com.esphere.validation.service.TypePrestationService;
import com.esphere.validation.service.ValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/validations")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;
    private final ConsommationService consommationService;
   
    private final MedicamentService medicamentService;
      private final TypePrestationService typePrestationService;
      private final ExamenService examenService;
      private final ConsultationRepository consultationRepository;
      private final LignePrestationRepository ligneRepository;



    // ── CONSULTATIONS ─────────────────────────────────────────────
    // GET /validations/consultations/en-attente
    @GetMapping("/consultations/en-attente")
    public ResponseEntity<List<ConsultationEnAttenteResponse>> getConsultationsEnAttente() {
        return ResponseEntity.ok(validationService.getConsultationsEnAttente());
    }

    // GET /validations/consultations/en-attente/prestataire/{id}
    @GetMapping("/consultations/en-attente/prestataire/{prestataireId}")
    public ResponseEntity<List<ConsultationEnAttenteResponse>> getConsultationsEnAttenteByPrestataire(
            @PathVariable String prestataireId) {
        return ResponseEntity.ok(
                validationService.getConsultationsEnAttenteByPrestataire(prestataireId));
    }

    // GET /validations/consultations?page=0&size=10&prestataireId=X&etat=Y&dateMin=Z&dateMax=W
    @GetMapping("/consultations")
    public ResponseEntity<Map<String, Object>> getConsultationsPaginees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String prestataireId,
            @RequestParam(required = false) String etat,
            @RequestParam(required = false) String typeConsultation,
            @RequestParam(required = false) String souscripteur,
            @RequestParam(required = false) String nomAdherent,
            @RequestParam(required = false) String nomAyantDroit,
            @RequestParam(required = false) String dateMin,
            @RequestParam(required = false) String dateMax) {
        return ResponseEntity.ok(
                validationService.getConsultationsPaginees(
                        page, size, prestataireId, etat,typeConsultation,souscripteur,nomAdherent,nomAyantDroit, dateMin, dateMax));
    }

    // PUT /validations/consultations/{id}
    @PutMapping("/consultations/{id}")
    public ResponseEntity<ConsultationEnAttenteResponse> validerConsultation(
            @PathVariable Integer id,
            @Valid @RequestBody ValidationConsultationRequest request) {
        return ResponseEntity.ok(validationService.validerConsultation(id, request));
    }

    // PUT /validations/consultations/{id}/encaisser
    @PutMapping("/consultations/{id}/encaisser")
    public ResponseEntity<Void> encaisserConsultation(@PathVariable Integer id) {
        validationService.encaisserConsultation(id);
        return ResponseEntity.noContent().build();
    }

    // ── LIGNES DE PRESTATION ──────────────────────────────────────
    // GET /validations/lignes/en-attente
    @GetMapping("/lignes/en-attente")
    public ResponseEntity<List<LigneEnAttenteResponse>> getLignesEnAttente() {
        return ResponseEntity.ok(validationService.getLignesEnAttente());
    }

    // GET /validations/lignes/en-attente/prestataire/{id}
    @GetMapping("/lignes/en-attente/prestataire/{prestataireId}")
    public ResponseEntity<List<LigneEnAttenteResponse>> getLignesEnAttenteByPrestataire(
            @PathVariable String prestataireId) {
        return ResponseEntity.ok(
                validationService.getLignesEnAttenteByPrestataire(prestataireId));
    }

    // GET /validations/lignes/prestation/{prestationId}
    @GetMapping("/lignes/prestation/{prestationId}")
    public ResponseEntity<List<LigneEnAttenteResponse>> getLignesByPrestation(
            @PathVariable Integer prestationId) {
        return ResponseEntity.ok(validationService.getLignesByPrestation(prestationId));
    }

    // PUT /validations/lignes/{id}
    @PutMapping("/lignes/{id}")
    public ResponseEntity<LigneEnAttenteResponse> validerLigne(
            @PathVariable Integer id,
            @Valid @RequestBody ValidationLigneRequest request) {
        return ResponseEntity.ok(validationService.validerLigne(id, request));
    }

    // PUT /validations/lignes/{id}/encaisser
    @PutMapping("/lignes/{id}/encaisser")
    public ResponseEntity<Void> encaisserLigne(@PathVariable Integer id) {
        validationService.encaisserLigne(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/consommation/visite/{visiteId:.+}")
    public ResponseEntity<ConsommationResponse> getConsommation(
            @PathVariable String visiteId) {
        return ResponseEntity.ok(consommationService.getConsommation(visiteId));
    }

// GET /validations/prestations?nature=ordonnance&page=0&size=10&...
    @GetMapping("/prestations")
    public ResponseEntity<Map<String, Object>> getPrestationsPaginees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ordonnance") String nature,
            @RequestParam(required = false) String prestataireId,
            @RequestParam(required = false) String dateMin,
            @RequestParam(required = false) String dateMax,
            @RequestParam(required = false) String souscripteur,
            @RequestParam(required = false) String adherent,
            @RequestParam(required = false) String ayantDroit,
            @RequestParam(required = false) String etat) {
        return ResponseEntity.ok(
                validationService.getPrestationsPaginees(
                        page, size, nature,
                        prestataireId, dateMin, dateMax,
                        souscripteur, adherent, ayantDroit, etat));
    }
    
    /**
 * GET /validations/visite/{codeVisite}
 * Retourne les infos de l'assuré pour pré-remplir
 * le formulaire de saisie prestataire
 */
@GetMapping("/visite/{codeVisite}")
public ResponseEntity<VisiteInfoResponse> getVisiteInfo(
        @PathVariable String codeVisite) {
    return ResponseEntity.ok(
        validationService.getVisiteInfo(codeVisite));
}
 /**
 * POST /validations/consultations
 * Soumission d'une consultation par le prestataire
 */
@PostMapping("/consultations")
public ResponseEntity<ConsultationEnAttenteResponse> soumettreConsultation(
        @RequestBody ConsultationSoumissionRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(validationService.soumettreConsultation(request));
}

 /**
 * POST /validations/consultations
 * Soumission d'une consultation par le prestataire
 */


/** Recherche un médicament par nom, le crée si inexistant */
    @PostMapping("/medicaments/rechercher-ou-creer")
    public ResponseEntity<MedicamentDTO> rechercherOuCreer(
            @RequestBody MedicamentRechercheRequest request) {

        MedicamentDTO result = medicamentService
                .rechercherOuCreer(request.getNom().trim());

        return ResponseEntity.ok(result);
    }

    /** Liste tous les médicaments (pour initialiser le frontend)
     * @return  */
    @GetMapping("/referentiel/medicaments")
    public ResponseEntity<List<MedicamentDTO>> listerTous() {
        return ResponseEntity.ok(
                medicamentService.listerTous());
    }
    
    /** GET /api/types-prestation → tous */
    @GetMapping
    public ResponseEntity<List<TypePrestationDTO>> listerTousTyprestation() {
        return ResponseEntity.ok(typePrestationService.listerTous());
    }

    /** GET /api/types-prestation/affiches → affiche=1 */
    @GetMapping("/affiches")
    public ResponseEntity<List<TypePrestationDTO>> listerAffiches() {
        return ResponseEntity.ok(typePrestationService.listerAffiches());
    }

    /** GET /api/types-prestation/categorie/{cat} */
    @GetMapping("/referentiel/types-examen/{categorie}")
    public ResponseEntity<List<TypePrestationDTO>> parCategorie(
            @PathVariable String categorie) {
        return ResponseEntity.ok(
                typePrestationService.listerParCategorie(categorie));
    }

    /** POST /api/types-prestation → créer */
    @PostMapping("/typeprestation/rechercher-ou-creer")
    public ResponseEntity<TypePrestationDTO> creer(
            @RequestBody TypePrestationDTO dto) {
        return ResponseEntity.ok(typePrestationService.creer(dto));
    }
    
    /** GET /api/examens */
    @GetMapping("/referentiel/examens-actes")
    public ResponseEntity<List<ExamenDTO>> listerTousExamens_actes() {
        return ResponseEntity.ok(this.examenService.listerTous());
    }

    /** POST /api/examens/rechercher-ou-creer */
    @PostMapping("/examens/rechercher-ou-creer")
    public ResponseEntity<ExamenDTO> rechercherOuCreer(
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                this.examenService.rechercherOuCreer(body.get("nom")));
    }
    
    // POST /validations/prestations
@PostMapping("/prestations")
public ResponseEntity<Void> soumettrePrestation(
        @RequestBody PrestationSoumissionRequest request) {
    validationService.soumettrePrestation(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
}

/**
 * GET /validations/dashboard/prestataire/{id}/a-encaisser
 * Retourne les compteurs de prestations validées
 * à encaisser pour un prestataire donné.
 * Appelé uniquement par le prestataire connecté.
 */

@GetMapping("/dashboard/prestataire/{prestataireId}/a-encaisser")
public ResponseEntity<Map<String, Long>>
getAEncaisserByPrestataire(
        @PathVariable String prestataireId) {

    long consultations = consultationRepository
        .countByPrestataireAndEtat(
            prestataireId, "valide");

    long ordonnances = ligneRepository
        .countOrdonnancesValidesByPrestataire(
            prestataireId, "valide");

    long examens = ligneRepository
        .countExamensValidesByPrestataire(
            prestataireId, "valide");

    return ResponseEntity.ok(Map.of(
        "consultationsValides", consultations,
        "ordonnancesValidees",  ordonnances,
        "examensValides",       examens
    ));
}
/**
 * GET /validations/taux
 * Retourne le taux de couverture pour un type
 * de prestation donné — utilisé par Angular
 * pour afficher le détail avant soumission.
 */
@GetMapping("/taux")
public ResponseEntity<Map<String, Object>> getTaux(
        @RequestParam String police,
        @RequestParam short  groupe,
        @RequestParam String typePrestation,
        @RequestParam String codeAdherent) {

    double taux = validationService.getTauxPublic(
        police, groupe, typePrestation, codeAdherent);

    return ResponseEntity.ok(Map.of(
        "taux",  taux,
        "police", police,
        "groupe", groupe,
        "typePrestation", typePrestation
    ));
}
}
