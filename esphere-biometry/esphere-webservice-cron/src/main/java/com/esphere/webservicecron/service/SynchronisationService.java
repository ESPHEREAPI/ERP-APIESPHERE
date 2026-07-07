package com.esphere.webservicecron.service;

import com.esphere.webservicecron.dto.response.*;
import com.esphere.webservicecron.entity.Adherent;
import com.esphere.webservicecron.entity.AyantDroit;
import com.esphere.webservicecron.entity.TauxPrestation;
import com.esphere.webservicecron.entity.TypePrestation;
import com.esphere.webservicecron.repository.AdherentRepository;
import com.esphere.webservicecron.repository.AyantDroitRepository;
import com.esphere.webservicecron.repository.TauxPrestationRepository;
import com.esphere.webservicecron.repository.TypePrestationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.*;

/**
 * Équivalent Java des actions de synchronisation du module PHP Zend
 * "Webservice" (Webservice\Controller\IndexController) :
 *
 *  - recupererDonneesAdherentAction      → {@link #importerAdherents}
 *  - recupererDonneesAyantDroitAction    → {@link #importerAyantsDroit}
 *  - recupererDonneesTauxPrestationAction→ {@link #importerTauxPrestation}
 *  - desactiverDonneesAdherentAction     → {@link #desactiverAdherents}
 *  - desactiverDonneesAyantDroitAction   → {@link #desactiverAyantsDroit}
 *
 * Les règles métier codées en dur dans le PHP (souscripteurs forcés par
 * numéro de police, taux 80/100%, groupes ONCC, fraudeurs CAMTEL) sont
 * reproduites à l'identique.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SynchronisationService {

    private final WebClient externalBiometryWebClient;
    private final ObjectMapper objectMapper;

    private final AdherentRepository adherentRepository;
    private final AyantDroitRepository ayantDroitRepository;
    private final TypePrestationRepository typePrestationRepository;
    private final TauxPrestationRepository tauxPrestationRepository;

    // Équivalent de IndexController::$tabEquiv (PHP)
    private static final Map<String, String> TAB_EQUIV = Map.of(
            "CG", "CS0", "CGJ", "CS0", "CGN", "CS0",
            "CS", "CS1", "CSJ", "CS1", "CSN", "CS1",
            "CP", "CS2", "CPJ", "CS2", "CPN", "CS2",
            "ME01", "PH02"
    );

    private static final Set<String> CODES_ADHERENTS_TAUX_80 = Set.of(
            "39_1017-2130000110", "38_1017-2130000110", "55_1017-2130000110",
            "56_1017-2130000110", "57_1017-2130000110", "58_1017-2130000110",
            "59_1017-2130000110", "202_1017-2130000110", "203_1017-2130000110",
            "258_1017-2130000110"
    );

    private static final Set<String> CODES_ADHERENTS_GROUPE_22 = Set.of(
            "226_1017-2130000084", "227_1017-2130000084", "228_1017-2130000084",
            "229_1017-2130000084", "230_1017-2130000084", "3_1017-2130000084",
            "54_1017-2130000110", "55_1017-2130000110", "56_1017-2130000110",
            "57_1017-2130000110", "58_1017-2130000110", "59_1017-2130000110"
    );

    private static final Set<String> CODES_ADHERENTS_GROUPE_33 = Set.of(
            "202_1017-2130000110", "203_1017-2130000110", "204_1017-2130000110",
            "231_1017-2130000084", "267_1017-2130000084", "268_1017-2130000084"
    );

    private static final Set<String> CODES_ADHERENTS_GROUPE_44 = Set.of(
            "232_1017-2130000084", "233_1017-2130000084",
            "257_1017-2130000110", "258_1017-2130000110"
    );

    private static final Set<String> CODES_ADHERENTS_FRAUDEURS_CAMTEL = Set.of(
            "1621_1017-2130000100", "2226_1017-2130000100",
            "2135_1017-2130000100", "3210_1017-2130000100"
    );

    private static final Set<String> POLICES_TAUX_100 = Set.of(
            "1017-2130000062", "1017-2130000084", "1017-2130000110"
    );

    // ── 1. recupererDonneesAdherentAction ───────────────────────────────

    @Transactional
    public SyncReportResponse importerAdherents(String policeFiltre) {
        AdherentExterneResponse reponse = fetchExterne("/get-liste-adherent", AdherentExterneResponse.class);

        if (reponse == null || reponse.getTabAdherent() == null) {
            return SyncReportResponse.builder().nbTraites(0).nbEchecs(0)
                    .lignesEnEchec(List.of("Transaction non initialisee")).build();
        }
        if (reponse.getError() != null && !reponse.getError().isBlank()) {
            return SyncReportResponse.builder().nbTraites(0).nbEchecs(0)
                    .lignesEnEchec(List.of(reponse.getError())).build();
        }

        int traites = 0;
        List<String> echecs = new ArrayList<>();
        List<String> traitesDetail = new ArrayList<>();

        for (AdherentExterneDTO dto : reponse.getTabAdherent()) {
            try {
                String police = trim(dto.getPolice());
                if (policeFiltre != null && !policeFiltre.equals(police)) {
                    continue;
                }

                String codeAdherent = trim(dto.getCodeAssure()) + "_" + police;

                Adherent adherent = adherentRepository.findById(codeAdherent).orElse(null);
                boolean nouveau = adherent == null;
                if (nouveau) {
                    adherent = new Adherent();
                    adherent.setCodeAdherent(codeAdherent);
                    adherent.setEnrole("-1");
                    adherent.setImprime("-1");
                }

                adherent.setAssurePrincipal(trim(dto.getAssurePrincipal()));
                adherent.setSexe(trim(dto.getSexe()));
                adherent.setMatricule(trim(dto.getMatricule()));
                if (notBlank(dto.getTelephone())) {
                    adherent.setTelephone(trim(dto.getTelephone()));
                }
                adherent.setVille(trim(dto.getVille()));
                adherent.setSouscripteur(trim(dto.getSouscripteur()));
                adherent.setPolice(police);
                adherent.setNaissance(parseDate(dto.getNaissance()));
                adherent.setEffetPolice(parseDate(dto.getEffetPolice()));
                adherent.setEcheancePolice(parseDate(dto.getEcheancePolice()));
                adherent.setTaux(parseDouble(dto.getTaux()));
                adherent.setPlafondAssurep(parseDouble(dto.getPlafondAssurep()));
                adherent.setConsAp(parseDouble(dto.getConsAp()));

                // Souscripteurs forcés par numéro de police (règle PHP)
                switch (police) {
                    case "1017-2130000081" -> adherent.setSouscripteur("CCIMA");
                    case "1017-2130000101" -> adherent.setSouscripteur("CIRCB");
                    case "1017-2130000063" -> adherent.setSouscripteur("PRODEL");
                    case "1017-2130000073" -> adherent.setSouscripteur("BMN");
                    case "1017-2130000092" -> adherent.setSouscripteur("CAA-PROJET REGIONAL PCDN");
                    case "1001-2130000020" -> adherent.setSouscripteur("JICA");
                    case "1017-2130000100" -> adherent.setSouscripteur("CAMTEL");
                    case "1001-2130000032" -> adherent.setSouscripteur("INTERPOL");
                    case "1017-2130000131" -> adherent.setSouscripteur("PDCVEP");
                    default -> { /* pas d'override */ }
                }

                // Taux 80/100% pour la police ONCC 1017-2130000110
                if ("1017-2130000110".equals(police)) {
                    adherent.setTaux(CODES_ADHERENTS_TAUX_80.contains(codeAdherent) ? 80d : 100d);
                }

                if (nouveau) {
                    adherent.setStatut("1");
                }

                adherentRepository.save(adherent);
                traites++;
                traitesDetail.add(codeAdherent + " (" + (nouveau ? "cree" : "maj")
                        + ", " + adherent.getAssurePrincipal() + ")");
            } catch (Exception e) {
                log.error("Echec import adherent : {}", e.getMessage());
                echecs.add(dto.getCodeAssure() + " : " + e.getMessage());
            }
        }

        return SyncReportResponse.builder().nbTraites(traites).nbEchecs(echecs.size())
                .lignesTraitees(traitesDetail).lignesEnEchec(echecs).build();
    }

    // ── 2. recupererDonneesAyantDroitAction ─────────────────────────────

    @Transactional
    public SyncReportResponse importerAyantsDroit(String codeAdherentFiltre) {
        AyantDroitExterneResponse reponse = fetchExterne("/get-liste-ayant-droit", AyantDroitExterneResponse.class);

        if (reponse == null || reponse.getTabAyantDroit() == null) {
            return SyncReportResponse.builder().nbTraites(0).nbEchecs(0)
                    .lignesEnEchec(List.of("Transaction non initialisee")).build();
        }
        if (reponse.getError() != null && !reponse.getError().isBlank()) {
            return SyncReportResponse.builder().nbTraites(0).nbEchecs(0)
                    .lignesEnEchec(List.of(reponse.getError())).build();
        }

        int traites = 0;
        List<String> echecs = new ArrayList<>();
        List<String> traitesDetail = new ArrayList<>();

        for (AyantDroitExterneDTO dto : reponse.getTabAyantDroit()) {
            try {
                String police = trim(dto.getPolice());
                String codeAdherent = trim(dto.getCodeAssure()) + "_" + police;

                if (codeAdherentFiltre != null && !codeAdherentFiltre.equals(codeAdherent)) {
                    continue;
                }

                Adherent adherent = adherentRepository.findById(codeAdherent).orElse(null);
                if (adherent == null) {
                    continue; // PHP : ignore si l'adhérent n'existe pas encore
                }

                String codeAyantDroit = codeAdherent + "_" + trim(dto.getCodeAyantD());

                AyantDroit ayantDroit = ayantDroitRepository.findById(codeAyantDroit).orElse(null);
                boolean nouveau = ayantDroit == null;
                if (nouveau) {
                    ayantDroit = new AyantDroit();
                    ayantDroit.setCodeAyantDroit(codeAyantDroit);
                    ayantDroit.setEnrole("-1");
                }

                ayantDroit.setNom(trim(dto.getAyantsDroits()));
                ayantDroit.setSexe(trim(dto.getSexe()));
                ayantDroit.setNaissance(parseDate(dto.getNaissance()));
                ayantDroit.setLienPare(trim(dto.getLienpare()).replace(" ", ""));
                if (notBlank(dto.getTelephone())) {
                    ayantDroit.setTelephone(trim(dto.getTelephone()));
                }
                ayantDroit.setPolice(police);
                ayantDroit.setAdherent(adherent);

                if (nouveau) {
                    ayantDroit.setStatut("1");
                }

                ayantDroitRepository.save(ayantDroit);
                traites++;
                traitesDetail.add(codeAyantDroit + " (" + (nouveau ? "cree" : "maj")
                        + ", " + ayantDroit.getNom() + ")");
            } catch (Exception e) {
                log.error("Echec import ayant droit : {}", e.getMessage());
                echecs.add(dto.getCodeAyantD() + " : " + e.getMessage());
            }
        }

        return SyncReportResponse.builder().nbTraites(traites).nbEchecs(echecs.size())
                .lignesTraitees(traitesDetail).lignesEnEchec(echecs).build();
    }

    // ── 3. recupererDonneesTauxPrestationAction ─────────────────────────

    /**
     * @param policeFiltre si renseignée (ex: "1017-2130000110"), seules les
     *                     lignes dont la police construite (CODEINTE-NUMEPOLI)
     *                     correspond sont importées.
     */
    @Transactional
    public SyncReportResponse importerTauxPrestation(String policeFiltre) {
        TauxPrestationExterneResponse reponse = fetchExterne("/get-liste-taux-prestation", TauxPrestationExterneResponse.class);

        if (reponse == null || reponse.getTabTauxPrestation() == null) {
            return SyncReportResponse.builder().nbTraites(0).nbEchecs(0)
                    .lignesEnEchec(List.of("Transaction non initialisee")).build();
        }
        if (reponse.getError() != null && !reponse.getError().isBlank()) {
            return SyncReportResponse.builder().nbTraites(0).nbEchecs(0)
                    .lignesEnEchec(List.of(reponse.getError())).build();
        }

        int traites = 0;
        List<String> echecs = new ArrayList<>();
        List<String> traitesDetail = new ArrayList<>();

        for (Map<String, Object> ligne : reponse.getTabTauxPrestation()) {
            try {
                String codePres = strVal(ligne.get("CODEPRES"));
                String codeInte = strVal(ligne.get("CODEINTE"));
                String numePoli = strVal(ligne.get("NUMEPOLI"));
                String numeGrou = strVal(ligne.get("NUMEGROU"));
                String typConsu = strVal(ligne.get("TYPCONSU"));

                if (isBlank(codePres) || isBlank(codeInte) || isBlank(numePoli)
                        || isBlank(numeGrou) || isBlank(typConsu)) {
                    echecs.add("Ligne incomplete : " + ligne);
                    continue;
                }

                String police = trim(codeInte) + "-" + trim(numePoli);
                if (policeFiltre != null && !policeFiltre.equals(police)) {
                    continue;
                }

                if (TAB_EQUIV.containsKey(codePres)) {
                    codePres = TAB_EQUIV.get(codePres);
                }

                String tauxCouvBrut = strVal(ligne.get("TAUXCOUV"));
                String valePlafBrut = strVal(ligne.get("VALEPLAF"));

                String typePrestationId = trim(codePres);
                short groupe = (short) parseInt(trim(numeGrou));

                TypePrestation typePrestation = typePrestationRepository.findById(typePrestationId).orElse(null);
                if (typePrestation == null) {
                    typePrestation = TypePrestation.builder()
                            .id(typePrestationId)
                            .nom(trim(typConsu))
                            .affiche(-1)
                            .categorie("non_defini")
                            .build();
                    try {
                        typePrestationRepository.save(typePrestation);
                    } catch (Exception e) {
                        log.error("Echec creation type prestation {} : {}", typePrestationId, e.getMessage());
                        echecs.add("TypePrestation " + typePrestationId + " : " + e.getMessage());
                    }
                }

                TauxPrestation tauxPrestation = tauxPrestationRepository
                        .findByTypePrestationIdAndPoliceAndGroupe(typePrestationId, police, groupe)
                        .orElse(null);
                boolean nouveau = tauxPrestation == null;
                if (nouveau) {
                    tauxPrestation = new TauxPrestation();
                    tauxPrestation.setTypePrestationId(typePrestationId);
                    tauxPrestation.setPolice(police);
                    tauxPrestation.setGroupe(groupe);
                }

                tauxPrestation.setTaux(isBlank(tauxCouvBrut) ? null : parseInt(trim(tauxCouvBrut)));
                tauxPrestation.setPlafond(isBlank(valePlafBrut) ? null : Float.valueOf(trim(valePlafBrut)));

                // Taux ONCC force a 100%
                if (POLICES_TAUX_100.contains(police)) {
                    tauxPrestation.setTaux(100);
                }

                tauxPrestationRepository.save(tauxPrestation);
                traites++;
                traitesDetail.add(typePrestationId + " / " + police + " / groupe " + groupe
                        + " (" + (nouveau ? "cree" : "maj") + ", taux=" + tauxPrestation.getTaux() + ")");
            } catch (Exception e) {
                log.error("Echec import taux prestation : {}", e.getMessage());
                echecs.add(ligne + " : " + e.getMessage());
            }
        }

        return SyncReportResponse.builder().nbTraites(traites).nbEchecs(echecs.size())
                .lignesTraitees(traitesDetail).lignesEnEchec(echecs).build();
    }

    // ── 4. desactiverDonneesAdherentAction ──────────────────────────────

    @Transactional
    public SyncReportResponse desactiverAdherents(String policeFiltre) {
        AdherentExterneResponse reponse = fetchExterne("/get-liste-adherent", AdherentExterneResponse.class);

        if (reponse == null || reponse.getTabAdherent() == null
                || (reponse.getError() != null && !reponse.getError().isBlank())) {
            return SyncReportResponse.builder().nbTraites(0).nbEchecs(0)
                    .lignesEnEchec(List.of("Transaction non initialisee ou en erreur")).build();
        }

        Set<String> codesAdherentsSource = new HashSet<>();
        for (AdherentExterneDTO dto : reponse.getTabAdherent()) {
            String police = trim(dto.getPolice());
            if (policeFiltre != null && !policeFiltre.equals(police)) {
                continue;
            }
            codesAdherentsSource.add(trim(dto.getCodeAssure()) + "_" + police);
        }

        String dateServeur = LocalDate.now().toString();
        List<Adherent> tousLesAdherents = adherentRepository.findAll();
        Map<String, String> statutAvant = new HashMap<>();
        tousLesAdherents.forEach(a -> statutAvant.put(a.getCodeAdherent(), a.getStatut()));

        for (Adherent adherent : tousLesAdherents) {
            String code = adherent.getCodeAdherent();

            if (CODES_ADHERENTS_GROUPE_22.contains(code)) {
                adherent.setGroupe((short) 22);
                adherent.setStatut("1");
            } else if (CODES_ADHERENTS_GROUPE_33.contains(code)) {
                adherent.setGroupe((short) 33);
                adherent.setStatut("1");
            } else if (CODES_ADHERENTS_GROUPE_44.contains(code)) {
                adherent.setGroupe((short) 44);
                adherent.setStatut("1");
            } else {
                adherent.setStatut("-1");
            }

            if ("1017-2130000084".equals(adherent.getPolice())
                    && adherent.getEcheancePolice() != null
                    && adherent.getEcheancePolice().toString().compareTo(dateServeur) > 0) {
                adherent.setStatut("1");
            }
        }

        for (Adherent adherent : tousLesAdherents) {
            if (codesAdherentsSource.contains(adherent.getCodeAdherent())) {
                adherent.setStatut("1");
            }
            if (CODES_ADHERENTS_FRAUDEURS_CAMTEL.contains(adherent.getCodeAdherent())) {
                adherent.setStatut("-1");
            }
        }

        adherentRepository.saveAll(tousLesAdherents);

        List<String> changements = new ArrayList<>();
        for (Adherent adherent : tousLesAdherents) {
            String ancien = statutAvant.get(adherent.getCodeAdherent());
            if (!Objects.equals(ancien, adherent.getStatut())) {
                changements.add(adherent.getCodeAdherent() + " : " + ancien + " -> " + adherent.getStatut());
            }
        }

        return SyncReportResponse.builder()
                .nbTraites(tousLesAdherents.size()).nbEchecs(0)
                .lignesTraitees(changements).lignesEnEchec(List.of()).build();
    }

    // ── 5. desactiverDonneesAyantDroitAction ────────────────────────────

    @Transactional
    public SyncReportResponse desactiverAyantsDroit(String codeAdherentFiltre) {
        AyantDroitExterneResponse reponse = fetchExterne("/get-liste-ayant-droit", AyantDroitExterneResponse.class);

        if (reponse == null || reponse.getTabAyantDroit() == null
                || (reponse.getError() != null && !reponse.getError().isBlank())) {
            return SyncReportResponse.builder().nbTraites(0).nbEchecs(0)
                    .lignesEnEchec(List.of("Transaction non initialisee ou en erreur")).build();
        }

        Set<String> codesAyantsDroitActifs = new HashSet<>();
        for (AyantDroitExterneDTO dto : reponse.getTabAyantDroit()) {
            String codeAdherent = trim(dto.getCodeAssure()) + "_" + trim(dto.getPolice());

            if (codeAdherentFiltre != null && !codeAdherentFiltre.equals(codeAdherent)) {
                continue;
            }

            Adherent adherent = adherentRepository.findById(codeAdherent).orElse(null);
            if (adherent != null && "1".equals(adherent.getStatut())) {
                codesAyantsDroitActifs.add(codeAdherent + "_" + trim(dto.getCodeAyantD()));
            }
        }

        List<AyantDroit> ayantsDroitActifs = ayantDroitRepository.findByStatut("1");
        int desactives = 0;
        List<String> desactivesDetail = new ArrayList<>();
        for (AyantDroit ayantDroit : ayantsDroitActifs) {
            if (!codesAyantsDroitActifs.contains(ayantDroit.getCodeAyantDroit())) {
                ayantDroit.setStatut("-1");
                desactives++;
                desactivesDetail.add(ayantDroit.getCodeAyantDroit() + " (" + ayantDroit.getNom() + ")");
            }
        }
        ayantDroitRepository.saveAll(ayantsDroitActifs);

        return SyncReportResponse.builder()
                .nbTraites(desactives).nbEchecs(0)
                .lignesTraitees(desactivesDetail).lignesEnEchec(List.of()).build();
    }

    // ── Helpers ──────────────────────────────────────────────────────

    /**
     * Le serveur biométrie externe legacy répond en JSON mais avec un
     * Content-Type "text/html" : WebClient refuse de désérialiser
     * automatiquement dans ce cas (UnsupportedMediaTypeException).
     * On lit donc le corps en String brut et on parse nous-mêmes,
     * comme le fait esphere-validation-service (AdherentExterneService).
     */
    private <T> T fetchExterne(String uri, Class<T> type) {
        String corpsBrut = externalBiometryWebClient.get()
                .uri(uri)
                .accept(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON, MediaType.ALL)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (corpsBrut == null || corpsBrut.isBlank()) {
            log.warn("Reponse vide du serveur externe pour {}", uri);
            return null;
        }

        try {
            return objectMapper.readValue(corpsBrut, type);
        } catch (JsonProcessingException e) {
            log.error("Reponse non-JSON du serveur externe pour {} : {}", uri, e.getMessage());
            return null;
        }
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static String strVal(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static LocalDate parseDate(String raw) {
        if (isBlank(raw)) return null;
        String cleaned = raw.replace(" ", "");
        try {
            return LocalDate.parse(cleaned.length() >= 10 ? cleaned.substring(0, 10) : cleaned);
        } catch (Exception e) {
            return null;
        }
    }

    private static Double parseDouble(String raw) {
        if (isBlank(raw)) return null;
        try {
            return Double.valueOf(trim(raw));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int parseInt(String raw) {
        if (isBlank(raw)) return 0;
        try {
            return Integer.parseInt(trim(raw));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
