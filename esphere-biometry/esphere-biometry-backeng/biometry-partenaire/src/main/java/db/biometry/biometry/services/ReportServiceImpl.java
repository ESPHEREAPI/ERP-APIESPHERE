///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package db.biometry.biometry.services;
//
//import db.biometry.biometry.dtos.Bon_Consultation;
//import db.biometry.biometry.dtos.Report;
//import db.biometry.biometry.dtos.Static_Souscripteur;
//import db.biometry.biometry.dtos.StringResult;
//import db.biometry.biometry.dtos.TableBons;
//import db.biometry.biometry.entite.Dbx45tyAdherent;
//import db.biometry.biometry.entite.Dbx45tyConsultation;
//import db.biometry.biometry.mappers.BiometrieMapperImpl;
//import db.biometry.biometry.repositories.AdherentRepositories;
//import db.biometry.biometry.repositories.ConsultationRepositories;
//import db.biometry.biometry.repositories.ConsultationTableRepository;
//import db.biometry.biometry.repositories.PrestataireRepositories;
//import db.biometry.biometry.utils.Constant;
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//import javax.sql.DataSource;
//import lombok.AllArgsConstructor;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.JasperRunManager;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.util.JRLoader;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
///**
// *
// * @author USER01
// */
//@Service
//@AllArgsConstructor
//public class ReportServiceImpl implements ReportService {
//
//    private DataSource DataSource;
//    private ResourceLoader ResourceLoader;
//    private ConsultationRepositories consultationRepositories;
//    private ConsultationTableRepository consultationTableRepository;
//    private BiometrieMapperImpl mappers;
//    private AdherentRepositories adherentRepositories;
//    private PrestataireRepositories prestataireRepositories;
//    private LignePrestationService lignePrestationService;
//
//    @Override
//    public StringResult createReportExamen(Report report) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    @Override
//    public ResponseEntity<byte[]> createReportConsultation(Report report) {
//
//        try {
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(consultationRepositories.findAll());
//            Resource resources = ResourceLoader.getResource("classpath:/reports/consultation.jrxml");
//
//            JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(resources.getFile()));
//            HashMap<String, Object> map = new HashMap<>();
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
//            byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
//            HttpHeaders header = new HttpHeaders();
//            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.pdf");
//
//            return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_PDF).body(data);
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JRException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//
//    }
//
//    @Override
//    public StringResult createReportConsultationByDataSource(Report report) {
//        StringResult reportName = null;
//        try {
//            Resource resources = ResourceLoader.getResource("classpath:/db/biometry/biometry/reports/" + report.getName() + ".jasper");
//
//            InputStream reportStream = resources.getInputStream();
//            Map<String, Object> parameters = new HashMap<>();
//            reportName = new StringResult();
//            parameters.put("REPORT_FOLDER", ResourceLoader.getResource("classpath:/db/biometry/biometry/reports/" + report.getName() + ".jasper").getFile().getAbsolutePath());
////            parameters.put(JRParameter.REPORT_LOCALE, )
//            Connection con = this.DataSource.getConnection();
//            byte[] reportsByte = JasperRunManager.runReportToPdf(reportStream, parameters, con);
//            reportName.setName(report.getName() + "_" + System.currentTimeMillis() + ".pdf");
//            FileOutputStream fileOutputStream = new FileOutputStream(Constant.REPORT_RESULT_FOLDER_CONSULTATION + reportName.getName());
//            fileOutputStream.write(reportsByte);
//            fileOutputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return reportName;
//    }
//
//    @Override
//    public ResponseEntity<byte[]> genereBonsConsultation() {
//
//        try {
//            Date date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2024-01-01 00:00:00");
//
//            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2024-04-30 23:59:00");
////            String souscripteur = "SOCIETE IMMOBILIERE DU CAMEROUN";
//            String souscripteur = "POLYCLINIQUE DE POITIERS";
//            List<Dbx45tyConsultation> listeconsultations = consultationTableRepository.listeeBonsConsultation(date1, date2);
////             System.out.println("taille consultations-->"+listeconsultations.size());
////             listeconsultations.forEach(System.out::println);
//            List<Bon_Consultation> listeBons = listeconsultations.stream().filter(c -> controleTaux(c) != null)
//                    .map(c -> mappers.fromVisiteConsultation(c)).collect(Collectors.toList());
////             System.out.println("taille listeBons-->"+listeBons.size());
////            listeBons.forEach(System.out::println);
//
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeBons, false);
////            Resource resources = ResourceLoader.getResource("classpath:/reports/bon_consultations_3.jrxml");
////            Resource resources = ResourceLoader.getResource("reports\\bon_consultations_3.jrxml");
////            File file = new File("C:\\reports\\bon_consultations_3.jrxml");
//
////            JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(resources.getFile()));
////            JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(file));
//            String pathFile = "C:\\reports\\bon_consultations_3";
//            String reportPathjrxml = pathFile + ".jrxml";
//            String reportPathjasper = pathFile + ".jasper";
//            File f = new File(reportPathjasper);
//            if (!f.exists()) {
//                JasperCompileManager.compileReportToFile(reportPathjrxml, reportPathjasper);
//            }
//            FileInputStream fis;
//            BufferedInputStream bufferedInputStream;
//            fis = new FileInputStream(reportPathjasper);
//            bufferedInputStream = new BufferedInputStream(fis);
//
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream);
//            HashMap<String, Object> map = new HashMap<>();
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
//            byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
//            HttpHeaders header = new HttpHeaders();
//            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.pdf");
//
//            return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_PDF).body(data);
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JRException | IOException | ParseException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public Dbx45tyConsultation controleTaux(Dbx45tyConsultation c) {
////        String souscripteur = "SOCIETE IMMOBILIERE DU CAMEROUN";
//        String souscripteur = "POLYCLINIQUE DE POITIERS";
//
//        if (c.getTaux() != null && c.getTaux().intValue() != 0 && c.getMontantModif() != null && c.getMontantModif().intValue() != 0) {
//            String codeAdherent = c.getVisiteId().getCodeAdherent();
//            Dbx45tyAdherent adherent = adherentRepositories.findByCodeAdherent(codeAdherent);
//            if (adherent != null && souscripteur.equalsIgnoreCase(adherent.getSouscripteur())) {
//
//                return c;
//            }
//
//        }
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<byte[]> genereStatistiqueBons() {
//        //recuperation des differents prestataire ayant effectuer un bons de prise en charge (ordonnance,examen,consultation)   
//        try {
//            Date date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-01-01 00:00:00");
//
//            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2023-12-31 23:59:00");
//            List<String> listePrestatires = prestataireRepositories.listePrestataire(date1, date2);
//            System.out.println("liste des prestataires ");
//            listePrestatires.forEach(System.out::println);
//            List<TableBons> listeBonsByPrestatair = listePrestatires.stream()
//                    .map(bons -> mappers.fromBonsPrestataire(bons, date1, date2))
//                    .collect(Collectors.toList());
//            System.out.println("liste des bons ");
//            listeBonsByPrestatair.forEach(System.out::println);
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeBonsByPrestatair, false);
////            Resource resources = ResourceLoader.getResource("classpath:/reports/Bons2_1.jrxml");
//            String pathFile = "C:\\reports\\Bons2_1";
//            String reportPathjrxml = pathFile + ".jrxml";
//            String reportPathjasper = pathFile + ".jasper";
//            File f = new File(reportPathjasper);
//            if (!f.exists()) {
//                JasperCompileManager.compileReportToFile(reportPathjrxml, reportPathjasper);
//            }
//            FileInputStream fis;
//            BufferedInputStream bufferedInputStream;
//            fis = new FileInputStream(reportPathjasper);
//            bufferedInputStream = new BufferedInputStream(fis);
//
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream);
////            JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(resources.getFile()));
//
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("debut", new java.sql.Date(date1.getTime()));
//            map.put("fin", new java.sql.Date(date2.getTime()));
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
//            byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
//            HttpHeaders header = new HttpHeaders();
//            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.pdf");
//
//            return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_PDF).body(data);
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JRException | IOException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParseException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<byte[]> genereStatistiqueBons_Souscripteur() {
//        //recuperation des differents prestataire ayant effectuer un bons de prise en charge (ordonnance,examen,consultation)   
//        try {
//            Date date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2024-11-01 01:00:00");
//
//            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2024-11-07 00:59:00");
//            List<Static_Souscripteur> listesStatistic_souscripteur = lignePrestationService.staticSouscripteur(date1, date2);
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listesStatistic_souscripteur, false);
////            Resource resources = ResourceLoader.getResource("classpath:/reports/Bons2_1.jrxml");
//            String pathFile = "C:\\reports\\statistic_souscrip";
//            String reportPathjrxml = pathFile + ".jrxml";
//            String reportPathjasper = pathFile + ".jasper";
//            File f = new File(reportPathjasper);
//            if (!f.exists()) {
//                JasperCompileManager.compileReportToFile(reportPathjrxml, reportPathjasper);
//            }
//            FileInputStream fis;
//            BufferedInputStream bufferedInputStream;
//            fis = new FileInputStream(reportPathjasper);
//            bufferedInputStream = new BufferedInputStream(fis);
//
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream);
////            JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(resources.getFile()));
//
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("debut", new java.sql.Date(date1.getTime()));
//            map.put("fin", new java.sql.Date(date2.getTime()));
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
//            byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
//            HttpHeaders header = new HttpHeaders();
//            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.pdf");
//
//            return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_PDF).body(data);
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JRException | IOException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParseException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<byte[]> genereStatistiqueBons_SouscripteurRegion2() {
//        //recuperation des differents prestataire ayant effectuer un bons de prise en charge (ordonnance,examen,consultation)   
//        try {
//            Date date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2024-11-01 01:00:00");
//
//            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2024-11-07 00:59:00");
//            List<Static_Souscripteur> listesStatistic_souscripteur = lignePrestationService.staticSouscripteurRegion2(date1, date2);
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listesStatistic_souscripteur, false);
////            Resource resources = ResourceLoader.getResource("classpath:/reports/Bons2_1.jrxml");
//            String pathFile = "C:\\reports\\statistic_souscrip";
//            String reportPathjrxml = pathFile + ".jrxml";
//            String reportPathjasper = pathFile + ".jasper";
//            File f = new File(reportPathjasper);
//            if (!f.exists()) {
//                JasperCompileManager.compileReportToFile(reportPathjrxml, reportPathjasper);
//            }
//            FileInputStream fis;
//            BufferedInputStream bufferedInputStream;
//            fis = new FileInputStream(reportPathjasper);
//            bufferedInputStream = new BufferedInputStream(fis);
//
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream);
////            JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(resources.getFile()));
//
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("debut", new java.sql.Date(date1.getTime()));
//            map.put("fin", new java.sql.Date(date2.getTime()));
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
//            byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
//            HttpHeaders header = new HttpHeaders();
//            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.pdf");
//
//            return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_PDF).body(data);
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JRException | IOException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParseException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//
//    }
//
//    @Override
//    public ResponseEntity<byte[]> statistiqueBons_SouscripteurR(db.biometry.biometry.entite.Report report) {
//
//        //recuperation des differents prestataire ayant effectuer un bons de prise en charge (ordonnance,examen,consultation)   
//        try {
//            Date date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(""+report.getDate_debut());
//
//            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(""+report.getDate_fin());
//            List<Static_Souscripteur> listesStatistic_souscripteur = lignePrestationService.staticSouscripteur(date1, date2,report.getVille());
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listesStatistic_souscripteur, false);
////            Resource resources = ResourceLoader.getResource("classpath:/reports/Bons2_1.jrxml");
//            String pathFile = "C:\\reports\\statistic_souscrip";
//            String reportPathjrxml = pathFile + ".jrxml";
//            String reportPathjasper = pathFile + ".jasper";
//            File f = new File(reportPathjasper);
//            if (!f.exists()) {
//                JasperCompileManager.compileReportToFile(reportPathjrxml, reportPathjasper);
//            }
//            FileInputStream fis;
//            BufferedInputStream bufferedInputStream;
//            fis = new FileInputStream(reportPathjasper);
//            bufferedInputStream = new BufferedInputStream(fis);
//
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream);
////            JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(resources.getFile()));
//
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("debut", new java.sql.Date(date1.getTime()));
//            map.put("fin", new java.sql.Date(date2.getTime()));
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
//            byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
//            HttpHeaders header = new HttpHeaders();
//            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.pdf");
//
//            return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_PDF).body(data);
//        } catch (JRException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParseException ex) {
//            Logger.getLogger(ReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    }
