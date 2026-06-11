/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dtos.DashboardStatisticsDTO;
import db.biometry.biometry.dtos.PrestationPrestataireDTO;
import db.biometry.biometry.dtos.StatistiqueTypePrestationDTO;
import db.biometry.biometry.dtos.StatistiquesGlobalesDTO;
import db.biometry.biometry.dtos.StatistiquesParPeriodeDTO;
import db.biometry.biometry.dtos.TopPrestationDTO;
import db.biometry.biometry.repositories.AdherentRepository;
import db.biometry.biometry.repositories.AyantDroitRepository;
import db.biometry.biometry.repositories.ConsultationRepository;
import db.biometry.biometry.repositories.LignePrestationRepository;
import db.biometry.biometry.repositories.TauxPrestationRepository;
import db.biometry.biometry.entite.Dbx45tyAdherent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import db.biometry.biometry.enums.EtatPrestation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service métier pour le tableau de bord souscripteur Génère les statistiques
 * et indicateurs de performance
 *
 * @author JIATOU FRANCK
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final AdherentRepository adherentRepository;
    private final AyantDroitRepository ayantDroitRepository;
    private final ConsultationRepository consultationRepository;
    private final LignePrestationRepository lignePrestationRepository;
    private final TauxPrestationRepository tauxPrestationRepository;
    private final PlafondExterneService plafondExterneService;

    /**
     * Génère les statistiques complètes du tableau de bord
     *
     * @param codeSouscripteur Code du souscripteur
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @return Statistiques du tableau de bord
     */
    public DashboardStatisticsDTO generateDashboardStatistics(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin) {

        log.info("Génération des statistiques du tableau de bord pour le souscripteur: {}", codeSouscripteur);

        DashboardStatisticsDTO.ConsommationGlobaleDTO consommation =
                getConsommationGlobale(codeSouscripteur, dateDebut, dateFin);

        return DashboardStatisticsDTO.builder()
                .consommationGlobale(consommation)
                .statistiquesParPeriode(getStatistiquesParPeriode(codeSouscripteur, dateDebut, dateFin, "MOIS"))
                .statistiquesParTypePrestation(getStatistiquesParTypePrestation(codeSouscripteur, dateDebut, dateFin))
                .topPrestations(getTopPrestations(codeSouscripteur, dateDebut, dateFin, 5))
                .topPrestataires(getTopPrestataires(codeSouscripteur, dateDebut, dateFin, 5))
                .tauxUtilisationPlafond(getTauxUtilisationPlafond(
                        codeSouscripteur, consommation.getTotalRembourse()))
                .alertes(generateAlertes(codeSouscripteur, dateDebut, dateFin))
                .dateGeneration(LocalDateTime.now())
                .codeSouscripteur(codeSouscripteur)
                .periode(DashboardStatisticsDTO.PeriodeDTO.builder()
                        .dateDebut(dateDebut)
                        .dateFin(dateFin)
                        .type("PERSONNALISE")
                        .build())
                .build();
    }

    /**
     * Calcule la consommation globale
     */
    private DashboardStatisticsDTO.ConsommationGlobaleDTO getConsommationGlobale(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin) {

        log.debug("Calcul de la consommation globale");

        // Récupération des statistiques depuis la base
        StatistiquesGlobalesDTO statsConsultation = consultationRepository.getStatistiquesGlobales(
                codeSouscripteur, dateDebut, dateFin, EtatPrestation.encaisse.name());
        StatistiquesGlobalesDTO statsExamen_ordonnance = lignePrestationRepository.getStatistiquesGlobales(codeSouscripteur, dateDebut, dateFin, EtatPrestation.encaisse.name());
//       Long nombreConsultations = stats[0] != null ? (Long.parseLong(""+stats[0]) ) : 0L;
//        BigDecimal totalDepenses = stats[1] != null ? (BigDecimal) stats[1] : BigDecimal.ZERO;
//        BigDecimal totalRembourse = stats[2] != null ? (BigDecimal) stats[2] : BigDecimal.ZERO;
//        BigDecimal moyenneDepense = stats[3] != null ? (BigDecimal) stats[3] : BigDecimal.ZERO;

        Long nombreConsultations = statsConsultation.getNombreConsultations() != null ? statsConsultation.getNombreConsultations() : 0L;
        // nombreConsultations = nombreConsultations + (statsExamen_ordonnance.getNombreConsultations() != null ? statsExamen_ordonnance.getNombreConsultations() : 0L);

        BigDecimal totalDepenses = statsConsultation.getTotalDepenses() != null ? statsConsultation.getTotalDepenses() : BigDecimal.ZERO;
        totalDepenses = totalDepenses.add(statsExamen_ordonnance.getTotalDepenses() != null ? statsExamen_ordonnance.getTotalDepenses() : BigDecimal.ZERO);

        BigDecimal totalRembourse = statsConsultation.getTotalRembourse() != null ? statsConsultation.getTotalRembourse() : BigDecimal.ZERO;
        totalRembourse = totalRembourse.add(statsExamen_ordonnance.getTotalRembourse() != null ? statsExamen_ordonnance.getTotalRembourse() : BigDecimal.ZERO);
        BigDecimal moyenneDepense = statsConsultation.getMoyenneDepenses() != null ? statsConsultation.getMoyenneDepenses() : BigDecimal.ZERO;
        moyenneDepense = moyenneDepense.add(statsExamen_ordonnance.getMoyenneDepenses() != null ? statsExamen_ordonnance.getMoyenneDepenses() : BigDecimal.ZERO);

        // Calcul du total à charge
        BigDecimal totalACharge = totalDepenses.subtract(totalRembourse);
        // Nombre de prestations
        Long nombrePrestations = lignePrestationRepository.countPrestationsBySouscripteur(
                codeSouscripteur, dateDebut, dateFin, EtatPrestation.encaisse.name());
        // Long nombrePrestations=statsExamen_ordonnance.getNombreConsultations();
        // Nombre d'assurés et ayants droit actifs
        Long nombreAssuresActifs = adherentRepository.countActiveAdherentsBySouscripteur(codeSouscripteur);
        Long nombreAyantsDroitsActifs = ayantDroitRepository.countActiveBySouscripteur(codeSouscripteur);

        // Calcul de la moyenne de dépense par assuré
        BigDecimal moyenneDepenseParAssure = BigDecimal.ZERO;
        if (nombreAssuresActifs > 0) {
            moyenneDepenseParAssure = totalDepenses.divide(
                    BigDecimal.valueOf(nombreAssuresActifs), 2, RoundingMode.HALF_UP);
        }

        // Calcul du taux de remboursement moyen
        BigDecimal tauxRemboursementMoyen = BigDecimal.ZERO;
        if (totalDepenses.compareTo(BigDecimal.ZERO) > 0) {
            tauxRemboursementMoyen = totalRembourse
                    .divide(totalDepenses, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return DashboardStatisticsDTO.ConsommationGlobaleDTO.builder()
                .totalDepenses(totalDepenses)
                .totalRembourse(totalRembourse)
                .totalACharge(totalACharge)
                .nombreConsultations(nombreConsultations.intValue())
                .nombrePrestations(nombrePrestations.intValue())
                .nombreAssuresActifs(nombreAssuresActifs.intValue())
                .nombreAyantsDroitsActifs(nombreAyantsDroitsActifs.intValue())
                .moyenneDepenseParAssure(moyenneDepenseParAssure)
                .tauxRemboursementMoyen(tauxRemboursementMoyen)
                .build();
    }

    /**
     * Calcule les statistiques par période
     */
    private List<DashboardStatisticsDTO.StatistiquePeriodeDTO> getStatistiquesParPeriode(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            String typePeriode) {

        log.debug("Calcul des statistiques par période: {}", typePeriode);

        String format;
        switch (typePeriode) {
            case "JOUR":
                format = "%Y-%m-%d";
                break;
            case "SEMAINE":
                format = "%Y-%u";
                break;
            case "ANNEE":
                format = "%Y";
                break;
            default: // MOIS
                format = "%Y-%m";
        }

        List<StatistiquesParPeriodeDTO> results = consultationRepository.getStatistiquesParPeriode(
                codeSouscripteur, dateDebut, dateFin, format, EtatPrestation.encaisse.name());

        List<StatistiquesParPeriodeDTO> resultsExem_Ordonnance = lignePrestationRepository.getStatistiquesParPeriode(
                codeSouscripteur, dateDebut, dateFin, format, EtatPrestation.encaisse.name());
        //resultsExem_Ordonnance.addAll(results);
        System.out.println("avant la fusion******");
        results.forEach(System.out::println);
        System.out.println("avant la fusion exam******");
        resultsExem_Ordonnance.forEach(System.out::println);

        results = this.fusionnerStatistiques(results, resultsExem_Ordonnance);
        System.out.println("apres la fusion exam******");
        results.forEach(System.out::println);

        List<DashboardStatisticsDTO.StatistiquePeriodeDTO> statistiques = new ArrayList<>();

        for (StatistiquesParPeriodeDTO result : results) {
            String periode = result.getPeriode();
            Long nombreConsultations = result.getNombreConsultations();
            BigDecimal montantTotal = result.getTotalDepenses();
            BigDecimal montantRembourse = result.getTotalRembourse();//represente le ticket moderateur

            statistiques.add(DashboardStatisticsDTO.StatistiquePeriodeDTO.builder()
                    .periode(typePeriode)
                    .libelle(periode)
                    .montantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO)
                    .montantRembourse(montantRembourse != null ? montantRembourse : BigDecimal.ZERO)
                    .nombreConsultations(nombreConsultations.intValue())
                    .build());
        }

        return statistiques;
    }

    public List<StatistiquesParPeriodeDTO> fusionnerStatistiques(
            List<StatistiquesParPeriodeDTO> consultations,
            List<StatistiquesParPeriodeDTO> examens) {

        return Stream.concat(consultations.stream(), examens.stream())
                .collect(Collectors.groupingBy(
                        StatistiquesParPeriodeDTO::getPeriode,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    Date date = list.get(0).getDate();
                                    String periode = list.get(0).getPeriode();

                                    Long totalConsultations = list.stream()
                                            .map(StatistiquesParPeriodeDTO::getNombreConsultations)
                                            .reduce(0L, Long::sum);

                                    BigDecimal totalDepenses = list.stream()
                                            .map(StatistiquesParPeriodeDTO::getTotalDepenses)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    BigDecimal totalRembourse = list.stream()
                                            .map(StatistiquesParPeriodeDTO::getTotalRembourse)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    StatistiquesParPeriodeDTO result = new StatistiquesParPeriodeDTO(
                                            date,
                                            totalConsultations,
                                            totalDepenses.doubleValue(),
                                            totalRembourse.doubleValue()
                                    );

                                    return result;
                                }
                        )
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(StatistiquesParPeriodeDTO::getDate))
                .collect(Collectors.toList());
    }

    public List<StatistiqueTypePrestationDTO> fusionnerStatistiquesTypePrestation(
            List<StatistiqueTypePrestationDTO> listprestation_1,
            List<StatistiqueTypePrestationDTO> listPrestation_2) {

        return Stream.concat(listprestation_1.stream(), listPrestation_2.stream())
                .collect(Collectors.groupingBy(
                        StatistiqueTypePrestationDTO::getCategorie,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    //Date date = list.get(0).getDate();
                                    String categorie = list.get(0).getCategorie();
                                    String typeId = "" + list.get(0).getTypeId();
                                    String nom = list.get(0).getNom();

                                    Long totalnombreConsultations = list.stream()
                                            .map(StatistiqueTypePrestationDTO::getNombreConsultations)
                                            .reduce(0L, Long::sum);

                                    BigDecimal totalDepenses = list.stream()
                                            .map(StatistiqueTypePrestationDTO::getTotalDepenses)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    BigDecimal totalRembourse = list.stream()
                                            .map(StatistiqueTypePrestationDTO::getTotalRembourse)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    StatistiqueTypePrestationDTO result = new StatistiqueTypePrestationDTO(typeId, nom, categorie, totalnombreConsultations, totalDepenses.doubleValue(), totalRembourse.doubleValue()
                                    );

                                    return result;
                                }
                        )
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(StatistiqueTypePrestationDTO::getCategorie))
                .collect(Collectors.toList());
    }
    
     //prestation by top adherent consomation 
    public List<TopPrestationDTO> fusionnerStatistiquesTopPrestionAherent(
        List<TopPrestationDTO> listprestation_1,
        List<TopPrestationDTO> listPrestation_2) {
    
    List<TopPrestationDTO> listeFusionnee = Stream.concat(listprestation_1.stream(), listPrestation_2.stream())
            .collect(Collectors.groupingBy(
                    TopPrestationDTO::getNom,
                    Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                String categorie = list.get(0).getCategorie();
                                String typeId = "" + list.get(0).getTypeId();
                                String nom = list.get(0).getNom();
                                Long totalnombreConsultations = list.stream()
                                        .map(TopPrestationDTO::getNombreConsultations)
                                        .reduce(0L, Long::sum);
                                BigDecimal totalDepenses = list.stream()
                                        .map(TopPrestationDTO::getTotalDepenses)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                             
                                TopPrestationDTO result = new TopPrestationDTO(
                                    typeId, nom, categorie, totalnombreConsultations, totalDepenses
                                );
                                return result;
                            }
                    )
            ))
            .values()
            .stream()
            // Tri par nombre de consultations décroissant (du plus grand au plus petit)
            .sorted(Comparator.comparing(TopPrestationDTO::getNombreConsultations).reversed())
            .collect(Collectors.toList());
    
//    // Attribuer les rangs après le tri
//    for (int i = 0; i < listeFusionnee.size(); i++) {
//        listeFusionnee.get(i).setRang(i + 1);
//    }
    
    return listeFusionnee;
}
    /**
     * Calcule les statistiques par type de prestation
     */
    private List<DashboardStatisticsDTO.StatistiqueTypePrestationDTO> getStatistiquesParTypePrestation(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin) {

        log.debug("Calcul des statistiques par type de prestation");

        List<StatistiqueTypePrestationDTO> results = consultationRepository.getStatistiquesParTypePrestation(
                codeSouscripteur, dateDebut, dateFin, EtatPrestation.encaisse.name());
        List<StatistiqueTypePrestationDTO> resultsExamen_ordonnance = lignePrestationRepository.getStatistiquesParTypePrestation(
                codeSouscripteur, dateDebut, dateFin, EtatPrestation.encaisse.name());
//         if (!resultsExamen_ordonnance.isEmpty()) {
//            results.addAll(resultsExamen_ordonnance);
//        }
//         for (TypePrestations prestations : TypePrestations.values()) {
//             
//            
//        }
        results.forEach(System.out::println);

        resultsExamen_ordonnance.forEach(System.out::println);
        results = fusionnerStatistiquesTypePrestation(results, resultsExamen_ordonnance);

        List<DashboardStatisticsDTO.StatistiqueTypePrestationDTO> statistiques = new ArrayList<>();
        BigDecimal totalGlobal = BigDecimal.ZERO;

        // Calcul du total global d'abord
        for (StatistiqueTypePrestationDTO result : results) {
            BigDecimal montant = result.getTotalDepenses();
            totalGlobal = totalGlobal.add(montant != null ? montant : BigDecimal.ZERO);
        }

        // Construction des statistiques avec pourcentages
        for (StatistiqueTypePrestationDTO result : results) {
            String typeId = "" + result.getTypeId();
            String typeNom = result.getNom();
            String categorie = result.getCategorie();
            Long nombreUtilisations = result.getNombreConsultations();
            BigDecimal montantTotal = result.getTotalDepenses();
            BigDecimal montantRembourse = result.getTotalRembourse();

            BigDecimal pourcentageTotal = BigDecimal.ZERO;
            if (totalGlobal.compareTo(BigDecimal.ZERO) > 0) {
                pourcentageTotal = montantTotal
                        .divide(totalGlobal, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            BigDecimal tauxRemboursementMoyen = BigDecimal.ZERO;
            if (montantTotal.compareTo(BigDecimal.ZERO) > 0) {
                tauxRemboursementMoyen = montantRembourse
                        .divide(montantTotal, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            statistiques.add(DashboardStatisticsDTO.StatistiqueTypePrestationDTO.builder()
                    .typeId(typeId)
                    .typeNom(typeNom)
                    .categorie(categorie)
                    .montantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO)
                    .montantRembourse(montantRembourse != null ? montantRembourse : BigDecimal.ZERO)
                    .nombreUtilisations(nombreUtilisations.intValue())
                    .pourcentageTotal(pourcentageTotal)
                    .tauxRemboursementMoyen(tauxRemboursementMoyen)
                    .build());
        }

        return statistiques;
    }

    /**
     * Récupère le top N des prestations les plus utilisées
     */
    private List<DashboardStatisticsDTO.TopPrestationDTO> getTopPrestations(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            int limit) {

        log.debug("Récupération du top {} des prestations", limit);

        List<TopPrestationDTO> results = consultationRepository.getTopPrestations(
                codeSouscripteur, dateDebut, dateFin,EtatPrestation.encaisse.name());
//        if(results.isEmpty()){
//            results=lignePrestationRepository.getTopPrestations(codeSouscripteur, dateDebut, dateFin, EtatPrestation.encaisse.name());
//        } else {
//           results.addAll(lignePrestationRepository.getTopPrestations(codeSouscripteur, dateDebut, dateFin, EtatPrestation.encaisse.name()));
//        }
           results=fusionnerStatistiquesTopPrestionAherent(results, lignePrestationRepository.getTopPrestations(codeSouscripteur, dateDebut, dateFin, EtatPrestation.encaisse.name()));

        List<DashboardStatisticsDTO.TopPrestationDTO> topPrestations = new ArrayList<>();
        Long totalUtilisations = 0L;

        // Calcul du total des utilisations
        for (TopPrestationDTO result : results) {
            Long count = result.getNombreConsultations();
            totalUtilisations += count;
        }

        // Construction du top avec limit
        int rang = 1;
        for (int i = 0; i < Math.min(limit, results.size()); i++) {
            TopPrestationDTO result = results.get(i);
            String typeId = result.getTypeId();
            String typeNom = result.getNom();
            String categorie = result.getCategorie();
            Long nombreUtilisations = result.getNombreConsultations();
            BigDecimal montantTotal = result.getTotalDepenses();

            BigDecimal pourcentageUtilisation = BigDecimal.ZERO;
            if (totalUtilisations > 0) {
                pourcentageUtilisation = BigDecimal.valueOf(nombreUtilisations)
                        .divide(BigDecimal.valueOf(totalUtilisations), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            topPrestations.add(DashboardStatisticsDTO.TopPrestationDTO.builder()
                    .rang(rang++)
                    .typeId(typeId)
                    .typenom(typeNom)
                    .categorie(categorie)
                    .nombreUtilisations(nombreUtilisations.intValue())
                    .montantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO)
                    .pourcentageUtilisation(pourcentageUtilisation)
                    .build());
        }

        return topPrestations;
    }

    /**
     * Récupère le top N des prestataires les plus sollicités
     */
    private List<DashboardStatisticsDTO.TopPrestataireDTO> getTopPrestataires(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            int limit) {

        log.debug("Récupération du top {} des prestataires", limit);

        List<PrestationPrestataireDTO> results = lignePrestationRepository.getPrestationsParPrestataire(
                codeSouscripteur, dateDebut, dateFin);

        List<DashboardStatisticsDTO.TopPrestataireDTO> topPrestataires = new ArrayList<>();
        Long totalVisites = 0L;

        // Calcul du total des visites
        for (PrestationPrestataireDTO result : results) {
            Long count = result.getNombreLignes();
            totalVisites += count;
        }

        // Construction du top avec limit
        int rang = 1;
        for (int i = 0; i < Math.min(limit, results.size()); i++) {
            PrestationPrestataireDTO result = results.get(i);
            String prestataireId = result.getPrestataireId();
            String nomPrestataire = result.getNomPrestataire();
            String categoriePrestataire = result.getCategorie();
            Long nombreVisites = result.getNombreLignes();
            BigDecimal montantTotal = result.getTotalMontant();

            BigDecimal pourcentageUtilisation = BigDecimal.ZERO;
            if (totalVisites > 0) {
                pourcentageUtilisation = BigDecimal.valueOf(nombreVisites)
                        .divide(BigDecimal.valueOf(totalVisites), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            topPrestataires.add(DashboardStatisticsDTO.TopPrestataireDTO.builder()
                    .rang(rang++)
                    .prestataireId(prestataireId)
                    .nomPrestataire(nomPrestataire)
                    .categoriePrestataire(categoriePrestataire)
                    .nombreVisites(nombreVisites.intValue())
                    .montantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO)
                    .pourcentageUtilisation(pourcentageUtilisation)
                    .build());
        }

        return topPrestataires;
    }

    /**
     * Calcule le taux d'utilisation des plafonds pour un souscripteur.
     *
     * Algorithme robuste :
     *  1. Vérifier si certains adhérents actifs ont un plafond null/0 en base.
     *  2. Pour ces adhérents, interroger l'API externe (cache 30 min) et mettre
     *     à jour la base dans une transaction REQUIRES_NEW.
     *  3. Sommer tous les plafonds des adhérents actifs.
     *  4. Comparer avec la consommation totale (PEC) déjà calculée.
     */
    private DashboardStatisticsDTO.TauxUtilisationPlafondDTO getTauxUtilisationPlafond(
            String codeSouscripteur,
            BigDecimal totalPEC) {

        log.debug("[DashboardSouscripteur] Calcul plafond global pour {}", codeSouscripteur);

        // Charger TOUS les adhérents actifs du souscripteur (pour sommer en mémoire)
        List<Dbx45tyAdherent> tous = adherentRepository.findActiveAdherentsBySouscripteur(codeSouscripteur);

        if (tous.isEmpty()) {
            log.warn("[DashboardSouscripteur] Aucun adhérent actif pour {}", codeSouscripteur);
            return DashboardStatisticsDTO.TauxUtilisationPlafondDTO.builder()
                    .plafondGlobal(BigDecimal.ZERO).montantUtilise(BigDecimal.ZERO)
                    .montantRestant(BigDecimal.ZERO).pourcentageUtilisation(BigDecimal.ZERO)
                    .detailsParType(new ArrayList<>()).build();
        }

        // 1. Sommer les plafonds déjà présents en base
        BigDecimal plafondGlobal = tous.stream()
                .filter(a -> a.getPlafondAssurep() != null && a.getPlafondAssurep() > 0.0)
                .map(a -> BigDecimal.valueOf(a.getPlafondAssurep()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Si aucun plafond en base, interroger l'API externe
        if (plafondGlobal.compareTo(BigDecimal.ZERO) == 0) {
            try {
                // Priorité : recherche par numéro de police (identifiant unique du contrat)
                String police = tous.stream()
                        .map(Dbx45tyAdherent::getPolice)
                        .filter(p -> p != null && !p.isBlank())
                        .findFirst().orElse(null);

                if (police != null) {
                    plafondGlobal = plafondExterneService.sumPlafondsByPolice(police);
                    log.info("[DashboardSouscripteur] Plafond via police={} → {}", police, plafondGlobal);
                }

                // Fallback : recherche par nom de souscripteur
                if (plafondGlobal.compareTo(BigDecimal.ZERO) == 0) {
                    plafondGlobal = plafondExterneService.sumPlafondsBySouscripteur(codeSouscripteur);
                    log.info("[DashboardSouscripteur] Plafond via souscripteur={} → {}", codeSouscripteur, plafondGlobal);
                }
            } catch (Exception e) {
                log.warn("[DashboardSouscripteur] Plafond externe non récupéré pour {} : {}",
                        codeSouscripteur, e.getMessage());
            }
        }

        log.info("[DashboardSouscripteur] Plafond global {} = {} FCFA ({} adhérents)",
                codeSouscripteur, plafondGlobal, tous.size());

        BigDecimal montantUtilise = totalPEC != null ? totalPEC : BigDecimal.ZERO;
        BigDecimal montantRestant = plafondGlobal.subtract(montantUtilise).max(BigDecimal.ZERO);

        BigDecimal pourcentage = BigDecimal.ZERO;
        if (plafondGlobal.compareTo(BigDecimal.ZERO) > 0) {
            pourcentage = montantUtilise
                    .multiply(BigDecimal.valueOf(100))
                    .divide(plafondGlobal, 2, java.math.RoundingMode.HALF_UP);
        }

        return DashboardStatisticsDTO.TauxUtilisationPlafondDTO.builder()
                .plafondGlobal(plafondGlobal)
                .montantUtilise(montantUtilise)
                .montantRestant(montantRestant)
                .pourcentageUtilisation(pourcentage)
                .detailsParType(new ArrayList<>())
                .build();
    }

    /**
     * Génère les alertes
     */
    private List<DashboardStatisticsDTO.AlerteDTO> generateAlertes(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin) {

        log.debug("Génération des alertes");

        List<DashboardStatisticsDTO.AlerteDTO> alertes = new ArrayList<>();

        // Détection des consultations répétées
        List<Object[]> consultationsRepetees = consultationRepository.detectConsultationsRepetees(
                codeSouscripteur, dateDebut, dateFin, 5);

        for (Object[] result : consultationsRepetees) {
            String codeAssure = (String) result[0];
            Long nombreConsultations = (Long) result[1];

            alertes.add(DashboardStatisticsDTO.AlerteDTO.builder()
                    .type("VISITE_REPETEE")
                    .niveau("WARNING")
                    .message("Nombre élevé de consultations détecté")
                    .codeAssure(codeAssure)
                    .dateDetection(LocalDateTime.now())
                    .details(String.format("%d consultations sur la période", nombreConsultations))
                    .build());
        }

        return alertes;
    }
}
