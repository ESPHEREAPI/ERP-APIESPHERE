/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dtos.Bons_adherent_details;
import db.biometry.biometry.dtos.SouscripteurBons;
import db.biometry.biometry.dtos.Static_Souscripteur;
import db.biometry.biometry.dtos.Zen_Sinistre_BioDto;
import db.biometry.biometry.entite.Report;
import db.biometry.biometry.vues.LignePrestation;
import java.util.Date;
import java.util.List;

/**
 *
 * @author USER01
 */
public interface LignePrestationService {

    List<LignePrestation> listesBeneficiaireHavePrestation();

    List<LignePrestation> listesBeneficiaireHavePrestationOrdonance();

    List<Zen_Sinistre_BioDto> listesLignePrestationGroupByDate();

    List<LignePrestation> listesPrestationByCodePrestataire(String codePrestataire);

    List<Zen_Sinistre_BioDto> listesAllLignePrestations();

    List<Zen_Sinistre_BioDto> listesConsultation();
//     List<Integer> listePrestationByLignePrestation();

    List<Static_Souscripteur> staticSouscripteur(Date debut, Date fin);

    List<Static_Souscripteur> staticSouscripteurRegion2(Date debut, Date fin);

    List<Static_Souscripteur> staticSouscripteur(Date debut, Date fin, String ville);

    Static_Souscripteur staticSouscripteur(String souscripteur);
    Report createreport(db.biometry.biometry.dtos.Report r);
    List<SouscripteurBons> listePrestationsAssureByPeriode(String assure,String prestation,Date debut,Date fin);
    List<Bons_adherent_details> listePrestationsAssureByPeriodeDetails(String assure,String prestation,Date debut,Date fin);

}
