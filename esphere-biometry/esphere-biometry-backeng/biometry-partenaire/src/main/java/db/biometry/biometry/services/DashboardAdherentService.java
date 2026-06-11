package db.biometry.biometry.services;

import db.biometry.biometry.dtos.DashboardAdherentDTO;
import db.biometry.biometry.entite.*;
import db.biometry.biometry.exceptions.ResourceNotFoundException;
import db.biometry.biometry.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service du tableau de bord adhérent.
 *
 * Consommation couverte :
 *   Consultation  → Dbx45tyConsultation   montantModif * (taux/100) = PEC
 *   Examen        → Dbx45tyLignePrestation (naturePrestation='examen')
 *   Ordonnance    → Dbx45tyLignePrestation (naturePrestation='ordonnance')
 *
 * Plafond : plafondAssurep dans Dbx45tyAdherent
 *
 * Formule PEC  : (montantModif|montant) * (taux/100)
 * Formule TM   : si taux<100 → (montantModif|montant) * ((100-taux)/100), sinon 0
 * Pour lignes   : (valeurModif|valeur) * (nbreModif|nbre) * (taux/100)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardAdherentService {

    private final AdherentRepository        adherentRepository;
    private final AyantDroitRepository      ayantDroitRepository;
    private final ConsultationRepository    consultationRepository;
    private final LignePrestationRepository lignePrestationRepository;
    private final PrestataireRepositories   prestataireRepository;
    private final PlafondExterneService     plafondExterneService;
    private final AdherentWriteService      adherentWriteService;

    // ── Point d'entrée ────────────────────────────────────────────────────────

    /**
     * @param prestataireId  filtre optionnel sur le prestataire (code)
     * @param dateDebutStr   filtre optionnel date début (yyyy-MM-dd)
     * @param dateFinStr     filtre optionnel date fin   (yyyy-MM-dd)
     * @param typePrestation filtre optionnel sur le type (consultation|examen|ordonnance)
     * @param codeAyantDroit filtre optionnel sur le bénéficiaire (code ayant droit)
     */
    public DashboardAdherentDTO getDashboard(String codeAdherent, int page, int size,
                                             String prestataireId, String dateDebutStr,
                                             String dateFinStr,   String typePrestation,
                                             String codeAyantDroit) {
        log.info("[DashboardAdherent] code={} page={} size={} filtre=[type={} presta={} ad={}]",
                codeAdherent, page, size, typePrestation, prestataireId, codeAyantDroit);

        Dbx45tyAdherent adherent = adherentRepository.findByCodeAdherent(codeAdherent)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Adhérent non trouvé : " + codeAdherent));

        // Période : année en cours
        LocalDateTime debutAnnee = LocalDateTime.now()
                .withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime maintenant = LocalDateTime.now();

        // ── Consommation détaillée ────────────────────────────────────────────

        // Consultation
        BigDecimal pecConsultation = safe(consultationRepository
                .getMontantRemboursePourAdherent(codeAdherent, debutAnnee, maintenant));
        BigDecimal tmConsultation  = safe(consultationRepository
                .getTicketModerateurPourAdherent(codeAdherent, debutAnnee, maintenant));
        int nbConsultations = consultationRepository
                .countByAdherentAndDateBetween(codeAdherent, debutAnnee, maintenant);

        // Examen
        BigDecimal pecExamen = safe(lignePrestationRepository
                .getMontantRembourseParNature(codeAdherent, "examen", debutAnnee, maintenant));
        BigDecimal tmExamen  = safe(lignePrestationRepository
                .getTMParNature(codeAdherent, "examen", debutAnnee, maintenant));
        int nbExamens = lignePrestationRepository
                .countByAdherentNatureAndDate(codeAdherent, "examen", debutAnnee, maintenant);

        // Ordonnance
        BigDecimal pecOrdonnance = safe(lignePrestationRepository
                .getMontantRembourseParNature(codeAdherent, "ordonnance", debutAnnee, maintenant));
        BigDecimal tmOrdonnance  = safe(lignePrestationRepository
                .getTMParNature(codeAdherent, "ordonnance", debutAnnee, maintenant));
        int nbOrdonnances = lignePrestationRepository
                .countByAdherentNatureAndDate(codeAdherent, "ordonnance", debutAnnee, maintenant);

        // Totaux
        BigDecimal totalPEC     = pecConsultation.add(pecExamen).add(pecOrdonnance);
        BigDecimal totalTM      = tmConsultation.add(tmExamen).add(tmOrdonnance);
        BigDecimal totalDepense = totalPEC.add(totalTM);

        // Part ayants droit
        BigDecimal pecAD = safe(consultationRepository
                .getMontantRembourseAyantsDroitsPourAdherent(codeAdherent, debutAnnee, maintenant))
                .add(safe(lignePrestationRepository
                .getMontantRembourseAyantsDroitsPourAdherent(codeAdherent, debutAnnee, maintenant)));

        // ── Plafond ───────────────────────────────────────────────────────────
        // Priorité 1 : plafond en base locale (plafond_assurep)
        // Priorité 2 : si null/0 → appel API externe Esphere → sauvegarde en base
        Double plafondValue = adherent.getPlafondAssurep();

        if (plafondValue == null || plafondValue == 0.0) {
            log.info("[DashboardAdherent] plafond non défini en base pour {} — appel API externe", codeAdherent);
            Double externe = plafondExterneService.getPlafond(codeAdherent);
            if (externe != null && externe > 0.0) {
                plafondValue = externe;
                // Sauvegarde en base dans une transaction séparée (service readOnly=true ici)
                try {
                    adherentWriteService.updatePlafond(codeAdherent, externe);
                } catch (Exception e) {
                    log.warn("[DashboardAdherent] Impossible de sauvegarder le plafond externe : {}", e.getMessage());
                }
            }
        }

        BigDecimal plafond = plafondValue != null && plafondValue > 0
                ? BigDecimal.valueOf(plafondValue)
                : BigDecimal.ZERO;

        BigDecimal restant = plafond.compareTo(BigDecimal.ZERO) > 0
                ? plafond.subtract(totalPEC).max(BigDecimal.ZERO)
                : BigDecimal.ZERO;

        double pourcentage = plafond.compareTo(BigDecimal.ZERO) > 0
                ? totalPEC.multiply(BigDecimal.valueOf(100))
                          .divide(plafond, 2, RoundingMode.HALF_UP)
                          .doubleValue()
                : 0.0;

        // ── Ayants droit ──────────────────────────────────────────────────────
        List<Dbx45tyAyantDroit> ayantsDroits =
                ayantDroitRepository.findByAdherent(codeAdherent);

        // ── Normalisation des filtres ─────────────────────────────────────────
        String filtPresta = (prestataireId != null && !prestataireId.isBlank()) ? prestataireId : null;
        String filtAD     = (codeAyantDroit != null && !codeAyantDroit.isBlank()) ? codeAyantDroit : null;
        String filtType   = (typePrestation != null && !typePrestation.isBlank()) ? typePrestation.toLowerCase() : null;
        LocalDateTime filtDebut = parseDateFilter(dateDebutStr, true);
        LocalDateTime filtFin   = parseDateFilter(dateFinStr, false);

        // ── Dernières visites paginées avec filtres ───────────────────────────
        int fetchCount = (page + 1) * size;
        boolean inclureConsultations = filtType == null || filtType.equals("consultation");
        boolean inclureLignes        = filtType == null || filtType.equals("examen") || filtType.equals("ordonnance");
        String  lignesType           = (filtType != null && !filtType.equals("consultation")) ? filtType : null;

        List<Dbx45tyConsultation> consultations = inclureConsultations
                ? consultationRepository.findDernieresVisitesFiltrees(
                        codeAdherent, filtPresta, filtDebut, filtFin, filtAD,
                        PageRequest.of(0, fetchCount))
                : List.of();
        List<Dbx45tyLignePrestation> lignes = inclureLignes
                ? lignePrestationRepository.findDernieresLignesFiltrees(
                        codeAdherent, filtPresta, filtDebut, filtFin, filtAD, lignesType,
                        PageRequest.of(0, fetchCount))
                : List.of();

        // Batch-load prestataire noms (évite N+1)
        Set<String> prestIds = new HashSet<>();
        consultations.stream()
                .filter(c -> c.getVisiteId() != null)
                .map(c -> c.getVisiteId().getPrestataireId())
                .filter(Objects::nonNull)
                .forEach(prestIds::add);
        lignes.stream()
                .filter(lp -> lp.getPrestataireId() != null)
                .map(lp -> lp.getPrestataireId().getId())
                .forEach(prestIds::add);

        Map<String, String> prestNoms = prestataireRepository.findAllById(prestIds).stream()
                .collect(Collectors.toMap(Dbx45tyPrestataire::getId, p -> p.getNom() != null ? p.getNom() : p.getId()));

        // Batch-load ayant-droit noms pour les visites
        Set<String> adCodes = new HashSet<>();
        consultations.stream()
                .filter(c -> c.getVisiteId() != null && c.getVisiteId().getCodeAyantDroit() != null)
                .map(c -> c.getVisiteId().getCodeAyantDroit())
                .forEach(adCodes::add);
        lignes.stream()
                .filter(lp -> lp.getPrestationId() != null && lp.getPrestationId().getVisiteId() != null
                           && lp.getPrestationId().getVisiteId().getCodeAyantDroit() != null)
                .map(lp -> lp.getPrestationId().getVisiteId().getCodeAyantDroit())
                .forEach(adCodes::add);

        Map<String, String> adNoms = new HashMap<>();
        if (!adCodes.isEmpty()) {
            ayantDroitRepository.findAllById(adCodes)
                    .forEach(ad -> adNoms.put(ad.getCodeAyantDroit(), ad.getNom()));
        }

        // Fusion + tri par date desc
        List<DashboardAdherentDTO.VisiteRecenteDTO> allVisites = Stream.concat(
                consultations.stream().map(c -> toVisiteFromConsultation(c, prestNoms, adNoms)),
                lignes.stream().map(lp -> toVisiteFromLigne(lp, adNoms))
        )
        .sorted(Comparator.comparing(
                DashboardAdherentDTO.VisiteRecenteDTO::getDate,
                Comparator.nullsLast(Comparator.reverseOrder())))
        .collect(Collectors.toList());

        // Total réel pour pagination (respecte les filtres)
        long totalVisites = (inclureConsultations
                ? consultationRepository.countConsultationsFiltrees(
                        codeAdherent, filtPresta, filtDebut, filtFin, filtAD)
                : 0L)
                + (inclureLignes
                ? lignePrestationRepository.countLignesFiltrees(
                        codeAdherent, filtPresta, filtDebut, filtFin, filtAD, lignesType)
                : 0L);

        // Totaux filtrés (montant base + PEC) pour affichage sous pagination
        BigDecimal filtBaseCons = inclureConsultations
                ? safe(consultationRepository.sumMontantBaseConsultationsFiltrees(
                        codeAdherent, filtPresta, filtDebut, filtFin, filtAD))
                : BigDecimal.ZERO;
        BigDecimal filtPECCons  = inclureConsultations
                ? safe(consultationRepository.sumPECConsultationsFiltrees(
                        codeAdherent, filtPresta, filtDebut, filtFin, filtAD))
                : BigDecimal.ZERO;
        BigDecimal filtBaseLig = inclureLignes
                ? safe(lignePrestationRepository.sumMontantBaseLignesFiltrees(
                        codeAdherent, filtPresta, filtDebut, filtFin, filtAD, lignesType))
                : BigDecimal.ZERO;
        BigDecimal filtPECLig  = inclureLignes
                ? safe(lignePrestationRepository.sumPECLignesFiltrees(
                        codeAdherent, filtPresta, filtDebut, filtFin, filtAD, lignesType))
                : BigDecimal.ZERO;
        BigDecimal filteredTotalBase = filtBaseCons.add(filtBaseLig);
        BigDecimal filteredTotalPEC  = filtPECCons.add(filtPECLig);

        // Tranche de la page courante
        int fromIdx = page * size;
        int toIdx   = Math.min(fromIdx + size, allVisites.size());
        List<DashboardAdherentDTO.VisiteRecenteDTO> pageVisitesList =
                fromIdx < allVisites.size() ? allVisites.subList(fromIdx, toIdx) : List.of();

        // ── Échéance ──────────────────────────────────────────────────────────
        long joursRestants    = calculerJoursRestants(adherent.getEcheancePolice());
        String niveauEcheance = niveauAlertEcheance(joursRestants);

        return DashboardAdherentDTO.builder()
                .codeAdherent(adherent.getCodeAdherent())
                .nom(adherent.getAssurePrincipal())
                .sexe(adherent.getSexe())
                .telephone(adherent.getTelephone())
                .matricule(adherent.getMatricule())
                .statut(adherent.getStatut())
                .police(adherent.getPolice())
                .souscripteur(adherent.getSouscripteur())
                .groupe(adherent.getGroupe())
                .taux(adherent.getTaux())
                .naissance(adherent.getNaissance())
                .effetPolice(adherent.getEffetPolice())
                .echeancePolice(adherent.getEcheancePolice())
                .joursAvantEcheance(joursRestants)
                .niveauAlertEcheance(niveauEcheance)
                .plafond(DashboardAdherentDTO.PlafondDTO.builder()
                        .plafondGlobal(plafond)
                        .montantConsomme(totalPEC)
                        .montantRestant(restant)
                        .pourcentageConsomme(pourcentage)
                        .niveau(niveauPlafond(pourcentage))
                        .build())
                .consommation(DashboardAdherentDTO.ConsommationDTO.builder()
                        // Totaux
                        .montantTotalPriseEnCharge(totalPEC)
                        .montantTotalTicketModerateur(totalTM)
                        .montantTotalDepense(totalDepense)
                        .montantAyantsDroits(pecAD)
                        // Consultation
                        .pecConsultation(pecConsultation)
                        .tmConsultation(tmConsultation)
                        .nombreConsultations(nbConsultations)
                        // Examen
                        .pecExamen(pecExamen)
                        .tmExamen(tmExamen)
                        .nombreExamens(nbExamens)
                        // Ordonnance
                        .pecOrdonnance(pecOrdonnance)
                        .tmOrdonnance(tmOrdonnance)
                        .nombreOrdonnances(nbOrdonnances)
                        .build())
                .ayantsDroits(ayantsDroits.stream()
                        .map(ad -> buildAyantDroitDTO(ad, debutAnnee, maintenant))
                        .collect(Collectors.toList()))
                .nombreAyantsDroits(ayantsDroits.size())
                .dernieresVisites(pageVisitesList)
                .totalVisites(totalVisites)
                .pageVisites(page)
                .filteredTotalMontantBase(filteredTotalBase)
                .filteredTotalMontantPEC(filteredTotalPEC)
                .build();
    }

    // ── Builders VisiteRecenteDTO ─────────────────────────────────────────────

    private DashboardAdherentDTO.VisiteRecenteDTO toVisiteFromConsultation(
            Dbx45tyConsultation c,
            Map<String, String> prestNoms,
            Map<String, String> adNoms) {

        boolean isAD  = c.getVisiteId() != null && c.getVisiteId().getCodeAyantDroit() != null;
        String benefNom = isAD
                ? adNoms.getOrDefault(c.getVisiteId().getCodeAyantDroit(), "Ayant droit")
                : (c.getVisiteId() != null ? c.getVisiteId().getCodeAdherent().getAssurePrincipal() : "");

        String prestId  = c.getVisiteId() != null ? c.getVisiteId().getPrestataireId() : "";
        String prestNom = prestNoms.getOrDefault(prestId, prestId != null ? prestId : "—");

        double montant = c.getMontantModif() != null ? c.getMontantModif()
                       :(c.getMontant()!= 0.0 ? c.getMontant() : 0.0);
        double taux = c.getTaux() != null ? c.getTaux() : 0.0;

        BigDecimal pec = BigDecimal.valueOf(montant * taux / 100.0)
                .setScale(0, RoundingMode.HALF_UP);
        BigDecimal tm  = taux >= 100 ? BigDecimal.ZERO
                       : BigDecimal.valueOf(montant * (100 - taux) / 100.0)
                           .setScale(0, RoundingMode.HALF_UP);

        return DashboardAdherentDTO.VisiteRecenteDTO.builder()
                .date(c.getDate())
                .prestataireId(prestId)
                .nomPrestataire(prestNom)
                .typePrestation("consultation")
                .montant(BigDecimal.valueOf(montant).setScale(0, RoundingMode.HALF_UP))
                .montantPriseEnCharge(pec)
                .montantTicketModerateur(tm)
                .taux(taux)
                .etat(c.getEtatConsultation())
                .ayantDroit(isAD)
                .nomBeneficiaire(benefNom)
                .build();
    }

    private DashboardAdherentDTO.VisiteRecenteDTO toVisiteFromLigne(
            Dbx45tyLignePrestation lp,
            Map<String, String> adNoms) {

        Dbx45tyVisite visite = lp.getPrestationId() != null ? lp.getPrestationId().getVisiteId() : null;
        boolean isAD = visite != null && visite.getCodeAyantDroit() != null;
        String benefNom = isAD
                ? adNoms.getOrDefault(visite.getCodeAyantDroit(), "Ayant droit")
                : (visite != null ? visite.getCodeAdherent().getAssurePrincipal() : "");

        String prestId  = lp.getPrestataireId() != null ? lp.getPrestataireId().getId() : "";
        String prestNom = lp.getPrestataireId() != null && lp.getPrestataireId().getNom() != null
                ? lp.getPrestataireId().getNom() : (prestId != null ? prestId : "—");

        double valeur  = lp.getValeurModif() != null ? lp.getValeurModif()
                       : (lp.getValeur() != null ? lp.getValeur() : 0.0);
        double nbre    = lp.getNbreModif()  != null ? lp.getNbreModif()
                       : (lp.getNbre()  != null ? lp.getNbre()  : 1.0);
        double taux    = lp.getTaux() != null ? lp.getTaux() : 0.0;
        double montant = valeur * nbre;

        BigDecimal pec = BigDecimal.valueOf(montant * taux / 100.0)
                .setScale(0, RoundingMode.HALF_UP);
        BigDecimal tm  = taux >= 100 ? BigDecimal.ZERO
                       : BigDecimal.valueOf(montant * (100 - taux) / 100.0)
                           .setScale(0, RoundingMode.HALF_UP);

        String nature = lp.getPrestationId() != null
                ? lp.getPrestationId().getNaturePrestation() : "prestation";

        return DashboardAdherentDTO.VisiteRecenteDTO.builder()
                .date(lp.getDate())
                .prestataireId(prestId)
                .nomPrestataire(prestNom)
                .typePrestation(nature)
                .montant(BigDecimal.valueOf(montant).setScale(0, RoundingMode.HALF_UP))
                .montantPriseEnCharge(pec)
                .montantTicketModerateur(tm)
                .taux(taux)
                .etat(lp.getEtat())
                .ayantDroit(isAD)
                .nomBeneficiaire(benefNom)
                .build();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private DashboardAdherentDTO.AyantDroitDTO buildAyantDroitDTO(
            Dbx45tyAyantDroit ad, LocalDateTime debut, LocalDateTime fin) {

        BigDecimal montant = safe(consultationRepository
                .getMontantRembourseParAyantDroit(ad.getCodeAyantDroit(), debut, fin))
                .add(safe(lignePrestationRepository
                .getMontantRembourseParAyantDroit(ad.getCodeAyantDroit(), debut, fin)));

        return DashboardAdherentDTO.AyantDroitDTO.builder()
                .codeAyantDroit(ad.getCodeAyantDroit())
                .nom(ad.getNom())
                .sexe(ad.getSexe())
                .lienpare(ad.getLienpare())
                .statut(ad.getStatut())
                .naissance(ad.getNaissance())
                .montantConsomme(montant)
                .build();
    }

    private long calculerJoursRestants(Date echeance) {
        if (echeance == null) return 365;
        // java.sql.Date.toInstant() lève UnsupportedOperationException
        LocalDate localDate = ((java.sql.Date) echeance).toLocalDate();
        return ChronoUnit.DAYS.between(LocalDate.now(), localDate);
    }

    private String niveauAlertEcheance(long jours) {
        if (jours < 0)   return "EXPIRE";
        if (jours <= 5)  return "DANGER";
        if (jours <= 30) return "WARNING";
        return "NORMAL";
    }

    private String niveauPlafond(double pourcentage) {
        if (pourcentage >= 90) return "DANGER";
        if (pourcentage >= 70) return "WARNING";
        return "NORMAL";
    }

    private BigDecimal safe(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    /**
     * Parse une date string "yyyy-MM-dd" en LocalDateTime.
     * @param isStart true → minuit du jour (00:00), false → fin de journée (23:59:59)
     */
    private LocalDateTime parseDateFilter(String dateStr, boolean isStart) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            LocalDate d = LocalDate.parse(dateStr);
            return isStart ? d.atStartOfDay() : d.atTime(23, 59, 59);
        } catch (Exception e) {
            log.warn("[DashboardAdherent] Impossible de parser la date filtre '{}': {}", dateStr, e.getMessage());
            return null;
        }
    }
}
