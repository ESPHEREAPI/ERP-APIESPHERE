

package com.esphere.validation.repository;

import com.esphere.validation.entity.Prestation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrestationRepository extends JpaRepository<Prestation, Integer> {

    @Query("SELECT p FROM Prestation p WHERE p.visiteId = :visiteId AND p.supprime = '-1'")
    List<Prestation> findByVisite(@Param("visiteId") String visiteId);

    @Query(
        value = """
            SELECT DISTINCT p.*
            FROM dbx45ty_prestation p
            INNER JOIN dbx45ty_visite      v  ON p.visite_id        = v.id
            INNER JOIN dbx45ty_adherent    a  ON v.code_adherent    = a.code_adherent
            LEFT  JOIN dbx45ty_ayant_droit ad ON v.code_ayant_droit = ad.code_ayant_droit
            WHERE p.supprime           = '-1'
              AND p.nature_prestation  = :nature
              AND (:prestataireId IS NULL
                   OR p.prestataire_id = :prestataireId)
              AND (:dateMin IS NULL
                   OR p.date           >= :dateMin)
              AND (:dateMax IS NULL
                   OR p.date           <= :dateMax)
              AND (:souscripteur IS NULL
                   OR :souscripteur    = ''
                   OR UPPER(a.souscripteur)
                      LIKE UPPER(CONCAT('%', :souscripteur, '%')))
              AND (:adherent IS NULL
                   OR :adherent        = ''
                   OR UPPER(a.assure_principal)
                      LIKE UPPER(CONCAT('%', :adherent, '%')))
              AND (:ayantDroit IS NULL
                   OR :ayantDroit      = ''
                   OR UPPER(ad.nom)
                      LIKE UPPER(CONCAT('%', :ayantDroit, '%')))
              AND (
                  :etat IS NULL OR :etat = ''
                  OR (
                      -- attente_validation : toutes les lignes sont en attente
                      :etat = 'attente_validation'
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.supprime      = '-1'
                      )
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat         != 'attente_validation'
                            AND lp2.supprime      = '-1'
                      )
                  )
                  OR (
                      -- valide : aucune en attente, aucune encaissée,
                      --          aucune rejetée, au moins une validée
                      :etat = 'valide'
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'attente_validation'
                            AND lp2.supprime      = '-1'
                      )
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'encaisse'
                            AND lp2.supprime      = '-1'
                      )
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'rejete'
                            AND lp2.supprime      = '-1'
                      )
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'valide'
                            AND lp2.supprime      = '-1'
                      )
                  )
                  OR (
                      -- encaisse : aucune en attente, aucune validée pure,
                      --            mix encaissées + rejetées avec au moins
                      --            une encaissée
                      :etat = 'encaisse'
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat NOT IN ('encaisse', 'rejete')
                            AND lp2.supprime      = '-1'
                      )
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'encaisse'
                            AND lp2.supprime      = '-1'
                      )
                  )
                  OR (
                      -- partiel : au moins une en attente ET
                      --           au moins une traitée (valide/encaisse/rejete)
                      :etat = 'partiel'
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'attente_validation'
                            AND lp2.supprime      = '-1'
                      )
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          IN ('valide', 'encaisse', 'rejete')
                            AND lp2.supprime      = '-1'
                      )
                  )
              )
            ORDER BY p.date DESC
        """,
        countQuery = """
            SELECT COUNT(DISTINCT p.id)
            FROM dbx45ty_prestation p
            INNER JOIN dbx45ty_visite      v  ON p.visite_id        = v.id
            INNER JOIN dbx45ty_adherent    a  ON v.code_adherent    = a.code_adherent
            LEFT  JOIN dbx45ty_ayant_droit ad ON v.code_ayant_droit = ad.code_ayant_droit
            WHERE p.supprime           = '-1'
              AND p.nature_prestation  = :nature
              AND (:prestataireId IS NULL
                   OR p.prestataire_id = :prestataireId)
              AND (:dateMin IS NULL
                   OR p.date           >= :dateMin)
              AND (:dateMax IS NULL
                   OR p.date           <= :dateMax)
              AND (:souscripteur IS NULL
                   OR :souscripteur    = ''
                   OR UPPER(a.souscripteur)
                      LIKE UPPER(CONCAT('%', :souscripteur, '%')))
              AND (:adherent IS NULL
                   OR :adherent        = ''
                   OR UPPER(a.assure_principal)
                      LIKE UPPER(CONCAT('%', :adherent, '%')))
              AND (:ayantDroit IS NULL
                   OR :ayantDroit      = ''
                   OR UPPER(ad.nom)
                      LIKE UPPER(CONCAT('%', :ayantDroit, '%')))
              AND (
                  :etat IS NULL OR :etat = ''
                  OR (
                      :etat = 'attente_validation'
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.supprime      = '-1'
                      )
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat         != 'attente_validation'
                            AND lp2.supprime      = '-1'
                      )
                  )
                  OR (
                      :etat = 'valide'
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'attente_validation'
                            AND lp2.supprime      = '-1'
                      )
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'encaisse'
                            AND lp2.supprime      = '-1'
                      )
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'rejete'
                            AND lp2.supprime      = '-1'
                      )
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'valide'
                            AND lp2.supprime      = '-1'
                      )
                  )
                  OR (
                      :etat = 'encaisse'
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat NOT IN ('encaisse', 'rejete')
                            AND lp2.supprime      = '-1'
                      )
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'encaisse'
                            AND lp2.supprime      = '-1'
                      )
                  )
                  OR (
                      :etat = 'partiel'
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          = 'attente_validation'
                            AND lp2.supprime      = '-1'
                      )
                      AND EXISTS (
                          SELECT 1
                          FROM dbx45ty_ligne_prestation lp2
                          WHERE lp2.prestation_id = p.id
                            AND lp2.etat          IN ('valide', 'encaisse', 'rejete')
                            AND lp2.supprime      = '-1'
                      )
                  )
              )
        """,
        nativeQuery = true
    )
    Page<Prestation> findAllByNature(
        @Param("nature")        String        nature,
        @Param("prestataireId") String        prestataireId,
        @Param("etat")          String        etat,
        @Param("dateMin")       LocalDateTime dateMin,
        @Param("dateMax")       LocalDateTime dateMax,
        @Param("souscripteur")  String        souscripteur,
        @Param("adherent")      String        adherent,
        @Param("ayantDroit")    String        ayantDroit,
        Pageable pageable);
    
    Optional<Prestation> findByVisiteIdAndNaturePrestation(String VisiteId,String NaturePrestation );
}


//package com.esphere.validation.repository;
//
//import com.esphere.validation.entity.Prestation;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface PrestationRepository extends JpaRepository<Prestation, Integer> {
//
//    @Query("SELECT p FROM Prestation p WHERE p.visiteId = :visiteId AND p.supprime = '-1'")
//    List<Prestation> findByVisite(@Param("visiteId") String visiteId);
//
//    @Query(
//        value = """
//            SELECT DISTINCT p.*
//            FROM dbx45ty_prestation p
//            INNER JOIN dbx45ty_visite      v  ON p.visite_id         = v.id
//            INNER JOIN dbx45ty_adherent    a  ON v.code_adherent     = a.code_adherent
//            LEFT  JOIN dbx45ty_ayant_droit ad ON v.code_ayant_droit  = ad.code_ayant_droit
//            WHERE p.supprime           = '-1'
//              AND p.nature_prestation  = :nature
//              AND (:prestataireId IS NULL
//                   OR p.prestataire_id = :prestataireId)
//              AND (:dateMin IS NULL
//                   OR p.date          >= :dateMin)
//              AND (:dateMax IS NULL
//                   OR p.date          <= :dateMax)
//              AND (:souscripteur IS NULL
//                   OR :souscripteur   = ''
//                   OR UPPER(a.souscripteur)
//                      LIKE UPPER(CONCAT('%', :souscripteur, '%')))
//              AND (:adherent IS NULL
//                   OR :adherent       = ''
//                   OR UPPER(a.assure_principal)
//                      LIKE UPPER(CONCAT('%', :adherent, '%')))
//              AND (:ayantDroit IS NULL
//                   OR :ayantDroit     = ''
//                   OR UPPER(ad.nom)
//                      LIKE UPPER(CONCAT('%', :ayantDroit, '%')))
//              AND (
//                  :etat IS NULL OR :etat = ''
//                  OR (
//                      :etat = 'attente_validation'
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND NOT EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat         != 'attente_validation'
//                            AND lp2.supprime      = '-1'
//                      )
//                  )
//                  OR (
//                      :etat = 'valide'
//                      AND NOT EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          = 'attente_validation'
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND NOT EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          = 'encaisse'
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          = 'valide'
//                            AND lp2.supprime      = '-1'
//                      )
//                  )
//                  OR (
//                      :etat = 'encaisse'
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND NOT EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat         != 'encaisse'
//                            AND lp2.supprime      = '-1'
//                      )
//                  )
//                  OR (
//                      :etat = 'partiel'
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          = 'attente_validation'
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          IN ('valide', 'encaisse')
//                            AND lp2.supprime      = '-1'
//                      )
//                  )
//              )
//            ORDER BY p.date DESC
//        """,
//        countQuery = """
//            SELECT COUNT(DISTINCT p.id)
//            FROM dbx45ty_prestation p
//            INNER JOIN dbx45ty_visite      v  ON p.visite_id         = v.id
//            INNER JOIN dbx45ty_adherent    a  ON v.code_adherent     = a.code_adherent
//            LEFT  JOIN dbx45ty_ayant_droit ad ON v.code_ayant_droit  = ad.code_ayant_droit
//            WHERE p.supprime           = '-1'
//              AND p.nature_prestation  = :nature
//              AND (:prestataireId IS NULL
//                   OR p.prestataire_id = :prestataireId)
//              AND (:dateMin IS NULL
//                   OR p.date          >= :dateMin)
//              AND (:dateMax IS NULL
//                   OR p.date          <= :dateMax)
//              AND (:souscripteur IS NULL
//                   OR :souscripteur   = ''
//                   OR UPPER(a.souscripteur)
//                      LIKE UPPER(CONCAT('%', :souscripteur, '%')))
//              AND (:adherent IS NULL
//                   OR :adherent       = ''
//                   OR UPPER(a.assure_principal)
//                      LIKE UPPER(CONCAT('%', :adherent, '%')))
//              AND (:ayantDroit IS NULL
//                   OR :ayantDroit     = ''
//                   OR UPPER(ad.nom)
//                      LIKE UPPER(CONCAT('%', :ayantDroit, '%')))
//              AND (
//                  :etat IS NULL OR :etat = ''
//                  OR (
//                      :etat = 'attente_validation'
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND NOT EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat         != 'attente_validation'
//                            AND lp2.supprime      = '-1'
//                      )
//                  )
//                  OR (
//                      :etat = 'valide'
//                      AND NOT EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          = 'attente_validation'
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND NOT EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          = 'encaisse'
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          = 'valide'
//                            AND lp2.supprime      = '-1'
//                      )
//                  )
//                  OR (
//                      :etat = 'encaisse'
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND NOT EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat         != 'encaisse'
//                            AND lp2.supprime      = '-1'
//                      )
//                  )
//                  OR (
//                      :etat = 'partiel'
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          = 'attente_validation'
//                            AND lp2.supprime      = '-1'
//                      )
//                      AND EXISTS (
//                          SELECT 1
//                          FROM dbx45ty_ligne_prestation lp2
//                          WHERE lp2.prestation_id = p.id
//                            AND lp2.etat          IN ('valide', 'encaisse')
//                            AND lp2.supprime      = '-1'
//                      )
//                  )
//              )
//        """,
//        nativeQuery = true
//    )
//    Page<Prestation> findAllByNature(
//        @Param("nature")        String        nature,
//        @Param("prestataireId") String        prestataireId,
//        @Param("etat")          String        etat,
//        @Param("dateMin")       LocalDateTime dateMin,
//        @Param("dateMax")       LocalDateTime dateMax,
//        @Param("souscripteur")  String        souscripteur,
//        @Param("adherent")      String        adherent,
//        @Param("ayantDroit")    String        ayantDroit,
//        Pageable pageable);
//}