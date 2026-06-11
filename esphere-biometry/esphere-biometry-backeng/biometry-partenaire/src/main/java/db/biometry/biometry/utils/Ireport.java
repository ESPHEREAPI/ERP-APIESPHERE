/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.utils;

import db.biometry.biometry.enums.TypePrestations;
import jakarta.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
//import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author USER01
 */
public class Ireport implements Serializable {

    private static JRBeanCollectionDataSource beanCollectionDataSource;
    private static JasperPrint jasperPrint;
    private static JasperReport jasperReport;
    private static String path_consultation = "C:\\rapport\\consultation";
    private static String path_examen = "C:\\rapport\\examen";
    private static String path_ordonnance = "C:\\rapport\\ordonnance";
     private static final int DEFAULT_BUFFER_SIZE = 10240; // 10 k

    public static void printConsultation(String fileName, Collection<?> data, Map<String, Object> parameters,TypePrestations type) throws IOException, JRException {
        ServletOutputStream servletOutputStream = null;

        try {
         
            beanCollectionDataSource = new JRBeanCollectionDataSource(data);

//            // get report location
//         Resource
//
//            String reportPathjasper = fileName + ".jasper";

//            String filePath = reportLocation + "/Report/bundle/i18nReport_" + myLoc + ".properties";
//            FileInputStream fis1 = new FileInputStream(filePath);
//            
//            ResourceBundle bundle = new PropertyResourceBundle(fis1);
//            parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, bundle);
//            parameters.put(JRParameter.REPORT_LOCALE, myLoc);
//            jasperPrint = JasperFillManager.fillReport(reportPathjasper, parameters, beanCollectionDataSource);
            switch (type) {
                case consultation:
                    JasperExportManager.exportReportToPdfFile(jasperPrint, path_consultation + "\\consultation.pdf");
                    break;
//            }
                case examen:
                    JasperExportManager.exportReportToPdfFile(jasperPrint, path_examen + "\\exemen.pdf");
                    break;
                default:
                    JasperExportManager.exportReportToPdfFile(jasperPrint, path_ordonnance + "\\ordonance.pdf");
                    break;
            }
        }finally{
            System.out.println("fin d execution....");
        }
                

    }
    
//      public static void downloadFile(File file) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
//        /* Récupère le type du fichier */
//        String type = facesContext.getExternalContext().getMimeType(file.getName());
//
//        /* Si le type de fichier est inconnu, alors on initialise un type par défaut */
//        if (type == null) {
//            type = "application/octet-stream";
//        }
//        response.setBufferSize(DEFAULT_BUFFER_SIZE);
////
//        response.setContentType(type);
//        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
//        response.setContentLength((int) file.length());
//        FileInputStream input = null;
//        try {
//            int i = 0;
//            input = new FileInputStream(file);
//            byte[] buffer = new byte[1024];
//            while ((i = input.read(buffer)) != -1) {
//                response.getOutputStream().write(buffer);
//                response.getOutputStream().flush();
//            }
//            facesContext.responseComplete();
//            facesContext.renderResponse();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (input != null) {
//                    input.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//  

//    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
//
//        //load file and compile it
//        File file = ResourceUtils.getFile("classpath:employees.jrxml");
//        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("createdBy", "Java Techie");
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//        if (reportFormat.equalsIgnoreCase("html")) {
//            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\employees.html");
//        }
//        if (reportFormat.equalsIgnoreCase("pdf")) {
//            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\employees.pdf");
//        }
//
//        return "report generated in path : " + path;
//    }
//
}
