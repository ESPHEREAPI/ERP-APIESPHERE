package com.esphere.webservicecron.service;

import com.esphere.webservicecron.dto.response.PoliceStatusResponse;
import com.esphere.webservicecron.entity.Adherent;
import com.esphere.webservicecron.repository.AdherentRepository;
import com.esphere.webservicecron.repository.AyantDroitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Construit la fiche d'état d'une police à partir des données déjà
 * synchronisées en base (dbx45ty_adherent / dbx45ty_ayant_droit).
 */
@Service
@RequiredArgsConstructor
public class PoliceStatusService {

    private final AdherentRepository adherentRepository;
    private final AyantDroitRepository ayantDroitRepository;

    @Transactional
    public PoliceStatusResponse obtenirStatut(String police) {
        Adherent unAdherent = adherentRepository.findFirstByPolice(police).orElse(null);

        long adherentsActifs = adherentRepository.countByPoliceAndStatut(police, "1");
        long adherentsDesactives = adherentRepository.countByPoliceAndStatut(police, "-1");
        long ayantsDroitActifs = ayantDroitRepository.countByPoliceAndStatut(police, "1");
        long ayantsDroitDesactives = ayantDroitRepository.countByPoliceAndStatut(police, "-1");

        if (unAdherent == null && adherentsActifs == 0 && adherentsDesactives == 0) {
            return null;
        }

        LocalDate dateEffet = unAdherent != null ? unAdherent.getEffetPolice() : null;
        LocalDate dateEcheance = unAdherent != null ? unAdherent.getEcheancePolice() : null;
        LocalDate aujourdHui = LocalDate.now();

        Long nbJoursRestants = dateEcheance != null
                ? ChronoUnit.DAYS.between(aujourdHui, dateEcheance)
                : null;

        // Regle : la police est ACTIVE lorsque sa date d'echeance est superieure a la date du jour.
        String statut = (dateEcheance != null && dateEcheance.isAfter(aujourdHui)) ? "ACTIF" : "DESACTIVE";

        return PoliceStatusResponse.builder()
                .police(police)
                .souscripteur(unAdherent != null ? unAdherent.getSouscripteur() : null)
                .dateEffet(dateEffet)
                .dateEcheance(dateEcheance)
                .nbJoursRestants(nbJoursRestants)
                .statut(statut)
                .nbAdherentsActifs(adherentsActifs)
                .nbAdherentsDesactives(adherentsDesactives)
                .nbAyantsDroitActifs(ayantsDroitActifs)
                .nbAyantsDroitDesactives(ayantsDroitDesactives)
                .build();
    }

    /**
     * Construit la fiche d'état de toutes les polices connues (triées :
     * actives d'abord, puis par échéance la plus proche).
     *
     * Implémentation en 3 requêtes agrégées au total (et non 5 requêtes
     * par police) afin d'éviter d'épuiser le pool de connexions quand le
     * nombre de polices distinctes est important.
     */
    @Transactional
    public List<PoliceStatusResponse> obtenirToutesLesPolices() {
        LocalDate aujourdHui = LocalDate.now();

        Map<String, long[]> compteursAdherents = new HashMap<>(); // police -> [actifs, desactives]
        for (Object[] ligne : adherentRepository.countGroupedByPoliceAndStatut()) {
            String police = (String) ligne[0];
            String statut = (String) ligne[1];
            long nb = (Long) ligne[2];
            long[] compteurs = compteursAdherents.computeIfAbsent(police, k -> new long[2]);
            if ("1".equals(statut)) compteurs[0] = nb; else compteurs[1] += nb;
        }

        Map<String, long[]> compteursAyantsDroit = new HashMap<>(); // police -> [actifs, desactives]
        for (Object[] ligne : ayantDroitRepository.countGroupedByPoliceAndStatut()) {
            String police = (String) ligne[0];
            String statut = (String) ligne[1];
            long nb = (Long) ligne[2];
            long[] compteurs = compteursAyantsDroit.computeIfAbsent(police, k -> new long[2]);
            if ("1".equals(statut)) compteurs[0] = nb; else compteurs[1] += nb;
        }

        List<PoliceStatusResponse> resultat = new java.util.ArrayList<>();
        for (Object[] ligne : adherentRepository.findInfoRepresentativeParPolice()) {
            String police = (String) ligne[0];
            String souscripteur = (String) ligne[1];
            LocalDate dateEffet = (LocalDate) ligne[2];
            LocalDate dateEcheance = (LocalDate) ligne[3];

            Long nbJoursRestants = dateEcheance != null
                    ? ChronoUnit.DAYS.between(aujourdHui, dateEcheance)
                    : null;
            String statut = (dateEcheance != null && dateEcheance.isAfter(aujourdHui)) ? "ACTIF" : "DESACTIVE";

            long[] adh = compteursAdherents.getOrDefault(police, new long[2]);
            long[] ayd = compteursAyantsDroit.getOrDefault(police, new long[2]);

            resultat.add(PoliceStatusResponse.builder()
                    .police(police)
                    .souscripteur(souscripteur)
                    .dateEffet(dateEffet)
                    .dateEcheance(dateEcheance)
                    .nbJoursRestants(nbJoursRestants)
                    .statut(statut)
                    .nbAdherentsActifs(adh[0])
                    .nbAdherentsDesactives(adh[1])
                    .nbAyantsDroitActifs(ayd[0])
                    .nbAyantsDroitDesactives(ayd[1])
                    .build());
        }

        return resultat.stream()
                .sorted(Comparator
                        .comparing((PoliceStatusResponse s) -> !"ACTIF".equals(s.getStatut()))
                        .thenComparing(PoliceStatusResponse::getNbJoursRestants,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }
}
