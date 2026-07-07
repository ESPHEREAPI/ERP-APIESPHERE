package com.esphere.webservicecron.controller;

import com.esphere.webservicecron.dto.response.DashboardStatsResponse;
import com.esphere.webservicecron.dto.response.HistoriqueSyncResponse;
import com.esphere.webservicecron.dto.response.PageResponse;
import com.esphere.webservicecron.dto.response.PoliceStatusResponse;
import com.esphere.webservicecron.service.HistoriqueSynchronisationService;
import com.esphere.webservicecron.service.PoliceStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/**
 * Vues HTML (Thymeleaf + Bootstrap) du dashboard de pilotage manuel et de
 * supervision des synchronisations webservice-cron. Les actions (boutons
 * "Lancer") appellent en AJAX les endpoints REST existants sous /webservice/*
 * (voir SynchronisationController) — ce controller ne fait que du rendu.
 */
@Controller
@RequestMapping("/webservice/dashboard")
@RequiredArgsConstructor
public class WebDashboardController {

    private static final String[] TYPES_HISTORIQUE = {
            "ADHERENT", "AYANT_DROIT", "TAUX_PRESTATION",
            "DESACTIVATION_ADHERENT", "DESACTIVATION_AYANT_DROIT"
    };

    private final HistoriqueSynchronisationService historiqueSynchronisationService;
    private final PoliceStatusService policeStatusService;

    // GET /webservice/dashboard ou /webservice/dashboard/  →  vue d'ensemble
    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        DashboardStatsResponse stats = historiqueSynchronisationService.obtenirStatistiquesDashboard();
        model.addAttribute("stats", stats);
        model.addAttribute("pageActive", "dashboard");
        return "dashboard";
    }

    // GET /webservice/dashboard/declenchement  →  déclenchement manuel par endpoint
    @GetMapping("/declenchement")
    public String declenchement(Model model) {
        model.addAttribute("pageActive", "declenchement");
        return "declenchement";
    }

    // GET /webservice/dashboard/historique  →  historique paginé et filtrable
    @GetMapping("/historique")
    public String historique(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateDebut"));
        PageResponse<HistoriqueSyncResponse> resultat = historiqueSynchronisationService
                .rechercherHistorique(type, dateDebut, dateFin, pageable);

        model.addAttribute("resultat", resultat);
        model.addAttribute("types", TYPES_HISTORIQUE);
        model.addAttribute("typeSelectionne", type);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);
        model.addAttribute("pageActive", "historique");
        return "historique";
    }

    // GET /webservice/dashboard/historique/{id}  →  détail d'une exécution
    @GetMapping("/historique/{id}")
    public String historiqueDetail(@PathVariable Long id, Model model) {
        HistoriqueSyncResponse detail = historiqueSynchronisationService.obtenirDetail(id);
        model.addAttribute("detail", detail);
        model.addAttribute("pageActive", "historique");
        return "historique-detail";
    }

    // GET /webservice/dashboard/police[?police=...]  →  fiche d'état d'une police
    @GetMapping("/police")
    public String policeStatus(@RequestParam(required = false) String police, Model model) {
        model.addAttribute("policeRecherchee", police);
        model.addAttribute("pageActive", "police");

        if (police != null && !police.isBlank()) {
            PoliceStatusResponse statut = policeStatusService.obtenirStatut(police.trim());
            model.addAttribute("statut", statut);
            model.addAttribute("introuvable", statut == null);
        }
        return "police";
    }

    // GET /webservice/dashboard/polices  →  vue d'ensemble de toutes les polices
    @GetMapping("/polices")
    public String toutesLesPolices(Model model) {
        model.addAttribute("polices", policeStatusService.obtenirToutesLesPolices());
        model.addAttribute("pageActive", "polices");
        return "polices";
    }
}
