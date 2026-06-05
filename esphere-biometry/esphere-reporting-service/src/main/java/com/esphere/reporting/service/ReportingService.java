package com.esphere.reporting.service;

import com.esphere.reporting.dto.response.*;
import com.esphere.reporting.repository.ReportingRepository;
import com.esphere.reporting.util.DroitsAcces;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportingService {

    private final ReportingRepository reportingRepository;
    private final RestTemplate        restTemplate;

    @Value("${services.notification-url:http://localhost:8088}")
    private String notificationServiceUrl;

    @Value("${services.adherent-url:http://localhost:8082}")
    private String adherentServiceUrl;

    @Value("${services.prestataire-url:http://localhost:8083}")
    private String prestataireServiceUrl;

    // ── TABLEAU DE BORD AGENT SS ──────────────────────────────────

    public DashboardSsResponse getDashboardSs(
            String employeId, int annee) {

        int moisCourant = LocalDate.now().getMonthValue();

        // Alertes non lues
        Long alertes = getAlertesNonLues(employeId);

        return DashboardSsResponse.builder()
                // Compteurs en attente
                .consultationsEnAttente(
                        reportingRepository.countConsultationsEnAttente())
                .ordonnancesEnAttente(
                        reportingRepository.countOrdonnancesEnAttente())
                .examensEnAttente(
                        reportingRepository.countExamensEnAttente())
                .bonsManuelEnAttente(
                        reportingRepository.countBonsManuelEnAttente())
                // Compteurs du mois
                .totalValidesMois(
                        reportingRepository.countValidesParMois(annee, moisCourant))
                .totalRejetesMois(
                        reportingRepository.countRejetesMois(annee, moisCourant))
                .totalEncaissesMois(
                        reportingRepository.countEncaissesMois(annee, moisCourant))
                .montantEncaisseMois(
                        reportingRepository.montantEncaisseMois(annee, moisCourant))
                // Compteurs annuels
                .totalVisitesAnnee(
                        reportingRepository.countVisitesAnnee(annee))
                .montantTotalAnnee(
                        reportingRepository.montantTotalAnnee(annee))
                // Graphiques par mois
                .consultationsParMois(
                        buildStatsMois(reportingRepository.consultationsParMois(annee)))
                .ordonnancesParMois(
                        buildStatsMois(reportingRepository.ordonnancesParMois(annee)))
                .examensParMois(
                        buildStatsMois(reportingRepository.examensParMois(annee)))
                .montantsParMois(
                        buildStatsMoisMontant(reportingRepository.montantsParMois(annee)))
                // Top prestataires
                .topPrestataires(
                        buildTopPrestataires(
                                reportingRepository.topPrestataires(annee, 10)))
                // Alertes
                .alertesNonLues(alertes)
                .build();
    }

    // ── TABLEAU DE BORD PRESTATAIRE ───────────────────────────────

    public DashboardPrestataireResponse getDashboardPrestataire(
            String prestataireId, String categorieId, int annee) {

        int moisCourant = LocalDate.now().getMonthValue();

        // Alertes non lues
        Long alertes = getAlertesNonLues(prestataireId);

        return DashboardPrestataireResponse.builder()
                .prestataireId(prestataireId)
                .categorieId(categorieId)
                // Droits d'accès
                .accesConsultation(DroitsAcces.peutVoirConsultation(categorieId))
                .accesOrdonnance(DroitsAcces.peutVoirOrdonnance(categorieId))
                .accesExamen(DroitsAcces.peutVoirExamen(categorieId))
                .accesBonManuel(DroitsAcces.peutVoirBonManuel(categorieId))
                .accesHospitalisation(DroitsAcces.peutVoirHospitalisation(categorieId))
                // Compteurs du jour
                .visitesAujourdhui(
                        reportingRepository.countVisitesAujourdhui(prestataireId))
                .prestationsEnAttenteAujourdhui(
                        reportingRepository.countPrestationsEnAttenteAujourdhui(
                                prestataireId))
                // Compteurs du mois — selon droits
                .consultationsMois(DroitsAcces.peutVoirConsultation(categorieId)
                        ? reportingRepository.countParNatureEtMois(
                                prestataireId, "consultation", annee, moisCourant)
                        : null)
                .ordonnancesMois(DroitsAcces.peutVoirOrdonnance(categorieId)
                        ? reportingRepository.countParNatureEtMois(
                                prestataireId, "ordonnance", annee, moisCourant)
                        : null)
                .examensMois(DroitsAcces.peutVoirExamen(categorieId)
                        ? reportingRepository.countParNatureEtMois(
                                prestataireId, "examen", annee, moisCourant)
                        : null)
                .bonsManuelsMois(
                        reportingRepository.countBonsManuelsMois(
                                prestataireId, annee, moisCourant))
                .validesMois(
                        reportingRepository.countLignesParEtatEtMois(
                                prestataireId, "valide", annee, moisCourant))
                .rejetesMois(
                        reportingRepository.countLignesParEtatEtMois(
                                prestataireId, "rejete", annee, moisCourant))
                .encaissesMois(
                        reportingRepository.countLignesParEtatEtMois(
                                prestataireId, "encaisse", annee, moisCourant))
                .montantEncaisseMois(
                        reportingRepository.montantEncaisseParMois(
                                prestataireId, annee, moisCourant))
                // Compteurs annuels
                .totalVisitesAnnee(
                        reportingRepository.countVisitesPrestataire(
                                prestataireId, annee))
                .montantTotalAnnee(
                        reportingRepository.montantTotalPrestataire(
                                prestataireId, annee))
                // Graphiques
                .encaissementsParMois(
                        buildStatsMoisMontant(
                                reportingRepository.encaissementsParMois(
                                        prestataireId, annee)))
                .repartitionParType(
                        buildRepartitionParType(
                                reportingRepository.repartitionParType(
                                        prestataireId, annee)))
                // Alertes
                .alertesNonLues(alertes)
                .build();
    }

    // ── CONSOMMATION ADHERENT ─────────────────────────────────────

    @SuppressWarnings("unchecked")
    public ConsommationAdherentResponse getConsommationAdherent(
            String codeAdherent, int annee) {

        // Récupérer les infos de l'adhérent
        Map<String, Object> adherent = getAdherent(codeAdherent);

        Double plafond   = adherent.get("plafondAssurep") != null
                ? ((Number) adherent.get("plafondAssurep")).doubleValue() : 0.0;
        Double consAp    = adherent.get("consAp") != null
                ? ((Number) adherent.get("consAp")).doubleValue() : 0.0;

        // Montants par type
        Double montantConsultations =
                reportingRepository.montantConsommeConsultation(codeAdherent, annee);
        Double montantOrdonnances =
                reportingRepository.montantConsommeAdherent(
                        codeAdherent, "ordonnance", annee);
        Double montantExamens =
                reportingRepository.montantConsommeAdherent(
                        codeAdherent, "examen", annee);
        Double montantBonsManuel = 0.0; // sera ajouté via bon_manuel

        Double totalConsomme = montantConsultations + montantOrdonnances
                + montantExamens + montantBonsManuel;
        Double restant = plafond > 0 ? Math.max(0, plafond - totalConsomme) : null;
        Double tauxConsommation = plafond > 0
                ? (totalConsomme / plafond) * 100 : null;

        // Consommation par mois
        List<StatMoisResponse> parMois = buildStatsMoisMontant(
                reportingRepository.consommationParMoisAdherent(codeAdherent, annee));

        // Consommation ayants droit
        List<ConsommationAyantDroitResponse> ayantsDroit =
                buildConsommationAyantsDroit(
                        reportingRepository.consommationAyantsDroit(
                                codeAdherent, annee));

        return ConsommationAdherentResponse.builder()
                .codeAdherent(codeAdherent)
                .assurePrincipal((String) adherent.get("assurePrincipal"))
                .police((String) adherent.get("police"))
                .souscripteur((String) adherent.get("souscripteur"))
                .plafondAssurep(plafond)
                .consomme(totalConsomme)
                .restant(restant)
                .tauxConsommation(tauxConsommation)
                .montantConsultations(montantConsultations)
                .montantOrdonnances(montantOrdonnances)
                .montantExamens(montantExamens)
                .montantBonsManuel(montantBonsManuel)
                .ayantsDroit(ayantsDroit)
                .consommationParMois(parMois)
                .build();
    }

    // ── Helpers ──────────────────────────────────────────────────

    private List<StatMoisResponse> buildStatsMois(List<Object[]> rows) {
        Map<Integer, Long> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put(((Number) row[0]).intValue(),
                    ((Number) row[1]).longValue());
        }
        List<StatMoisResponse> result = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            result.add(StatMoisResponse.builder()
                    .mois(m)
                    .libelleMois(libelleMois(m))
                    .nombre(map.getOrDefault(m, 0L))
                    .montant(0.0)
                    .build());
        }
        return result;
    }

//    private List<StatMoisResponse> buildStatsMoisMontant(List<Object[]> rows) {
//        Map<Integer, double[]> map = new HashMap<>();
//        for (Object[] row : rows) {
//            int mois = ((Number) row[0]).intValue();
//            long nombre = row.length > 2
//                    ? ((Number) row[1]).longValue() : 0L;
//            double montant = row.length > 2
//                    ? ((Number) row[2]).doubleValue()
//                    : ((Number) row[1]).doubleValue();
//            map.put(mois, new double[]{nombre, montant});
//        }
//        List<StatMoisResponse> result = new ArrayList<>();
//        for (int m = 1; m <= 12; m++) {
//            double[] vals = map.getOrDefault(m, new double[]{0, 0});
//            result.add(StatMoisResponse.builder()
//                    .mois(m)
//                    .libelleMois(libelleMois(m))
//                    .nombre((long) vals[0])
//                    .montant(vals[1])
//                    .build());
//        }
//        return result;
//    }

    private List<StatMoisResponse> buildStatsMoisMontant(List<Object[]> rows) {
    Map<Integer, double[]> map = new HashMap<>();
    for (Object[] row : rows) {
        int mois = ((Number) row[0]).intValue();
        // Si 3 colonnes : mois, nombre, montant (encaissementsParMois)
        // Si 2 colonnes : mois, montant (montantsParMois)
        if (row.length >= 3) {
            long nombre  = ((Number) row[1]).longValue();
            double montant = ((Number) row[2]).doubleValue();
            map.put(mois, new double[]{nombre, montant});
        } else {
            double montant = ((Number) row[1]).doubleValue();
            map.put(mois, new double[]{0, montant});
        }
    }
    List<StatMoisResponse> result = new ArrayList<>();
    for (int m = 1; m <= 12; m++) {
        double[] vals = map.getOrDefault(m, new double[]{0, 0});
        result.add(StatMoisResponse.builder()
                .mois(m)
                .libelleMois(libelleMois(m))
                .nombre((long) vals[0])
                .montant(vals[1])
                .build());
    }
    return result;
}
    private List<TopPrestataireResponse> buildTopPrestataires(
            List<Object[]> rows) {
        return rows.stream().map(row -> TopPrestataireResponse.builder()
                .prestataireId((String) row[0])
                .nombrePrestations(((Number) row[1]).longValue())
                .montantTotal(((Number) row[2]).doubleValue())
                .build())
                .collect(Collectors.toList());
    }

    private List<StatTypeResponse> buildRepartitionParType(
            List<Object[]> rows) {
        double totalMontant = rows.stream()
                .mapToDouble(r -> ((Number) r[2]).doubleValue())
                .sum();

        return rows.stream().map(row -> {
            double montant = ((Number) row[2]).doubleValue();
            return StatTypeResponse.builder()
                    .type((String) row[0])
                    .nombre(((Number) row[1]).longValue())
                    .montant(montant)
                    .pourcentage(totalMontant > 0
                            ? (montant / totalMontant) * 100 : 0.0)
                    .build();
        }).collect(Collectors.toList());
    }

    private List<ConsommationAyantDroitResponse> buildConsommationAyantsDroit(
            List<Object[]> rows) {
        return rows.stream().map(row ->
                ConsommationAyantDroitResponse.builder()
                        .codeAyantDroit((String) row[0])
                        .montantConsomme(((Number) row[1]).doubleValue())
                        .build())
                .collect(Collectors.toList());
    }

    private String libelleMois(int mois) {
        return Month.of(mois)
                .getDisplayName(TextStyle.FULL, Locale.FRENCH);
    }

    @SuppressWarnings("unchecked")
    private Long getAlertesNonLues(String destinataireId) {
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    notificationServiceUrl
                    + "/notifications/alertes/"
                    + destinataireId + "/compteur",
                    HttpMethod.GET, null, Map.class);
            if (response.getBody() != null) {
                return ((Number) response.getBody().get("nonLues")).longValue();
            }
            return 0L;
        } catch (Exception e) {
            log.warn("Impossible de récupérer les alertes : {}", e.getMessage());
            return 0L;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getAdherent(String codeAdherent) {
        try {
            String url = adherentServiceUrl + "/adherents/"
                    + org.springframework.web.util.UriUtils
                            .encodePath(codeAdherent, "UTF-8");
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, Map.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Adhérent introuvable : " + codeAdherent);
        }
    }
}