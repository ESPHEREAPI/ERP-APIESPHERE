/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;
import db.biometry.biometry.dtos.AdherentDTO;
import db.biometry.biometry.dtos.AdherentFilterDTO;
import db.biometry.biometry.dtos.AdherentSimpleDTO;
import db.biometry.biometry.dtos.DetailConsomationAdherentDTO;
import db.biometry.biometry.dtos.SouscripteurActifDTO;
import db.biometry.biometry.repositories.AdherentRepository;
import db.biometry.biometry.services.AdherentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST pour la gestion des adhérents
 * Expose les endpoints CRUD pour les adhérents
 * 
 * @author JIATOU FRANCK
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@Slf4j
@RestController
@RequestMapping("/partenaire")
//@RequestMapping("/service-biometrie-partenaire")
@RequiredArgsConstructor
@Tag(name = "Gestion des Adhérents", description = "APIs pour la gestion des adhérents/assurés")
//@CrossOrigin("*")
public class AdherentController {
    private final AdherentService adherentService;
    private final AdherentRepository adherentRepository;

    // ── Souscripteurs actifs ──────────────────────────────────────────────────

    /**
     * Liste distincte des souscripteurs actifs (effetPolice <= today < echeancePolice).
     * Utilisée pour peupler le dropdown "Souscripteur" dans les dashboards DII/Service Santé.
     * Retourne : [{souscripteur, police, libelle}]  ex: "CAMTE (1017-2310000100)"
     */
    @GetMapping("/adherents/souscripteurs/actifs")
    @Operation(summary = "Liste des souscripteurs actifs",
               description = "Retourne les souscripteurs dont la police est en vigueur aujourd'hui")
    public ResponseEntity<List<SouscripteurActifDTO>> getSouscripteursActifs() {
        List<Object[]> rows = adherentRepository.findDistinctSouscripteursActifs();
        List<SouscripteurActifDTO> result = rows.stream()
                .map(r -> new SouscripteurActifDTO((String) r[0], (String) r[1]))
                .toList();
        return ResponseEntity.ok(result);
    }

    // ── Adhérents d'une police ─────────────────────────────────────────────────

    /**
     * Liste allégée des adhérents d'une police active.
     * Utilisée pour peupler la liste "Adhérent" après sélection d'un souscripteur.
     */
    @GetMapping("/adherents/by-police/{police}")
    @Operation(summary = "Adhérents par police",
               description = "Liste des adhérents actifs pour une police donnée")
    public ResponseEntity<List<AdherentSimpleDTO>> getAdherentsByPolice(
            @PathVariable String police) {
        List<Object[]> rows = adherentRepository.findAdherentsByPoliceActif(police);
        List<AdherentSimpleDTO> result = rows.stream()
                .map(r -> new AdherentSimpleDTO(
                        (String) r[0],   // code_adherent
                        (String) r[1],   // assure_principal
                        (String) r[2],   // matricule
                        (String) r[3]))  // statut
                .toList();
        return ResponseEntity.ok(result);
    }
    
    /**
     * Recherche des adhérents avec filtres et pagination
     * 
     * @param filter Critères de recherche
     * @return Page d'adhérents
     */
    @PostMapping("/adherents/search")
    @Operation(
            summary = "Recherche d'adhérents",
            description = "Recherche des adhérents avec filtres multiples et pagination"
    )
    public ResponseEntity<Page<AdherentDTO>> searchAdherents(
            @Valid @RequestBody AdherentFilterDTO filter) {
        
        log.info("POST /api/v1/adherents/search - filtres: {}", filter);
        
        Page<AdherentDTO> adherents = adherentService.searchAdherents(filter);
        
        return ResponseEntity.ok(adherents);
    }
    
    /**
     * Récupère le profil détaillé d'un adhérent
     * 
     * @param codeAdherent Code de l'adhérent
     * @return Profil détaillé
     */
    @GetMapping("/adherents/{codeAdherent}")
    @Operation(
            summary = "Détails d'un adhérent",
            description = "Récupère le profil complet d'un adhérent incluant ses ayants droit et sa consommation"
    )
    public ResponseEntity<AdherentDTO> getAdherent(
            @Parameter(description = "Code de l'adhérent", required = true)
            @PathVariable String codeAdherent) {
        
        log.info("GET /api/v1/adherents/{} - récupération du profil", codeAdherent);
        
        AdherentDTO adherent = adherentService.getAdherentProfile(codeAdherent);
        
        return ResponseEntity.ok(adherent);
    }
    
    /**
     * Crée un nouvel adhérent
     * 
     * @param adherentDTO Données de l'adhérent
     * @return Adhérent créé
     */
    @PostMapping
    @Operation(
            summary = "Créer un adhérent",
            description = "Crée un nouvel adhérent dans le système"
    )
    public ResponseEntity<AdherentDTO> createAdherent(
            @Valid @RequestBody AdherentDTO adherentDTO) {
        
        log.info("POST /api/v1/adherents - création: {}", adherentDTO.getAssurePrincipal());
        
        AdherentDTO created = adherentService.createAdherent(adherentDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Met à jour un adhérent existant
     * 
     * @param codeAdherent Code de l'adhérent
     * @param adherentDTO Nouvelles données
     * @return Adhérent mis à jour
     */
    @PutMapping("/adherents/{codeAdherent}")
    @Operation(
            summary = "Modifier un adhérent",
            description = "Met à jour les informations d'un adhérent existant"
    )
    public ResponseEntity<AdherentDTO> updateAdherent(
            @Parameter(description = "Code de l'adhérent", required = true)
            @PathVariable String codeAdherent,
            @Valid @RequestBody AdherentDTO adherentDTO) {
        
        log.info("PUT /api/v1/adherents/{} - mise à jour", codeAdherent);
        
        AdherentDTO updated = adherentService.updateAdherent(codeAdherent, adherentDTO);
        
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Supprime un adhérent
     * 
     * @param codeAdherent Code de l'adhérent
     * @return Confirmation de suppression
     */
    @DeleteMapping("/adherents/{codeAdherent}")
    @Operation(
            summary = "Supprimer un adhérent",
            description = "Supprime logiquement un adhérent (changement de statut)"
    )
    public ResponseEntity<Void> deleteAdherent(
            @Parameter(description = "Code de l'adhérent", required = true)
            @PathVariable String codeAdherent) {
        
        log.info("DELETE /api/v1/adherents/{} - suppression", codeAdherent);
        
        adherentService.deleteAdherent(codeAdherent);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Change le statut d'un adhérent
     * 
     * @param codeAdherent Code de l'adhérent
     * @param statut Nouveau statut
     * @return Adhérent mis à jour
     */
    @PatchMapping("/adherents/{codeAdherent}/statut")
    @Operation(
            summary = "Changer le statut",
            description = "Modifie le statut d'un adhérent (ACTIF, SUSPENDU, RESILIE)"
    )
    public ResponseEntity<AdherentDTO> changeStatut(
            @Parameter(description = "Code de l'adhérent", required = true)
            @PathVariable String codeAdherent,
            @Parameter(description = "Nouveau statut", required = true)
            @RequestParam String statut) {
        
        log.info("PATCH /api/v1/adherents/{}/statut - changement vers: {}", codeAdherent, statut);
        
        AdherentDTO adherent = adherentService.getAdherentProfile(codeAdherent);
        adherent.setStatut(statut);
        AdherentDTO updated = adherentService.updateAdherent(codeAdherent, adherent);
        
        return ResponseEntity.ok(updated);
    }
    
       public ResponseEntity<List<DetailConsomationAdherentDTO>> getDetailConsomationAdherent(
            @Parameter(description = "Code du souscripteur", required = true)
            @RequestParam String codeSouscripteur,
            
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDateTime dateDebut,
            
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDateTime dateFin){
           
           
           List<DetailConsomationAdherentDTO>consomation=adherentService.detailConsomationAdherent(dateDebut, dateFin, codeSouscripteur);
             return ResponseEntity.ok(consomation);
       }
       
         @GetMapping("/adherents/all/{souscripteur}")
    @Operation(
            summary = "Détails d'un adhérent",
            description = "Récupère le profil complet d'un adhérent incluant ses ayants droit et sa consommation"
    )
    public ResponseEntity<List<AdherentDTO>> allAdherent(
            
            @PathVariable String souscripteur) {
        
        //log.info("GET /api/v1/adherents/{} - récupération du profil", codeAdherent);
        
        
        
        return ResponseEntity.ok(adherentService.allAdherentBySoucripteur("CAMTEL"));
    }
}
