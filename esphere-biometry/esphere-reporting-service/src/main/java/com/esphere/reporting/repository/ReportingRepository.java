package com.esphere.reporting.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ReportingRepository {

    @PersistenceContext
    private EntityManager em;

    // ══════════════════════════════════════════════════════════════
    // AGENT SS — COMPTEURS EN ATTENTE
    // ══════════════════════════════════════════════════════════════
    public Long countConsultationsEnAttente() {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_consultation c "
                + "JOIN dbx45ty_visite vi  ON vi.id = c.visite_id "
                + "JOIN dbx45ty_adherent a  ON a.code_adherent = vi.code_adherent "
                + "WHERE etat_consultation = 'attente_validation' AND supprime = '-1'"
                + " AND a.effet_police    IS NOT NULL "
                + "                AND a.echeance_police IS NOT NULL "
                + "                AND a.effet_police    <= CURDATE() "
                + "                AND a.echeance_police >= CURDATE()")
                .getSingleResult();
    }

    public Long countOrdonnancesEnAttente() {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "JOIN dbx45ty_visite vi  ON vi.id = p.visite_id "
                + "JOIN dbx45ty_adherent a  ON a.code_adherent = vi.code_adherent "
                + "WHERE p.nature_prestation = 'ordonnance' "
                + "AND l.etat = 'attente_validation' AND p.supprime = '-1'"
                + " AND a.effet_police    IS NOT NULL "
                + "                AND a.echeance_police IS NOT NULL "
                + "                AND a.effet_police    <= CURDATE() "
                + "                AND a.echeance_police >= CURDATE()")
                .getSingleResult();
    }

    public Long countExamensEnAttente() {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "JOIN dbx45ty_visite vi  ON vi.id = p.visite_id "
                + "JOIN dbx45ty_adherent a  ON a.code_adherent = vi.code_adherent "
                + "WHERE p.nature_prestation = 'examen' "
                + "AND l.etat = 'attente_validation' AND p.supprime = '-1'"
                + " AND a.effet_police    IS NOT NULL "
                + "                AND a.echeance_police IS NOT NULL "
                + "                AND a.effet_police    <= CURDATE() "
                + "                AND a.echeance_police >= CURDATE()")
                .getSingleResult();
    }

    public Long countBonsManuelEnAttente() {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_bon_manuel "
                + "WHERE statut = 'en_attente' AND supprime = '-1'")
                .getSingleResult();
    }

    // ══════════════════════════════════════════════════════════════
    // AGENT SS — VALIDÉS CE MOIS (consultations + ordonnances + examens)
    // ══════════════════════════════════════════════════════════════
    public Long countValidesParMois(int annee, int mois) {
        // Consultations validées
        Long consultations = (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_consultation "
                + "WHERE etat_consultation = 'valide' "
                + "AND YEAR(date_valide_rejete) = :annee "
                + "AND MONTH(date_valide_rejete) = :mois "
                + "AND supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        // Ordonnances validées
        Long ordonnances = (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.nature_prestation = 'ordonnance' "
                + "AND l.etat = 'valide' "
                + "AND YEAR(l.date) = :annee AND MONTH(l.date) = :mois "
                + "AND p.supprime = '-1' AND l.supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        // Examens validés
        Long examens = (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.nature_prestation = 'examen' "
                + "AND l.etat = 'valide' "
                + "AND YEAR(l.date) = :annee AND MONTH(l.date) = :mois "
                + "AND p.supprime = '-1' AND l.supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        return (consultations != null ? consultations : 0L)
                + (ordonnances != null ? ordonnances : 0L)
                + (examens != null ? examens : 0L);
    }

    // ══════════════════════════════════════════════════════════════
    // AGENT SS — REJETÉS CE MOIS (consultations + ordonnances + examens)
    // ══════════════════════════════════════════════════════════════
    public Long countRejetesMois(int annee, int mois) {
        // Consultations rejetées
        Long consultations = (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_consultation "
                + "WHERE etat_consultation = 'rejete' "
                + "AND YEAR(date_valide_rejete) = :annee "
                + "AND MONTH(date_valide_rejete) = :mois "
                + "AND supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        // Ordonnances rejetées
        Long ordonnances = (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.nature_prestation = 'ordonnance' "
                + "AND l.etat = 'rejete' "
                + "AND YEAR(l.date) = :annee AND MONTH(l.date) = :mois "
                + "AND p.supprime = '-1' AND l.supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        // Examens rejetés
        Long examens = (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.nature_prestation = 'examen' "
                + "AND l.etat = 'rejete' "
                + "AND YEAR(l.date) = :annee AND MONTH(l.date) = :mois "
                + "AND p.supprime = '-1' AND l.supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        return (consultations != null ? consultations : 0L)
                + (ordonnances != null ? ordonnances : 0L)
                + (examens != null ? examens : 0L);
    }

    // ══════════════════════════════════════════════════════════════
    // AGENT SS — ENCAISSÉS CE MOIS (consultations + ordonnances + examens)
    // ══════════════════════════════════════════════════════════════
    public Long countEncaissesMois(int annee, int mois) {
        // Consultations encaissées
        Long consultations = (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_consultation "
                + "WHERE etat_consultation = 'encaisse' "
                + "AND YEAR(date) = :annee AND MONTH(date) = :mois "
                + "AND supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        // Ordonnances encaissées
        Long ordonnances = (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.nature_prestation = 'ordonnance' "
                + "AND l.etat = 'encaisse' "
                + "AND YEAR(l.date) = :annee AND MONTH(l.date) = :mois "
                + "AND p.supprime = '-1' AND l.supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        // Examens encaissés
        Long examens = (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.nature_prestation = 'examen' "
                + "AND l.etat = 'encaisse' "
                + "AND YEAR(l.date) = :annee AND MONTH(l.date) = :mois "
                + "AND p.supprime = '-1' AND l.supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        return (consultations != null ? consultations : 0L)
                + (ordonnances != null ? ordonnances : 0L)
                + (examens != null ? examens : 0L);
    }

    // ══════════════════════════════════════════════════════════════
    // MONTANT ENCAISSÉ CE MOIS
    // Règle : taux = 100% → montant_modif * nbre_modif
    //         taux < 100% → (montant_modif * nbre_modif) * taux / 100
    // Pour consultation : montant_modif (pas de nbre)
    // ══════════════════════════════════════════════════════════════
    public Double montantEncaisseMois(int annee, int mois) {
        // Montant consultations encaissées
        Object resConsultations = em.createNativeQuery(
                "SELECT COALESCE(SUM("
                + "  CASE WHEN taux = 100 OR taux IS NULL "
                + "       THEN COALESCE(montant_modif, montant) "
                + "       ELSE COALESCE(montant_modif, montant) * taux / 100 "
                + "  END"
                + "), 0) "
                + "FROM dbx45ty_consultation "
                + "WHERE etat_consultation = 'encaisse' "
                + "AND YEAR(date) = :annee AND MONTH(date) = :mois "
                + "AND supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        // Montant lignes prestation encaissées (ordonnances + examens)
        Object resLignes = em.createNativeQuery(
                "SELECT COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) "
                + "FROM dbx45ty_ligne_prestation l "
                + "WHERE l.etat = 'encaisse' "
                + "AND YEAR(l.date) = :annee AND MONTH(l.date) = :mois "
                + "AND l.supprime = '-1'")
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();

        double montantC = resConsultations != null ? ((Number) resConsultations).doubleValue() : 0.0;
        double montantL = resLignes != null ? ((Number) resLignes).doubleValue() : 0.0;
        return montantC + montantL;
    }

    // ══════════════════════════════════════════════════════════════
    // VISITES ANNEE
    // ══════════════════════════════════════════════════════════════
    public Long countVisitesAnnee(int annee) {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_visite "
                + "WHERE YEAR(date) = :annee")
                .setParameter("annee", annee)
                .getSingleResult();
    }

    // ══════════════════════════════════════════════════════════════
    // MONTANT TOTAL ANNEE (consultations + lignes prestation)
    // Règle taux appliquée
    // ══════════════════════════════════════════════════════════════
    public Double montantTotalAnnee(int annee) {
        // Consultations encaissées
        Object resC = em.createNativeQuery(
                "SELECT COALESCE(SUM("
                + "  CASE WHEN taux = 100 OR taux IS NULL "
                + "       THEN COALESCE(montant_modif, montant) "
                + "       ELSE COALESCE(montant_modif, montant) * taux / 100 "
                + "  END"
                + "), 0) "
                + "FROM dbx45ty_consultation "
                + "WHERE etat_consultation = 'encaisse' "
                + "AND YEAR(date) = :annee AND supprime = '-1'")
                .setParameter("annee", annee)
                .getSingleResult();

        // Lignes prestation encaissées
        Object resL = em.createNativeQuery(
                "SELECT COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) "
                + "FROM dbx45ty_ligne_prestation l "
                + "WHERE l.etat = 'encaisse' "
                + "AND YEAR(l.date) = :annee AND l.supprime = '-1'")
                .setParameter("annee", annee)
                .getSingleResult();

        double c = resC != null ? ((Number) resC).doubleValue() : 0.0;
        double l = resL != null ? ((Number) resL).doubleValue() : 0.0;
        return c + l;
    }

    // ══════════════════════════════════════════════════════════════
    // GRAPHIQUE — CONSULTATIONS PAR MOIS (encaissées)
    // ══════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    public List<Object[]> consultationsParMois(int annee) {
        return em.createNativeQuery(
                "SELECT MONTH(date) as mois, COUNT(*) as nombre "
                + "FROM dbx45ty_consultation "
                + "WHERE YEAR(date) = :annee AND supprime = '-1' "
                + "AND etat_consultation = 'encaisse' "
                + "GROUP BY MONTH(date) ORDER BY mois")
                .setParameter("annee", annee)
                .getResultList();
    }

    // ══════════════════════════════════════════════════════════════
    // GRAPHIQUE — ORDONNANCES PAR MOIS (encaissées)
    // ══════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    public List<Object[]> ordonnancesParMois(int annee) {
        return em.createNativeQuery(
                "SELECT MONTH(p.date) as mois, COUNT(DISTINCT p.id) as nombre "
                + "FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.nature_prestation = 'ordonnance' "
                + "AND YEAR(p.date) = :annee AND p.supprime = '-1' "
                + "AND l.etat = 'encaisse' AND l.supprime = '-1' "
                + "GROUP BY MONTH(p.date) ORDER BY mois")
                .setParameter("annee", annee)
                .getResultList();
    }

    // ══════════════════════════════════════════════════════════════
    // GRAPHIQUE — EXAMENS PAR MOIS (encaissés)
    // ══════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    public List<Object[]> examensParMois(int annee) {
        return em.createNativeQuery(
                "SELECT MONTH(p.date) as mois, COUNT(DISTINCT p.id) as nombre "
                + "FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.nature_prestation = 'examen' "
                + "AND YEAR(p.date) = :annee AND p.supprime = '-1' "
                + "AND l.etat = 'encaisse' AND l.supprime = '-1' "
                + "GROUP BY MONTH(p.date) ORDER BY mois")
                .setParameter("annee", annee)
                .getResultList();
    }

    // ══════════════════════════════════════════════════════════════
    // GRAPHIQUE — MONTANTS PAR MOIS (consultations + lignes, encaissés)
    // Règle taux appliquée
    // ══════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    public List<Object[]> montantsParMois(int annee) {
        // Consultations encaissées par mois
        List<Object[]> consult = em.createNativeQuery(
                "SELECT MONTH(date) as mois, "
                + "COALESCE(SUM("
                + "  CASE WHEN taux = 100 OR taux IS NULL "
                + "       THEN COALESCE(montant_modif, montant) "
                + "       ELSE COALESCE(montant_modif, montant) * taux / 100 "
                + "  END"
                + "), 0) as montant "
                + "FROM dbx45ty_consultation "
                + "WHERE YEAR(date) = :annee AND supprime = '-1' "
                + "AND etat_consultation = 'encaisse' "
                + "GROUP BY MONTH(date) ORDER BY mois")
                .setParameter("annee", annee)
                .getResultList();

        // Lignes prestation encaissées par mois
        List<Object[]> lignes = em.createNativeQuery(
                "SELECT MONTH(l.date) as mois, "
                + "COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) as montant "
                + "FROM dbx45ty_ligne_prestation l "
                + "WHERE YEAR(l.date) = :annee AND l.supprime = '-1' "
                + "AND l.etat = 'encaisse' "
                + "GROUP BY MONTH(l.date) ORDER BY mois")
                .setParameter("annee", annee)
                .getResultList();

        // Fusionner les deux listes par mois
        double[] totaux = new double[13];
        for (Object[] row : consult) {
            int mois = ((Number) row[0]).intValue();
            totaux[mois] += ((Number) row[1]).doubleValue();
        }
        for (Object[] row : lignes) {
            int mois = ((Number) row[0]).intValue();
            totaux[mois] += ((Number) row[1]).doubleValue();
        }

        List<Object[]> result = new java.util.ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            if (totaux[m] > 0) {
                result.add(new Object[]{m, totaux[m]});
            }
        }
        return result;
    }

    // ══════════════════════════════════════════════════════════════
    // TOP PRESTATAIRES
    // ══════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    public List<Object[]> topPrestataires(int annee, int limit) {
        return em.createNativeQuery(
                "SELECT p.prestataire_id, "
                + "COUNT(DISTINCT p.id) as nombre, "
                + "COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) as montant "
                + "FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE YEAR(p.date) = :annee AND p.supprime = '-1' "
                + "AND l.etat = 'encaisse' AND l.supprime = '-1' "
                + "GROUP BY p.prestataire_id "
                + "ORDER BY montant DESC "
                + "LIMIT :limit")
                .setParameter("annee", annee)
                .setParameter("limit", limit)
                .getResultList();
    }

    // ══════════════════════════════════════════════════════════════
    // PRESTATAIRE — VISITES AUJOURD'HUI
    // ══════════════════════════════════════════════════════════════
    public Long countVisitesAujourdhui(String prestataireId) {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_visite "
                + "WHERE prestataire_id = :prestataireId "
                + "AND DATE(date) = CURDATE()")
                .setParameter("prestataireId", prestataireId)
                .getSingleResult();
    }

    public Long countPrestationsEnAttenteAujourdhui(String prestataireId) {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.prestataire_id = :prestataireId "
                + "AND l.etat = 'attente_validation' "
                + "AND DATE(p.date) = CURDATE() AND p.supprime = '-1'")
                .setParameter("prestataireId", prestataireId)
                .getSingleResult();
    }

    public Long countParNatureEtMois(String prestataireId,
            String nature, int annee, int mois) {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
                + "WHERE p.prestataire_id = :prestataireId "
                + "AND p.nature_prestation = :nature "
                + "AND YEAR(p.date) = :annee AND MONTH(p.date) = :mois "
                + "AND p.supprime = '-1'")
                .setParameter("prestataireId", prestataireId)
                .setParameter("nature", nature)
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();
    }

    public Long countBonsManuelsMois(String prestataireId, int annee, int mois) {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_bon_manuel "
                + "WHERE prestataire_id = :prestataireId "
                + "AND YEAR(date_creation) = :annee "
                + "AND MONTH(date_creation) = :mois "
                + "AND supprime = '-1'")
                .setParameter("prestataireId", prestataireId)
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();
    }

    public Long countLignesParEtatEtMois(String prestataireId,
            String etat, int annee, int mois) {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_ligne_prestation l "
                + "WHERE l.prestataire_id = :prestataireId "
                + "AND l.etat = :etat "
                + "AND YEAR(l.date) = :annee AND MONTH(l.date) = :mois "
                + "AND l.supprime = '-1'")
                .setParameter("prestataireId", prestataireId)
                .setParameter("etat", etat)
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();
    }

    // ══════════════════════════════════════════════════════════════
    // PRESTATAIRE — MONTANT ENCAISSÉ PAR MOIS (règle taux)
    // ══════════════════════════════════════════════════════════════
    public Double montantEncaisseParMois(String prestataireId,
            int annee, int mois) {
        Object result = em.createNativeQuery(
                "SELECT COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) "
                + "FROM dbx45ty_ligne_prestation l "
                + "WHERE l.prestataire_id = :prestataireId "
                + "AND l.etat = 'encaisse' "
                + "AND YEAR(l.date_encaisse) = :annee "
                + "AND MONTH(l.date_encaisse) = :mois "
                + "AND l.supprime = '-1'")
                .setParameter("prestataireId", prestataireId)
                .setParameter("annee", annee)
                .setParameter("mois", mois)
                .getSingleResult();
        return result != null ? ((Number) result).doubleValue() : 0.0;
    }

    public Long countVisitesPrestataire(String prestataireId, int annee) {
        return (Long) em.createNativeQuery(
                "SELECT COUNT(*) FROM dbx45ty_visite "
                + "WHERE prestataire_id = :prestataireId "
                + "AND YEAR(date) = :annee")
                .setParameter("prestataireId", prestataireId)
                .setParameter("annee", annee)
                .getSingleResult();
    }

    // ══════════════════════════════════════════════════════════════
    // PRESTATAIRE — MONTANT TOTAL ANNEE (règle taux)
    // ══════════════════════════════════════════════════════════════
    public Double montantTotalPrestataire(String prestataireId, int annee) {
        Object result = em.createNativeQuery(
                "SELECT COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) "
                + "FROM dbx45ty_ligne_prestation l "
                + "WHERE l.prestataire_id = :prestataireId "
                + "AND l.etat = 'encaisse' "
                + "AND YEAR(l.date) = :annee "
                + "AND l.supprime = '-1'")
                .setParameter("prestataireId", prestataireId)
                .setParameter("annee", annee)
                .getSingleResult();
        return result != null ? ((Number) result).doubleValue() : 0.0;
    }

    // ══════════════════════════════════════════════════════════════
    // PRESTATAIRE — ENCAISSEMENTS PAR MOIS (règle taux)
    // ══════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    public List<Object[]> encaissementsParMois(String prestataireId, int annee) {
        return em.createNativeQuery(
                "SELECT MONTH(l.date_encaisse) as mois, "
                + "COUNT(*) as nombre, "
                + "COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) as montant "
                + "FROM dbx45ty_ligne_prestation l "
                + "WHERE l.prestataire_id = :prestataireId "
                + "AND l.etat = 'encaisse' "
                + "AND YEAR(l.date_encaisse) = :annee "
                + "AND l.supprime = '-1' "
                + "GROUP BY MONTH(l.date_encaisse) ORDER BY mois")
                .setParameter("prestataireId", prestataireId)
                .setParameter("annee", annee)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> repartitionParType(String prestataireId, int annee) {
        return em.createNativeQuery(
                "SELECT p.nature_prestation, COUNT(DISTINCT p.id) as nombre, "
                + "COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) as montant "
                + "FROM dbx45ty_prestation p "
                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
                + "WHERE p.prestataire_id = :prestataireId "
                + "AND YEAR(p.date) = :annee AND p.supprime = '-1' "
                + "AND l.etat = 'encaisse' AND l.supprime = '-1' "
                + "GROUP BY p.nature_prestation")
                .setParameter("prestataireId", prestataireId)
                .setParameter("annee", annee)
                .getResultList();
    }

    // ══════════════════════════════════════════════════════════════
    // CONSOMMATION ADHERENT (règle taux)
    // ══════════════════════════════════════════════════════════════
    public Double montantConsommeAdherent(String codeAdherent,
            String nature, int annee) {
        Object result = em.createNativeQuery(
                "SELECT COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) "
                + "FROM dbx45ty_ligne_prestation l "
                + "JOIN dbx45ty_prestation p ON p.id = l.prestation_id "
                + "JOIN dbx45ty_visite v ON v.id = p.visite_id "
                + "WHERE v.code_adherent = :codeAdherent "
                + "AND p.nature_prestation = :nature "
                + "AND l.etat = 'encaisse' "
                + "AND YEAR(l.date) = :annee "
                + "AND l.supprime = '-1' AND p.supprime = '-1'")
                .setParameter("codeAdherent", codeAdherent)
                .setParameter("nature", nature)
                .setParameter("annee", annee)
                .getSingleResult();
        return result != null ? ((Number) result).doubleValue() : 0.0;
    }

    public Double montantConsommeConsultation(String codeAdherent, int annee) {
        Object result = em.createNativeQuery(
                "SELECT COALESCE(SUM("
                + "  CASE WHEN taux = 100 OR taux IS NULL "
                + "       THEN COALESCE(montant_modif, montant) "
                + "       ELSE COALESCE(montant_modif, montant) * taux / 100 "
                + "  END"
                + "), 0) "
                + "FROM dbx45ty_consultation c "
                + "JOIN dbx45ty_visite v ON v.id = c.visite_id "
                + "WHERE v.code_adherent = :codeAdherent "
                + "AND c.etat_consultation = 'encaisse' "
                + "AND YEAR(c.date) = :annee AND c.supprime = '-1'")
                .setParameter("codeAdherent", codeAdherent)
                .setParameter("annee", annee)
                .getSingleResult();
        return result != null ? ((Number) result).doubleValue() : 0.0;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> consommationParMoisAdherent(
            String codeAdherent, int annee) {
        return em.createNativeQuery(
                "SELECT MONTH(l.date) as mois, "
                + "COUNT(*) as nombre, "
                + "COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) as montant "
                + "FROM dbx45ty_ligne_prestation l "
                + "JOIN dbx45ty_prestation p ON p.id = l.prestation_id "
                + "JOIN dbx45ty_visite v ON v.id = p.visite_id "
                + "WHERE v.code_adherent = :codeAdherent "
                + "AND l.etat = 'encaisse' "
                + "AND YEAR(l.date) = :annee "
                + "AND l.supprime = '-1' AND p.supprime = '-1' "
                + "GROUP BY MONTH(l.date) ORDER BY mois")
                .setParameter("codeAdherent", codeAdherent)
                .setParameter("annee", annee)
                .getResultList();
    }

    // ══════════════════════════════════════════════════════════════
    // ÉTAT DES PRESTATIONS — PRESTATAIRE (liste paginée + montants)
    // ══════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    public List<Object[]> etatPrestationsPrestataire(
            String prestataireId, String nature, String statut,
            Integer mois, int annee, int page, int size) {

        // etat_global et nbre_lignes sont calculés depuis les lignes
        // (ces colonnes n'existent pas dans dbx45ty_prestation)
        StringBuilder sql = new StringBuilder(
            "SELECT p.id, p.nature_prestation, p.date, "
            + "  v.code_adherent, v.code_ayant_droit, "
            + "  a.assure_principal as nom_assure, "
            + "  ad.nom as nom_ayant_droit, "
            + "  a.souscripteur, "
            + "  COALESCE(a.taux, 100) as taux, "
            + "  COUNT(l.id) as nbre_lignes, "
            + "  COALESCE(SUM(l.valeur * l.nbre), 0) as montant_soumis, "
            + "  COALESCE(SUM(CASE WHEN l.etat IN ('valide','encaisse') "
            + "    THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
            + "    ELSE 0 END), 0) as montant_valide, "
            + "  CASE "
            + "    WHEN SUM(CASE WHEN l.etat = 'encaisse'          THEN 1 ELSE 0 END) = COUNT(l.id) AND COUNT(l.id) > 0 THEN 'encaisse' "
            + "    WHEN SUM(CASE WHEN l.etat IN ('valide','encaisse') THEN 1 ELSE 0 END) = COUNT(l.id) AND COUNT(l.id) > 0 THEN 'valide' "
            + "    WHEN SUM(CASE WHEN l.etat = 'rejete'             THEN 1 ELSE 0 END) = COUNT(l.id) AND COUNT(l.id) > 0 THEN 'rejete' "
            + "    ELSE 'attente_validation' "
            + "  END as etat_calcule "
            + "FROM dbx45ty_prestation p "
            + "LEFT JOIN dbx45ty_visite v ON v.id = p.visite_id "
            + "LEFT JOIN dbx45ty_adherent a ON a.code_adherent = v.code_adherent "
            + "LEFT JOIN dbx45ty_ayant_droit ad ON ad.code_ayant_droit = v.code_ayant_droit "
            + "LEFT JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id AND l.supprime = '-1' "
            + "WHERE p.prestataire_id = :prestataireId "
            + "  AND YEAR(p.date) = :annee "
            + "  AND p.supprime = '-1' "
        );
        if (nature != null && !nature.isBlank()) sql.append("AND p.nature_prestation = :nature ");
        if (mois != null && mois > 0)            sql.append("AND MONTH(p.date) = :mois ");
        sql.append("GROUP BY p.id, p.nature_prestation, p.date, v.code_adherent, v.code_ayant_droit, a.assure_principal, ad.nom, a.souscripteur, a.taux ");
        if (statut != null && !statut.isBlank())  sql.append("HAVING etat_calcule = :statut ");
        sql.append("ORDER BY p.date DESC ");
        sql.append("LIMIT :size OFFSET :offset");

        var q = em.createNativeQuery(sql.toString())
                .setParameter("prestataireId", prestataireId)
                .setParameter("annee", annee)
                .setParameter("size", size)
                .setParameter("offset", page * size);
        if (nature != null && !nature.isBlank()) q.setParameter("nature", nature);
        if (statut != null && !statut.isBlank())  q.setParameter("statut", statut);
        if (mois != null && mois > 0)             q.setParameter("mois", mois);
        return q.getResultList();
    }

    public Long countEtatPrestationsPrestataire(
            String prestataireId, String nature, String statut,
            Integer mois, int annee) {

        // Pour le count avec filtre statut on utilise une sous-requête
        StringBuilder inner = new StringBuilder(
            "SELECT p.id, "
            + "CASE "
            + "  WHEN SUM(CASE WHEN l.etat = 'encaisse'             THEN 1 ELSE 0 END) = COUNT(l.id) AND COUNT(l.id) > 0 THEN 'encaisse' "
            + "  WHEN SUM(CASE WHEN l.etat IN ('valide','encaisse')  THEN 1 ELSE 0 END) = COUNT(l.id) AND COUNT(l.id) > 0 THEN 'valide' "
            + "  WHEN SUM(CASE WHEN l.etat = 'rejete'                THEN 1 ELSE 0 END) = COUNT(l.id) AND COUNT(l.id) > 0 THEN 'rejete' "
            + "  ELSE 'attente_validation' "
            + "END as etat_calcule "
            + "FROM dbx45ty_prestation p "
            + "LEFT JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id AND l.supprime = '-1' "
            + "WHERE p.prestataire_id = :prestataireId "
            + "  AND YEAR(p.date) = :annee "
            + "  AND p.supprime = '-1' "
        );
        if (nature != null && !nature.isBlank()) inner.append("AND p.nature_prestation = :nature ");
        if (mois != null && mois > 0)            inner.append("AND MONTH(p.date) = :mois ");
        inner.append("GROUP BY p.id ");
        if (statut != null && !statut.isBlank())  inner.append("HAVING etat_calcule = :statut ");

        String sql = "SELECT COUNT(*) FROM (" + inner + ") AS sub";

        var q = em.createNativeQuery(sql)
                .setParameter("prestataireId", prestataireId)
                .setParameter("annee", annee);
        if (nature != null && !nature.isBlank()) q.setParameter("nature", nature);
        if (statut != null && !statut.isBlank())  q.setParameter("statut", statut);
        if (mois != null && mois > 0)             q.setParameter("mois", mois);
        return ((Number) q.getSingleResult()).longValue();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> consommationAyantsDroit(
            String codeAdherent, int annee) {
        return em.createNativeQuery(
                "SELECT v.code_ayant_droit, "
                + "COALESCE(SUM("
                + "  CASE WHEN l.taux = 100 OR l.taux IS NULL "
                + "       THEN COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) "
                + "       ELSE COALESCE(l.valeur_modif, l.valeur) * COALESCE(l.nbre_modif, l.nbre) * l.taux / 100 "
                + "  END"
                + "), 0) as montant "
                + "FROM dbx45ty_ligne_prestation l "
                + "JOIN dbx45ty_prestation p ON p.id = l.prestation_id "
                + "JOIN dbx45ty_visite v ON v.id = p.visite_id "
                + "WHERE v.code_adherent = :codeAdherent "
                + "AND v.code_ayant_droit IS NOT NULL "
                + "AND l.etat = 'encaisse' "
                + "AND YEAR(l.date) = :annee "
                + "AND l.supprime = '-1' AND p.supprime = '-1' "
                + "GROUP BY v.code_ayant_droit")
                .setParameter("codeAdherent", codeAdherent)
                .setParameter("annee", annee)
                .getResultList();
    }
    
    
}

//package com.esphere.reporting.repository;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Slf4j
//@Repository
//public class ReportingRepository {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    // ── AGENT SS ─────────────────────────────────────────────────
//    public Long countConsultationsEnAttente() {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_consultation "
//                + "WHERE etat_consultation = 'attente_validation' AND supprime = '-1'")
//                .getSingleResult();
//    }
//
//    public Long countOrdonnancesEnAttente() {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
//                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
//                + "WHERE p.nature_prestation = 'ordonnance' "
//                + "AND l.etat = 'attente_validation' AND p.supprime = '-1'")
//                .getSingleResult();
//    }
//
//    public Long countExamensEnAttente() {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
//                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
//                + "WHERE p.nature_prestation = 'examen' "
//                + "AND l.etat = 'attente_validation' AND p.supprime = '-1'")
//                .getSingleResult();
//    }
//
//    public Long countBonsManuelEnAttente() {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_bon_manuel "
//                + "WHERE statut = 'en_attente' AND supprime = '-1'")
//                .getSingleResult();
//    }
//
//    public Long countValidesParMois(int annee, int mois) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_consultation "
//                + "WHERE etat_consultation = 'valide' "
//                + "AND YEAR(date_valide_rejete) = :annee "
//                + "AND MONTH(date_valide_rejete) = :mois "
//                + "AND supprime = '-1'")
//                .setParameter("annee", annee)
//                .setParameter("mois", mois)
//                .getSingleResult();
//    }
//
//    public Long countRejetesMois(int annee, int mois) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_consultation "
//                + "WHERE etat_consultation = 'rejete' "
//                + "AND YEAR(date_valide_rejete) = :annee "
//                + "AND MONTH(date_valide_rejete) = :mois "
//                + "AND supprime = '-1'")
//                .setParameter("annee", annee)
//                .setParameter("mois", mois)
//                .getSingleResult();
//    }
//
//    public Long countEncaissesMois(int annee, int mois) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_consultation "
//                + "WHERE etat_consultation = 'encaisse' "
//                + "AND YEAR(date) = :annee "
//                + "AND MONTH(date) = :mois "
//                + "AND supprime = '-1'")
//                .setParameter("annee", annee)
//                .setParameter("mois", mois)
//                .getSingleResult();
//    }
//
//    public Double montantEncaisseMois(int annee, int mois) {
//        Object result = em.createNativeQuery(
//                "SELECT COALESCE(SUM(montant_modif), SUM(montant), 0) "
//                + "FROM dbx45ty_consultation "
//                + "WHERE etat_consultation = 'encaisse' "
//                + "AND YEAR(date) = :annee "
//                + "AND MONTH(date) = :mois "
//                + "AND supprime = '-1'")
//                .setParameter("annee", annee)
//                .setParameter("mois", mois)
//                .getSingleResult();
//        return result != null ? ((Number) result).doubleValue() : 0.0;
//    }
//
//    public Long countVisitesAnnee(int annee) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_visite "
//                + "WHERE YEAR(date) = :annee")
//                .setParameter("annee", annee)
//                .getSingleResult();
//    }
//
//    public Double montantTotalAnnee(int annee) {
//        Object result = em.createNativeQuery(
//                "SELECT COALESCE(SUM(COALESCE(montant_modif, montant)), 0) "
//                + "FROM dbx45ty_consultation "
//                + "WHERE YEAR(date) = :annee AND supprime = '-1'")
//                .setParameter("annee", annee)
//                .getSingleResult();
//        return result != null ? ((Number) result).doubleValue() : 0.0;
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Object[]> consultationsParMois(int annee) {
//        return em.createNativeQuery(
//                "SELECT MONTH(date) as mois, COUNT(*) as nombre "
//                + "FROM dbx45ty_consultation "
//                + "WHERE YEAR(date) = :annee AND supprime = '-1' "
//                + "AND etat_consultation = 'encaisse' "
//                + "GROUP BY MONTH(date) ORDER BY mois")
//                .setParameter("annee", annee)
//                .getResultList();
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Object[]> ordonnancesParMois(int annee) {
////        return em.createNativeQuery(
////            "SELECT MONTH(p.date) as mois, COUNT(DISTINCT p.id) as nombre " +
////            "FROM dbx45ty_prestation p " +
////            "WHERE p.nature_prestation = 'ordonnance' " +
////            "AND YEAR(p.date) = :annee AND p.supprime = '-1' " +
////            "GROUP BY MONTH(p.date) ORDER BY mois")
////                .setParameter("annee", annee)
////                .getResultList();
//        return em.createNativeQuery(
//                "SELECT MONTH(p.date) as mois, COUNT(DISTINCT p.id) as nombre "
//                + "FROM dbx45ty_prestation p "
//                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
//                + "WHERE p.nature_prestation = 'ordonnance' "
//                + "AND YEAR(p.date) = :annee AND p.supprime = '-1' "
//                + "AND l.etat = 'encaisse' AND l.supprime = '-1' "
//                + "GROUP BY MONTH(p.date) ORDER BY mois")
//                .setParameter("annee", annee)
//                .getResultList();
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Object[]> examensParMois(int annee) {
////        return em.createNativeQuery(
////                "SELECT MONTH(p.date) as mois, COUNT(DISTINCT p.id) as nombre "
////                + "FROM dbx45ty_prestation p "
////                + "WHERE p.nature_prestation = 'examen' "
////                + "AND YEAR(p.date) = :annee AND p.supprime = '-1' "
////                + "GROUP BY MONTH(p.date) ORDER BY mois")
////                .setParameter("annee", annee)
////                .getResultList();
//        return em.createNativeQuery(
//                "SELECT MONTH(p.date) as mois, COUNT(DISTINCT p.id) as nombre "
//                + "FROM dbx45ty_prestation p "
//                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
//                + "WHERE p.nature_prestation = 'examen' "
//                + "AND YEAR(p.date) = :annee AND p.supprime = '-1' "
//                + "AND l.etat = 'encaisse' AND l.supprime = '-1' "
//                + "GROUP BY MONTH(p.date) ORDER BY mois")
//                .setParameter("annee", annee)
//                .getResultList();
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Object[]> montantsParMois(int annee) {
//        return em.createNativeQuery(
//                "SELECT MONTH(date) as mois, "
//                + "COALESCE(SUM(COALESCE(montant_modif, montant)), 0) as montant "
//                + "FROM dbx45ty_consultation "
//                + "WHERE YEAR(date) = :annee AND supprime = '-1' "
//                + "GROUP BY MONTH(date) ORDER BY mois")
//                .setParameter("annee", annee)
//                .getResultList();
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Object[]> topPrestataires(int annee, int limit) {
//        return em.createNativeQuery(
//                "SELECT p.prestataire_id, "
//                + "COUNT(DISTINCT p.id) as nombre, "
//                + "COALESCE(SUM(l.valeur * l.nbre), 0) as montant "
//                + "FROM dbx45ty_prestation p "
//                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
//                + "WHERE YEAR(p.date) = :annee AND p.supprime = '-1' "
//                + "AND l.supprime = '-1' "
//                + "GROUP BY p.prestataire_id "
//                + "ORDER BY montant DESC "
//                + "LIMIT :limit")
//                .setParameter("annee", annee)
//                .setParameter("limit", limit)
//                .getResultList();
//    }
//
//    // ── PRESTATAIRE ───────────────────────────────────────────────
//    public Long countVisitesAujourdhui(String prestataireId) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_visite "
//                + "WHERE prestataire_id = :prestataireId "
//                + "AND DATE(date) = CURDATE()")
//                .setParameter("prestataireId", prestataireId)
//                .getSingleResult();
//    }
//
//    public Long countPrestationsEnAttenteAujourdhui(String prestataireId) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
//                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
//                + "WHERE p.prestataire_id = :prestataireId "
//                + "AND l.etat = 'attente_validation' "
//                + "AND DATE(p.date) = CURDATE() AND p.supprime = '-1'")
//                .setParameter("prestataireId", prestataireId)
//                .getSingleResult();
//    }
//
//    public Long countParNatureEtMois(String prestataireId,
//            String nature, int annee, int mois) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(DISTINCT p.id) FROM dbx45ty_prestation p "
//                + "WHERE p.prestataire_id = :prestataireId "
//                + "AND p.nature_prestation = :nature "
//                + "AND YEAR(p.date) = :annee AND MONTH(p.date) = :mois "
//                + "AND p.supprime = '-1'")
//                .setParameter("prestataireId", prestataireId)
//                .setParameter("nature", nature)
//                .setParameter("annee", annee)
//                .setParameter("mois", mois)
//                .getSingleResult();
//    }
//
//    public Long countBonsManuelsMois(String prestataireId, int annee, int mois) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_bon_manuel "
//                + "WHERE prestataire_id = :prestataireId "
//                + "AND YEAR(date_creation) = :annee "
//                + "AND MONTH(date_creation) = :mois "
//                + "AND supprime = '-1'")
//                .setParameter("prestataireId", prestataireId)
//                .setParameter("annee", annee)
//                .setParameter("mois", mois)
//                .getSingleResult();
//    }
//
//    public Long countLignesParEtatEtMois(String prestataireId,
//            String etat, int annee, int mois) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_ligne_prestation l "
//                + "WHERE l.prestataire_id = :prestataireId "
//                + "AND l.etat = :etat "
//                + "AND YEAR(l.date) = :annee AND MONTH(l.date) = :mois "
//                + "AND l.supprime = '-1'")
//                .setParameter("prestataireId", prestataireId)
//                .setParameter("etat", etat)
//                .setParameter("annee", annee)
//                .setParameter("mois", mois)
//                .getSingleResult();
//    }
//
//    public Double montantEncaisseParMois(String prestataireId,
//            int annee, int mois) {
//        Object result = em.createNativeQuery(
//                "SELECT COALESCE(SUM(COALESCE(l.valeur_modif, l.valeur) * "
//                + "COALESCE(l.nbre_modif, l.nbre)), 0) "
//                + "FROM dbx45ty_ligne_prestation l "
//                + "WHERE l.prestataire_id = :prestataireId "
//                + "AND l.etat = 'encaisse' "
//                + "AND YEAR(l.date_encaisse) = :annee "
//                + "AND MONTH(l.date_encaisse) = :mois "
//                + "AND l.supprime = '-1'")
//                .setParameter("prestataireId", prestataireId)
//                .setParameter("annee", annee)
//                .setParameter("mois", mois)
//                .getSingleResult();
//        return result != null ? ((Number) result).doubleValue() : 0.0;
//    }
//
//    public Long countVisitesPrestataire(String prestataireId, int annee) {
//        return (Long) em.createNativeQuery(
//                "SELECT COUNT(*) FROM dbx45ty_visite "
//                + "WHERE prestataire_id = :prestataireId "
//                + "AND YEAR(date) = :annee")
//                .setParameter("prestataireId", prestataireId)
//                .setParameter("annee", annee)
//                .getSingleResult();
//    }
//
//    public Double montantTotalPrestataire(String prestataireId, int annee) {
//        Object result = em.createNativeQuery(
//                "SELECT COALESCE(SUM(COALESCE(l.valeur_modif, l.valeur) * "
//                + "COALESCE(l.nbre_modif, l.nbre)), 0) "
//                + "FROM dbx45ty_ligne_prestation l "
//                + "WHERE l.prestataire_id = :prestataireId "
//                + "AND l.etat = 'encaisse' "
//                + "AND YEAR(l.date) = :annee "
//                + "AND l.supprime = '-1'")
//                .setParameter("prestataireId", prestataireId)
//                .setParameter("annee", annee)
//                .getSingleResult();
//        return result != null ? ((Number) result).doubleValue() : 0.0;
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Object[]> encaissementsParMois(String prestataireId, int annee) {
//        return em.createNativeQuery(
//                "SELECT MONTH(l.date_encaisse) as mois, "
//                + "COUNT(*) as nombre, "
//                + "COALESCE(SUM(COALESCE(l.valeur_modif, l.valeur) * "
//                + "COALESCE(l.nbre_modif, l.nbre)), 0) as montant "
//                + "FROM dbx45ty_ligne_prestation l "
//                + "WHERE l.prestataire_id = :prestataireId "
//                + "AND l.etat = 'encaisse' "
//                + "AND YEAR(l.date_encaisse) = :annee "
//                + "AND l.supprime = '-1' "
//                + "GROUP BY MONTH(l.date_encaisse) ORDER BY mois")
//                .setParameter("prestataireId", prestataireId)
//                .setParameter("annee", annee)
//                .getResultList();
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Object[]> repartitionParType(String prestataireId, int annee) {
//        return em.createNativeQuery(
//                "SELECT p.nature_prestation, COUNT(DISTINCT p.id) as nombre, "
//                + "COALESCE(SUM(COALESCE(l.valeur_modif, l.valeur) * "
//                + "COALESCE(l.nbre_modif, l.nbre)), 0) as montant "
//                + "FROM dbx45ty_prestation p "
//                + "JOIN dbx45ty_ligne_prestation l ON l.prestation_id = p.id "
//                + "WHERE p.prestataire_id = :prestataireId "
//                + "AND YEAR(p.date) = :annee AND p.supprime = '-1' "
//                + "AND l.supprime = '-1' "
//                + "GROUP BY p.nature_prestation")
//                .setParameter("prestataireId", prestataireId)
//                .setParameter("annee", annee)
//                .getResultList();
//    }
//
//    // ── CONSOMMATION ADHERENT ─────────────────────────────────────
//    public Double montantConsommeAdherent(String codeAdherent,
//            String nature, int annee) {
//        Object result = em.createNativeQuery(
//                "SELECT COALESCE(SUM(COALESCE(l.valeur_modif, l.valeur) * "
//                + "COALESCE(l.nbre_modif, l.nbre)), 0) "
//                + "FROM dbx45ty_ligne_prestation l "
//                + "JOIN dbx45ty_prestation p ON p.id = l.prestation_id "
//                + "JOIN dbx45ty_visite v ON v.id = p.visite_id "
//                + "WHERE v.code_adherent = :codeAdherent "
//                + "AND p.nature_prestation = :nature "
//                + "AND l.etat = 'encaisse' "
//                + "AND YEAR(l.date) = :annee "
//                + "AND l.supprime = '-1' AND p.supprime = '-1'")
//                .setParameter("codeAdherent", codeAdherent)
//                .setParameter("nature", nature)
//                .setParameter("annee", annee)
//                .getSingleResult();
//        return result != null ? ((Number) result).doubleValue() : 0.0;
//    }
//
//    public Double montantConsommeConsultation(String codeAdherent, int annee) {
//        Object result = em.createNativeQuery(
//                "SELECT COALESCE(SUM(COALESCE(c.montant_modif, c.montant)), 0) "
//                + "FROM dbx45ty_consultation c "
//                + "JOIN dbx45ty_visite v ON v.id = c.visite_id "
//                + "WHERE v.code_adherent = :codeAdherent "
//                + "AND c.etat_consultation = 'encaisse' "
//                + "AND YEAR(c.date) = :annee AND c.supprime = '-1'")
//                .setParameter("codeAdherent", codeAdherent)
//                .setParameter("annee", annee)
//                .getSingleResult();
//        return result != null ? ((Number) result).doubleValue() : 0.0;
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Object[]> consommationParMoisAdherent(
//            String codeAdherent, int annee) {
//        return em.createNativeQuery(
//                "SELECT MONTH(l.date) as mois, "
//                + "COUNT(*) as nombre, "
//                + "COALESCE(SUM(COALESCE(l.valeur_modif, l.valeur) * "
//                + "COALESCE(l.nbre_modif, l.nbre)), 0) as montant "
//                + "FROM dbx45ty_ligne_prestation l "
//                + "JOIN dbx45ty_prestation p ON p.id = l.prestation_id "
//                + "JOIN dbx45ty_visite v ON v.id = p.visite_id "
//                + "WHERE v.code_adherent = :codeAdherent "
//                + "AND l.etat = 'encaisse' "
//                + "AND YEAR(l.date) = :annee "
//                + "AND l.supprime = '-1' AND p.supprime = '-1' "
//                + "GROUP BY MONTH(l.date) ORDER BY mois")
//                .setParameter("codeAdherent", codeAdherent)
//                .setParameter("annee", annee)
//                .getResultList();
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<Object[]> consommationAyantsDroit(
//            String codeAdherent, int annee) {
//        return em.createNativeQuery(
//                "SELECT v.code_ayant_droit, "
//                + "COALESCE(SUM(COALESCE(l.valeur_modif, l.valeur) * "
//                + "COALESCE(l.nbre_modif, l.nbre)), 0) as montant "
//                + "FROM dbx45ty_ligne_prestation l "
//                + "JOIN dbx45ty_prestation p ON p.id = l.prestation_id "
//                + "JOIN dbx45ty_visite v ON v.id = p.visite_id "
//                + "WHERE v.code_adherent = :codeAdherent "
//                + "AND v.code_ayant_droit IS NOT NULL "
//                + "AND l.etat = 'encaisse' "
//                + "AND YEAR(l.date) = :annee "
//                + "AND l.supprime = '-1' AND p.supprime = '-1' "
//                + "GROUP BY v.code_ayant_droit")
//                .setParameter("codeAdherent", codeAdherent)
//                .setParameter("annee", annee)
//                .getResultList();
//    }
//}
