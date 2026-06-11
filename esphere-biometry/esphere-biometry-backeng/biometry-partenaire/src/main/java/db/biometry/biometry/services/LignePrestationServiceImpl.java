///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package db.biometry.biometry.services;
//
//import db.biometry.biometry.dtos.Bons_adherent_details;
//import db.biometry.biometry.dtos.PrestationDto;
//import db.biometry.biometry.dtos.SouscripteurBons;
//import db.biometry.biometry.dtos.Static_Souscripteur;
//import db.biometry.biometry.dtos.Zen_Sinistre_BioDto;
//import db.biometry.biometry.entite.Dbx45tyAdherent;
//import db.biometry.biometry.entite.Dbx45tyLignePrestation;
//import db.biometry.biometry.entite.Dbx45tyVisite;
//import db.biometry.biometry.entite.Report;
//
//import db.biometry.biometry.repositories.AdherentRepositories;
//import db.biometry.biometry.repositories.ConsultationRepositories;
//import db.biometry.biometry.repositories.Dbx45tyLignePrestationRepositories;
//import db.biometry.biometry.repositories.LignePrestationRepositories;
//import db.biometry.biometry.repositories.ReportRepositories;
//import db.biometry.biometry.repositories.VisiterRepository;
//import db.biometry.biometry.vues.Consultation;
//import db.biometry.biometry.vues.LignePrestation;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// *
// * @author USER01
// */
//@Service
//@AllArgsConstructor
//public class LignePrestationServiceImpl implements LignePrestationService {
//@Autowired
//    private LignePrestationRepositories lignePrestationRepositories;
//@Autowired
//    private BiometrieMapperImpl dtoMapper;
//@Autowired
//    private ConsultationRepositories consultationRepositories;
//@Autowired
//    private Dbx45tyLignePrestationRepositories lpRepositories;
//@Autowired
//    private ReportRepositories reportRepository;
//@Autowired
//    private AdherentRepositories adherentrepositories;
//@Autowired
//    private VisiterRepository visiteRepository;
//
//    @Override
//    public List<LignePrestation> listesBeneficiaireHavePrestation() {
//        return this.lignePrestationRepositories.findByCodeBeneficiaireIsNotNullAndCodeTypePrestationIsNotNull();
//
//    }
//
//    @Override
//    public List<LignePrestation> listesBeneficiaireHavePrestationOrdonance() {
//        return this.lignePrestationRepositories.findByCodeBeneficiaireIsNotNullAndCodeTypePrestationIsNull();
//    }
//
//    @Override
//    public List<Zen_Sinistre_BioDto> listesLignePrestationGroupByDate() {
//
//        List<Object> listeobjects = this.lignePrestationRepositories.listePrestataireDistinct();
//        List<PrestationDto> listePrestationDtos = listeobjects.stream().flatMap(ob -> dtoMapper.formObjetlignePrestations(ob).stream())
//                .collect(Collectors.toList());
////        List<Zen_Sinistre_BioDto> listesZenSinistresBio = listePrestationDtos.stream().map(pdto -> dtoMapper.ZenSinistreBiofromLignePrestation(pdto))
////                .collect(Collectors.toList());
//        return null;
//
//    }
//
//    @Override
//    public List<LignePrestation> listesPrestationByCodePrestataire(String codePrestataire) {
//        return this.lignePrestationRepositories.findByCodePrestataire(codePrestataire);
//    }
//
//    /*
//    fonction pour retourner les données vers orass
//     */
//    @Override
//    public List<Zen_Sinistre_BioDto> listesAllLignePrestations() {
//        List<Integer> listesId_prestations = this.lignePrestationRepositories.listesPrestionId();
//        List<LignePrestation> prestations = listesId_prestations.stream()
//                .map(id -> dtoMapper.listeLignePrestationByIdPrestation(id))
//                .collect(Collectors.toList());
//        List<Zen_Sinistre_BioDto> listesZenSinistresBio = prestations.stream().map(lp -> dtoMapper.ZenSinistreBiofromLignePrestation(lp))
//                .collect(Collectors.toList());
//        return listesZenSinistresBio;
//    }
//
//    @Override
//    public List<Zen_Sinistre_BioDto> listesConsultation() {
//        List<Consultation> consultation = consultationRepositories.findAll();
//
//        List<Zen_Sinistre_BioDto> listesZenSinistresBio = consultation.stream().map(c -> dtoMapper.ZenSinistreBiofromConsultation(c)).collect(Collectors.toList());
//        return listesZenSinistresBio;
//    }
//
//    @Override
//    public List<Static_Souscripteur> staticSouscripteur(Date debut, Date fin) {
//        List<Static_Souscripteur> listesStatic_Souscripteur = new ArrayList<>();
//        try {
////            Date date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-01-01 00:00:00");
////            
////            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-12-31 23:59:00");
////            //je recupere les souscripteurs pour une date precise
//            Date date1 = debut;
//            Date date2 = fin;
//            List<String> listeSouscripteur = lpRepositories.listesSouscripteurs(date1, date2);
//
//            listesStatic_Souscripteur = listeSouscripteur.stream()
//                    .map(s -> dtoMapper.fromSouscripteurReligion1(s, date1, date2))
//                    .collect(Collectors.toList());
//
//        } catch (Exception ex) {
//            Logger.getLogger(LignePrestationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return listesStatic_Souscripteur;
//    }
//
//    @Override
//    public Static_Souscripteur staticSouscripteur(String souscripteur) {
//
//        Static_Souscripteur listesStatic_Souscripteur = null;
//        try {
//            Date date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-01-01 00:00:00");
//
//            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-12-31 23:59:00");
//            //je recupere les souscripteurs pour une date precise
////            List<String> listeSouscripteur = lpRepositories.listesSouscripteurs(date1, date2);
//
//            listesStatic_Souscripteur
//                    = dtoMapper.fromSouscripteurReligion1(souscripteur, date1, date2);
//
//        } catch (ParseException ex) {
//            Logger.getLogger(LignePrestationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return listesStatic_Souscripteur;
//    }
//
//    @Override
//    public List<Static_Souscripteur> staticSouscripteurRegion2(Date debut, Date fin) {
//        List<Static_Souscripteur> listesStatic_Souscripteur = new ArrayList<>();
//        try {
////            Date date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-01-01 00:00:00");
////            
////            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-12-31 23:59:00");
////            //je recupere les souscripteurs pour une date precise
//            Date date1 = debut;
//            Date date2 = fin;
//            List<String> listeSouscripteur = lpRepositories.listesSouscripteurs(date1, date2);
//
//            listesStatic_Souscripteur = listeSouscripteur.stream()
//                    .map(s -> dtoMapper.fromSouscripteurReligion2(s, date1, date2))
//                    .collect(Collectors.toList());
//
//        } catch (Exception ex) {
//            Logger.getLogger(LignePrestationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return listesStatic_Souscripteur;
//    }
//
//    @Override
//    public List<Static_Souscripteur> staticSouscripteur(Date debut, Date fin, String ville) {
//        List<Static_Souscripteur> listesStatic_Souscripteur = new ArrayList<>();
//        try {
////            Date date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-01-01 00:00:00");
////            
////            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-12-31 23:59:00");
////            //je recupere les souscripteurs pour une date precise
//            Date date1 = debut;
//            Date date2 = fin;
//            List<String> listeSouscripteur = lpRepositories.listesSouscripteurs(date1, date2);
//
//            listesStatic_Souscripteur = listeSouscripteur.stream()
//                    .map(s -> dtoMapper.fromSouscripteur(s, date1, date2, ville))
//                    .collect(Collectors.toList());
//
//        } catch (Exception ex) {
//            Logger.getLogger(LignePrestationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return listesStatic_Souscripteur;
//    }
//
//    @Override
//    public Report createreport(db.biometry.biometry.dtos.Report r) {
//
//        Report rp = new Report();
//        rp.setDate_debut(r.getDate_debut());
//        rp.setDate_fin(r.getDate_fin());
//        rp.setVille(r.getVille());
//        rp.setName(r.getName());
//        this.reportRepository.save(rp);
//        return rp;
//
//    }
//
//    @Override
//    public List<SouscripteurBons> listePrestationsAssureByPeriode(String assures, String prestation, Date debut, Date fin) {
//
//        Date date1;
//        Date date2;
//        
//        List<SouscripteurBons> listeAdherentPrestation = new ArrayList<>();
//        try {
//            int i=0;
//            date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2025-07-24 01:00:00");
//            date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2025-09-03 00:59:00");
//            String souscriteur = "P 2 M PHARMA";
//            List<Dbx45tyLignePrestation> listeLignePrestation = new ArrayList<>();
//            List<Dbx45tyAdherent> adherents = this.adherentrepositories.findBySouscripteur(souscriteur);
////            this.lpRepositories.listeBonsPrestationByAssureByJoint(date1, date2, "101_1017-2130000084").forEach(l->  System.out.println(l.getDate()));
////           this.lpRepositories.listeBonsPrestationByAssures("99_1017-2130000110").forEach(l->  System.out.println(l.getDate()));
////           this.lpRepositories.listeBonsPrestationByAssuress(date1,date2).forEach(l->  System.out.println(l.getPrestationId().getVisiteId().getCodeAdherent()));
//
//            listeAdherentPrestation = adherents.stream()
//                    .filter(s -> listeVisite(date1, date2, "57_1017-2130000110") != null)
//                    .map(s -> dtoMapper.lignePrestationFronSouscripteurBonsWithVisit(listeVisite(date1, date2, "57_1017-2130000110"), souscriteur, "57_1017-2130000110", date1, date2))
//                    .collect(Collectors.toList());
//        } catch (ParseException ex) {
//            Logger.getLogger(LignePrestationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//
//        }catch (Exception e) {
//            Logger.getLogger(LignePrestationServiceImpl.class.getName()).log(Level.SEVERE, null, e);
//
//        }
//
//        return listeAdherentPrestation;
//
//    }
//
//    public Dbx45tyLignePrestation mapByObjet(Dbx45tyLignePrestation l) {
//        return l;
//    }
//
//    public List<Dbx45tyVisite> listeVisite(Date debut, Date fin, String codeAdherent) {
//        System.out.println("code Assure ---"+codeAdherent);
//        try {
//            
//             List<Dbx45tyVisite> visite = this.visiteRepository.listeVisiteByAssurre(debut, fin, codeAdherent);
//        if (visite == null || visite.size() == 0 || visite.isEmpty() == true) {
//            return null;
//        }
//        return visite;
//    
//        } catch (Exception e) {
//            System.err.println("code  error: "+codeAdherent);
//           return null;
//        }
//        }
////consommation detailler pour une companies precis
//    @Override
//    public List<Bons_adherent_details> listePrestationsAssureByPeriodeDetails(String assure, String prestation, Date debut, Date fin) {
//         Date date1;
//        Date date2;
//        
//        List<Bons_adherent_details> listeAdherentPrestation = new ArrayList<>();
//        try {
//            int i=0;
//            date1 =new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2025-07-24 01:00:00");
//            date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2025-09-04 00:59:00");
//            String souscriteur = "P 2 M PHARMA";
//            
//            List<Dbx45tyAdherent> adherents = this.adherentrepositories.findBySouscripteur(souscriteur);
////            this.lpRepositories.listeBonsPrestationByAssureByJoint(date1, date2, "101_1017-2130000084").forEach(l->  System.out.println(l.getDate()));
////           this.lpRepositories.listeBonsPrestationByAssures("99_1017-2130000110").forEach(l->  System.out.println(l.getDate()));
////           this.lpRepositories.listeBonsPrestationByAssuress(date1,date2).forEach(l->  System.out.println(l.getPrestationId().getVisiteId().getCodeAdherent()));
//
//            listeAdherentPrestation = adherents.stream()
//                    .filter(s -> listeVisite(date1, date2, s.getCodeAdherent()) != null)
//                    .flatMap(s -> dtoMapper.lignePrestationFronSouscripteurBonsDetails(listeVisite(date1, date2, s.getCodeAdherent()), souscriteur, s.getCodeAdherent(), date1, date2).stream())
//                    .collect(Collectors.toList());
//        } catch (ParseException ex) {
//            Logger.getLogger(LignePrestationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//
//        }catch (Exception e) {
//            Logger.getLogger(LignePrestationServiceImpl.class.getName()).log(Level.SEVERE, null, e);
//
//        }
//
//        return listeAdherentPrestation;
//    
//    }
//       
// 
//}
