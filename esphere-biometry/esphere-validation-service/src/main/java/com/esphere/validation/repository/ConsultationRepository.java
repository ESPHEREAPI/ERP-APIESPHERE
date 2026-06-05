package com.esphere.validation.repository;

import com.esphere.validation.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository
        extends JpaRepository<Consultation, Integer> {

    // ── REQUÊTES MÉTIER ───────────────────────────────────────────────────────
    @Query("""
        SELECT c FROM Consultation c
        WHERE c.etatConsultation = 'attente_validation'
          AND c.supprime = '-1'
        ORDER BY c.date ASC
    """)
    List<Consultation> findEnAttente();

    @Query("""
        SELECT c FROM Consultation c
        WHERE c.etatConsultation = 'attente_validation'
          AND c.supprime = '-1'
          AND c.visiteId IN (
              SELECT v.id FROM Visite v
              WHERE v.prestataireId = :prestataireId
          )
        ORDER BY c.date ASC
    """)
    List<Consultation> findEnAttenteByPrestataire(
            @Param("prestataireId") String prestataireId);

    @Query("""
        SELECT c FROM Consultation c
        WHERE c.visiteId = :visiteId
          AND c.supprime = '-1'
    """)
    Optional<Consultation> findByVisite(
            @Param("visiteId") String visiteId);

//    @Query("""
//        SELECT c FROM Consultation c
//        WHERE c.supprime = '-1'
//          AND c.taux IS NOT NULL
//          AND (:etat IS NULL OR c.etatConsultation = :etat)
//          AND (:dateMin IS NULL OR c.date >= :dateMin)
//          AND (:dateMax IS NULL OR c.date <= :dateMax)
//          AND (
//              :prestataireId IS NULL
//              OR c.visiteId IN (
//                  SELECT v.id FROM Visite v
//                  WHERE v.prestataireId = :prestataireId
//              )
//          )
//        ORDER BY
//            CASE WHEN c.etatConsultation = 'attente_validation' THEN 0
//                 WHEN c.etatConsultation = 'valide'             THEN 1
//                 WHEN c.etatConsultation = 'rejete'             THEN 2
//                 ELSE 3
//            END,
//            c.date DESC
//    """)
//    Page<Consultation> findAllActives(
//            @Param("prestataireId") String prestataireId,
//            @Param("etat")          String etat,
//            @Param("dateMin")       LocalDateTime dateMin,
//            @Param("dateMax")       LocalDateTime dateMax,
//            Pageable pageable);
   @Query(value = """
    SELECT c.*
    FROM dbx45ty_consultation c
    INNER JOIN dbx45ty_visite v ON c.visite_id = v.id
    INNER JOIN dbx45ty_adherent a ON v.code_adherent = a.code_adherent
    WHERE c.supprime = '-1'
      AND c.taux IS NOT NULL
      AND a.statut = '1'
      AND a.effet_police    IS NOT NULL
      AND a.echeance_police IS NOT NULL
      AND a.effet_police    <= CURDATE()
      AND a.echeance_police >= CURDATE()
      AND (:etat IS NULL OR c.etat_consultation = :etat)
      AND (:typeConsultation IS NULL OR c.type_consultation = :typeConsultation)
      AND (:dateMin IS NULL OR c.date >= :dateMin)
      AND (:dateMax IS NULL OR c.date <= :dateMax)
      AND (:prestataireId IS NULL
           OR v.prestataire_id = :prestataireId)
      AND (:nomAdherent IS NULL OR :nomAdherent = ''
           OR UPPER(a.assure_principal)
              LIKE UPPER(CONCAT('%', :nomAdherent, '%')))
      AND (:souscripteur IS NULL OR :souscripteur = ''
           OR UPPER(a.souscripteur)
              LIKE UPPER(CONCAT('%', :souscripteur, '%')))
    ORDER BY
        CASE WHEN c.etat_consultation = 'attente_validation' THEN 0
             WHEN c.etat_consultation = 'valide'             THEN 1
             WHEN c.etat_consultation = 'rejete'             THEN 2
             ELSE 3
        END,
        c.date DESC
""", countQuery = """
    SELECT COUNT(c.id)
    FROM dbx45ty_consultation c
    INNER JOIN dbx45ty_visite   v ON c.visite_id     = v.id
    INNER JOIN dbx45ty_adherent a ON v.code_adherent = a.code_adherent
    WHERE c.supprime = '-1'
      AND c.taux IS NOT NULL
      AND a.statut = '1'
      AND a.effet_police    IS NOT NULL
      AND a.echeance_police IS NOT NULL
      AND a.effet_police    <= CURDATE()
      AND a.echeance_police >= CURDATE()
      AND (:etat IS NULL OR c.etat_consultation = :etat)
      AND (:typeConsultation IS NULL OR c.type_consultation = :typeConsultation)
      AND (:dateMin IS NULL OR c.date >= :dateMin)
      AND (:dateMax IS NULL OR c.date <= :dateMax)
      AND (:prestataireId IS NULL
           OR v.prestataire_id = :prestataireId)
      AND (:nomAdherent IS NULL OR :nomAdherent = ''
           OR UPPER(a.assure_principal)
              LIKE UPPER(CONCAT('%', :nomAdherent, '%')))
      AND (:souscripteur IS NULL OR :souscripteur = ''
           OR UPPER(a.souscripteur)
              LIKE UPPER(CONCAT('%', :souscripteur, '%')))
""", nativeQuery = true)
Page<Consultation> findAllActives(
        @Param("prestataireId")    String        prestataireId,
        @Param("etat")             String        etat,
        @Param("typeConsultation") String        typeConsultation,
        @Param("souscripteur")     String        souscripteur,
        @Param("nomAdherent")      String        nomAdherent,
        @Param("dateMin")          LocalDateTime dateMin,
        @Param("dateMax")          LocalDateTime dateMax,
        Pageable pageable);

    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.supprime = '-1'")
    Long countAllActives();

    // ════════════════════════════════════════════════════════════════
    // CONSULTATIONS ENCAISSÉES — SQL NATIF
    // ════════════════════════════════════════════════════════════════
    @Query(value = """
        SELECT COALESCE(SUM(
            (CASE WHEN c.montant_modif IS NOT NULL AND c.montant_modif > 0
                  THEN c.montant_modif
                  ELSE c.montant END)
            * COALESCE(c.taux, 100) / 100
        ), 0)
        FROM dbx45ty_consultation c
        INNER JOIN dbx45ty_visite v ON c.visite_id = v.id
        WHERE v.code_adherent     = :codeAdherent
          AND c.etat_consultation = 'encaisse'
          AND c.supprime          = '-1'
          AND YEAR(c.date)        = :annee
    """, nativeQuery = true)
    Double sumConsultationsEncaissees(
            @Param("codeAdherent") String codeAdherent,
            @Param("annee") int annee);

    @Query(value = """
        SELECT COUNT(c.id)
        FROM dbx45ty_consultation c
        INNER JOIN dbx45ty_visite v ON c.visite_id = v.id
        WHERE v.code_adherent     = :codeAdherent
          AND c.etat_consultation = 'encaisse'
          AND c.supprime          = '-1'
          AND YEAR(c.date)        = :annee
    """, nativeQuery = true)
    Long countConsultationsEncaissees(
            @Param("codeAdherent") String codeAdherent,
            @Param("annee") int annee);

    // ════════════════════════════════════════════════════════════════
    // CONSULTATIONS EN COURS — SQL NATIF
    // ════════════════════════════════════════════════════════════════
    @Query(value = """
        SELECT COALESCE(SUM(
            (CASE WHEN c.montant_modif IS NOT NULL AND c.montant_modif > 0
                  THEN c.montant_modif
                  ELSE c.montant END)
            * COALESCE(c.taux, 100) / 100
        ), 0)
        FROM dbx45ty_consultation c
        INNER JOIN dbx45ty_visite v ON c.visite_id = v.id
        WHERE v.code_adherent     = :codeAdherent
          AND c.etat_consultation IN ('attente_validation', 'valide')
          AND c.supprime          = '-1'
          AND YEAR(c.date)        = :annee
    """, nativeQuery = true)
    Double sumConsultationsEnCours(
            @Param("codeAdherent") String codeAdherent,
            @Param("annee") int annee);

    // ════════════════════════════════════════════════════════════════
    // ORDONNANCES ENCAISSÉES — SQL NATIF
    // ════════════════════════════════════════════════════════════════
    @Query(value = """
        SELECT COALESCE(SUM(
            (CASE WHEN lp.valeur_modif IS NOT NULL AND lp.valeur_modif > 0
                  THEN lp.valeur_modif * COALESCE(lp.nbre_modif, lp.nbre)
                  ELSE lp.valeur * lp.nbre END)
            * COALESCE(lp.taux, 100) / 100
        ), 0)
        FROM dbx45ty_ligne_prestation lp
        INNER JOIN dbx45ty_prestation p ON lp.prestation_id = p.id
        INNER JOIN dbx45ty_visite     v ON p.visite_id      = v.id
        WHERE v.code_adherent       = :codeAdherent
          AND p.nature_prestation   = 'ordonnance'
          AND p.supprime            = '-1'
          AND lp.etat               = 'encaisse'
          AND lp.supprime           = '-1'
          AND YEAR(lp.date)         = :annee
    """, nativeQuery = true)
    Double sumOrdonnancesEncaissees(
            @Param("codeAdherent") String codeAdherent,
            @Param("annee") int annee);

    @Query(value = """
        SELECT COUNT(DISTINCT p.id)
        FROM dbx45ty_ligne_prestation lp
        INNER JOIN dbx45ty_prestation p ON lp.prestation_id = p.id
        INNER JOIN dbx45ty_visite     v ON p.visite_id      = v.id
        WHERE v.code_adherent       = :codeAdherent
          AND p.nature_prestation   = 'ordonnance'
          AND p.supprime            = '-1'
          AND lp.etat               = 'encaisse'
          AND lp.supprime           = '-1'
          AND YEAR(lp.date)         = :annee
    """, nativeQuery = true)
    Long countOrdonnancesEncaissees(
            @Param("codeAdherent") String codeAdherent,
            @Param("annee") int annee);

    // ════════════════════════════════════════════════════════════════
    // ORDONNANCES EN COURS — SQL NATIF
    // ════════════════════════════════════════════════════════════════
    @Query(value = """
        SELECT COALESCE(SUM(
            (CASE WHEN lp.valeur_modif IS NOT NULL AND lp.valeur_modif > 0
                  THEN lp.valeur_modif * COALESCE(lp.nbre_modif, lp.nbre)
                  ELSE lp.valeur * lp.nbre END)
            * COALESCE(lp.taux, 100) / 100
        ), 0)
        FROM dbx45ty_ligne_prestation lp
        INNER JOIN dbx45ty_prestation p ON lp.prestation_id = p.id
        INNER JOIN dbx45ty_visite     v ON p.visite_id      = v.id
        WHERE v.code_adherent       = :codeAdherent
          AND p.nature_prestation   = 'ordonnance'
          AND p.supprime            = '-1'
          AND lp.etat               IN ('attente_validation', 'valide')
          AND lp.supprime           = '-1'
          AND YEAR(lp.date)         = :annee
    """, nativeQuery = true)
    Double sumOrdonnancesEnCours(
            @Param("codeAdherent") String codeAdherent,
            @Param("annee") int annee);

    // ════════════════════════════════════════════════════════════════
    // EXAMENS ENCAISSÉS — SQL NATIF
    // ════════════════════════════════════════════════════════════════
    @Query(value = """
       SELECT COALESCE(SUM(
                                (CASE WHEN lp.valeur_modif IS NOT NULL AND lp.valeur_modif > 0
                                      THEN lp.valeur_modif * COALESCE(lp.nbre_modif, lp.nbre)
                                      ELSE lp.valeur * lp.nbre END)
                                * COALESCE(lp.taux, 100) / 100
                            ), 0)
                            FROM dbx45ty_ligne_prestation lp
                            INNER JOIN dbx45ty_prestation p ON lp.prestation_id = p.id
                            INNER JOIN dbx45ty_visite     v ON p.visite_id      = v.id
                            WHERE v.code_adherent       = :codeAdherent
                              AND p.nature_prestation   = 'examen'
                              AND p.supprime            = '-1'
                              AND lp.etat               = 'encaisse'
                              AND lp.supprime           = '-1'
                              AND YEAR(lp.date)         = :annee
    """, nativeQuery = true)
    Double sumExamensEncaisses(
            @Param("codeAdherent") String codeAdherent,
            @Param("annee") int annee);

    @Query(value = """
        SELECT COUNT(DISTINCT p.id)
        FROM dbx45ty_ligne_prestation lp
        INNER JOIN dbx45ty_prestation p ON lp.prestation_id = p.id
        INNER JOIN dbx45ty_visite     v ON p.visite_id      = v.id
        WHERE v.code_adherent       = :codeAdherent
          AND p.nature_prestation   = 'examen'
          AND p.supprime            = '-1'
          AND lp.etat               = 'encaisse'
          AND lp.supprime           = '-1'
          AND YEAR(lp.date)         = :annee
    """, nativeQuery = true)
    Long countExamensEncaisses(
            @Param("codeAdherent") String codeAdherent,
            @Param("annee") int annee);

    // ════════════════════════════════════════════════════════════════
    // EXAMENS EN COURS — SQL NATIF
    // ════════════════════════════════════════════════════════════════
    @Query(value = """
        SELECT COALESCE(SUM(
            (CASE WHEN lp.acte_prelevement_modif IS NOT NULL
                       AND lp.acte_prelevement_modif > 0
                  THEN lp.acte_prelevement_modif
                  ELSE lp.acte_prelevement END)
            * COALESCE(lp.taux, 100) / 100
        ), 0)
        FROM dbx45ty_ligne_prestation lp
        INNER JOIN dbx45ty_prestation p ON lp.prestation_id = p.id
        INNER JOIN dbx45ty_visite     v ON p.visite_id      = v.id
        WHERE v.code_adherent       = :codeAdherent
          AND p.nature_prestation   = 'examen'
          AND p.supprime            = '-1'
          AND lp.etat               IN ('attente_validation', 'valide')
          AND lp.supprime           = '-1'
          AND YEAR(lp.date)         = :annee
    """, nativeQuery = true)
    Double sumExamensEnCours(
            @Param("codeAdherent") String codeAdherent,
            @Param("annee") int annee);
    
    // ConsultationRepository.java
  
 // ← Requête native avec JOIN visite
    @Query(value = """
        SELECT COUNT(*) 
        FROM dbx45ty_consultation c
        INNER JOIN dbx45ty_visite vi 
            ON c.visite_id = vi.id
        WHERE vi.prestataire_id = :prestataireId
          AND c.etat_consultation = :etat
        """, nativeQuery = true)
    long countByPrestataireAndEtat(
        @Param("prestataireId") String prestataireId,
        @Param("etat")          String etat);

}

//package com.esphere.validation.repository;
//
//import com.esphere.validation.entity.Consultation;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
//
//    @Query("""
//        SELECT c FROM Consultation c
//        WHERE c.etatConsultation = 'attente_validation'
//        AND c.supprime = '-1'
//        ORDER BY c.date ASC
//    """)
//    List<Consultation> findEnAttente();
//
//    @Query("""
//        SELECT c FROM Consultation c
//        WHERE c.etatConsultation = 'attente_validation'
//        AND c.supprime = '-1'
//        AND c.visiteId IN (
//            SELECT v.id FROM Visite v
//            WHERE v.prestataireId = :prestataireId
//        )
//        ORDER BY c.date ASC
//    """)
//    List<Consultation> findEnAttenteByPrestataire(
//            @Param("prestataireId") String prestataireId);
//
//    @Query("SELECT c FROM Consultation c WHERE c.visiteId = :visiteId AND c.supprime = '-1'")
//    Optional<Consultation> findByVisite(@Param("visiteId") String visiteId);
//
//    /**
//     * Recherche paginée avec filtres optionnels :
//     * - prestataireId : filtre sur la visite liée
//     * - etat          : filtre sur etatConsultation
//     * - dateMin/dateMax : filtre sur la date de consultation
//     */
//    @Query("""
//        SELECT c FROM Consultation c
//        WHERE c.supprime = '-1'
//        AND c.taux IS NOT NULL
//        AND (:etat IS NULL OR c.etatConsultation = :etat)
//        AND (:dateMin IS NULL OR c.date >= :dateMin)
//        AND (:dateMax IS NULL OR c.date <= :dateMax)
//        AND (
//            :prestataireId IS NULL
//            OR c.visiteId IN (
//                SELECT v.id FROM Visite v
//                WHERE v.prestataireId = :prestataireId
//            )
//        )
//        ORDER BY
//            CASE WHEN c.etatConsultation = 'attente_validation' THEN 0
//                 WHEN c.etatConsultation = 'valide'             THEN 1
//                 WHEN c.etatConsultation = 'rejete'             THEN 2
//                 ELSE 3
//            END,
//            c.date DESC
//    """)
//    Page<Consultation> findAllActives(
//            @Param("prestataireId") String prestataireId,
//            @Param("etat")          String etat,
//            @Param("dateMin")       LocalDateTime dateMin,
//            @Param("dateMax")       LocalDateTime dateMax,
//            Pageable pageable);
//
//    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.supprime = '-1'")
//    Long countAllActives();
//    
//}
