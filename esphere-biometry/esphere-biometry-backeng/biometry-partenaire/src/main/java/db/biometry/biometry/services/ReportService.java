/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dtos.Report;
import db.biometry.biometry.dtos.StringResult;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author USER01
 */
public interface ReportService {

    public StringResult createReportExamen(Report report);

    public ResponseEntity<byte[]> createReportConsultation(Report report);

    public StringResult createReportConsultationByDataSource(Report report);

    public ResponseEntity<byte[]> genereBonsConsultation();

    public ResponseEntity<byte[]> genereStatistiqueBons();

    public ResponseEntity<byte[]> genereStatistiqueBons_Souscripteur();
    public ResponseEntity<byte[]> genereStatistiqueBons_SouscripteurRegion2();
     public ResponseEntity<byte[]> statistiqueBons_SouscripteurR(db.biometry.biometry.entite.Report report);

}
