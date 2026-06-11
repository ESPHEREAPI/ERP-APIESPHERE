///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package db.biometry.biometry.web;
//
//import db.biometry.biometry.dtos.Bons_adherent_details;
//import db.biometry.biometry.dtos.PrestataireStatsDTO;
//import db.biometry.biometry.dtos.Report;
//import db.biometry.biometry.dtos.SouscripteurBons;
//import db.biometry.biometry.dtos.Static_Souscripteur;
//import db.biometry.biometry.dtos.StringResult;
//import db.biometry.biometry.dtos.Zen_Sinistre_BioDto;
//import db.biometry.biometry.repositories.ConsultationTableRepository;
//import db.biometry.biometry.repositories.Dbx45tyLignePrestationRepositories;
//import db.biometry.biometry.repositories.ReportRepositories;
//import db.biometry.biometry.services.LignePrestationService;
//import db.biometry.biometry.services.ReportService;
//import db.biometry.biometry.vues.LignePrestation;
//import java.io.Serializable;
//import java.util.List;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// *
// * @author USER01
// */
//@RestController
//@AllArgsConstructor
//@Slf4j
//@CrossOrigin("*")
//public class LignePrestationRestController implements Serializable {
//
//    private LignePrestationService lignePrestationService;
//    private ReportService reportService;
//    private ConsultationTableRepository consultationTableRepository;
//    private ReportRepositories repoprtRepositories;
//    private Dbx45tyLignePrestationRepositories lignePrestationRepositories;
//
//    @GetMapping("/biometry/prestation-beneficiaire-exam")
//
//    public List<LignePrestation> listePrestationBeneficiares() {
//        return lignePrestationService.listesBeneficiaireHavePrestation();
//    }
//
//    @GetMapping("/biometry/prestation-beneficiaire-ordon")
//
//    public List<LignePrestation> listePrestationBeneficiaresOrdonance() {
//        return lignePrestationService.listesBeneficiaireHavePrestationOrdonance();
//    }
//
//    @GetMapping("/biometry/prestations")
//
//    public List<Zen_Sinistre_BioDto> listeLignePrestation() {
//        return lignePrestationService.listesAllLignePrestations();
//    }
//
//    @GetMapping("/biometry/{codePrestataire}")
//    public List<LignePrestation> listePrestatairesBycode(@PathVariable(name = "codePrestataire") String codePrestataire) {
//        return this.lignePrestationService.listesPrestationByCodePrestataire(codePrestataire);
//    }
//
//    @GetMapping("/biometry/consultations")
//
//    public List<Zen_Sinistre_BioDto> listeConsultations() {
//        return lignePrestationService.listesConsultation();
//    }
//
//    @PostMapping("/biometry/consultations/print")
//    public StringResult printConsultation(@RequestBody Report report) {
//        StringResult reportName = null;
//        try {
//            reportName = this.reportService.createReportConsultationByDataSource(report);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return reportName;
//    }
//
//    @GetMapping("biometry/consultations/pdf")
//    public ResponseEntity<byte[]> generedConsultationsPdf() {
//        Report report = new Report();
//        return reportService.createReportConsultation(report);
//
//    }
//
//    @GetMapping("biometry/consultations/bons")
//    public ResponseEntity<byte[]> generationBons() {
//
//        return reportService.genereBonsConsultation();
//    }
//
//    @GetMapping("biometry/statistique/bons")
//    public ResponseEntity<byte[]> statistiqueBons() {
//
//        return reportService.genereStatistiqueBons();
//    }
//
//    @PostMapping("biometry/statistique/souscripteur-name")
//    public Static_Souscripteur statistiqueSouscripteurByName(@RequestBody String compagnies) {
//
//        return lignePrestationService.staticSouscripteur(compagnies);
//    }
//
//    @GetMapping("biometry/statistique/souscripteur")
//    public ResponseEntity<byte[]> statistiqueSouscripteur() {
//
//        return reportService.genereStatistiqueBons_Souscripteur();
//    }
//
//    @GetMapping("biometry/statistique/souscripteur-rg2")
//    public ResponseEntity<byte[]> statistiqueSouscripteurRg2() {
//
//        return reportService.genereStatistiqueBons_SouscripteurRegion2();
//    }
//
//    @PostMapping("biometry/statistique/bons-compagnie")
//    public ResponseEntity<byte[]> statistiqueBonsCompagnie(@RequestBody db.biometry.biometry.entite.Report report) {
//        return reportService.statistiqueBons_SouscripteurR(report);
//    }
//
//    @GetMapping("biometry/statistique/bons-compagnie/{id}")
//    public ResponseEntity<byte[]> statistiqueBonsCompagnie(@PathVariable(name = "id") Long idreport) {
//        db.biometry.biometry.entite.Report report = repoprtRepositories.findById(idreport).orElseThrow();
//        if (report != null) {
//            return reportService.statistiqueBons_SouscripteurR(report);
//        }
//        return null;
//    }
//
//    @PostMapping("biometry/report")
//    public db.biometry.biometry.entite.Report createReport(@RequestBody Report report) {
//
//        return lignePrestationService.createreport(report);
//
//    }
//
//    @GetMapping("biometry/bons-by-adherent-for-compagnie")
//    public List<SouscripteurBons> listeBonsAdherent() {
//
//        return lignePrestationService.listePrestationsAssureByPeriode(null, null, null, null);
//
//    }
//    
//     @GetMapping("biometry/bons-details-adherent-for-compagnie")
//    public List<Bons_adherent_details> listeBonsAdherentDetails() {
//
//        return lignePrestationService.listePrestationsAssureByPeriodeDetails(null, null, null, null);
//
//    }
//    @GetMapping("biometry/prestataire-online-consultation")
//    public List<PrestataireStatsDTO>listePrestataireOnLine(){
////        consultationTableRepository.listePrestataireOnlineConsultation().forEach(System.out::println);
////        return  this.consultationTableRepository.listePrestataireOnlineConsultation();
//return null;
//        
//    }
//    
//     @GetMapping("biometry/prestataire-online-prestation")
//    public List<PrestataireStatsDTO>listePrestataireOnLinePrestation(){
//        lignePrestationRepositories.listePrestataireOnlinePrestation().forEach(System.out::println);
//        return  this.lignePrestationRepositories.listePrestataireOnlinePrestation();
//        
//    }
//    
//     @GetMapping("biometry/prestataire-online-cat/{idcategorie}")
//    public List<PrestataireStatsDTO>listePrestataireOnLine(@PathVariable(name = "idcategorie") String categorie ){
////       consultationTableRepository.listePrestataireOnlineConsultation(categorie).forEach(System.out::println);
////        return  this.consultationTableRepository.listePrestataireOnlineConsultation(categorie);
////        
//return null;
//    }
//
//}
