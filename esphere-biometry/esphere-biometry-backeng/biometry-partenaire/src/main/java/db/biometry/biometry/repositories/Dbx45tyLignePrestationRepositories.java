/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.dtos.PrestataireStatsDTO;
import db.biometry.biometry.entite.Dbx45tyLignePrestation;
import db.biometry.biometry.entite.Dbx45tyPrestation;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER01
 */
@Repository
public interface Dbx45tyLignePrestationRepositories extends JpaRepository<Dbx45tyLignePrestation, Integer> {

    @Query("SELECT  COUNT(p) FROM Dbx45tyLignePrestation p WHERE p.dateEncaisse BETWEEN :debut and :fin  and p.taux IS NOT NULL  and p.prestataireId.id= :prestataire and p.prestationId.naturePrestation= :naturePrestation")
    public Long nombreBonsbyPrestataire(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2, @Param(value = "prestataire") String prestataire, @Param(value = "naturePrestation") String naturePrestation);

    //listes les souscripteurs pour une periode precise
    @Query(" SELECT DISTINCT(ad.souscripteur)  FROM Dbx45tyLignePrestation l inner join Dbx45tyPrestation p  inner join Dbx45tyVisite v  inner join Dbx45tyAdherent ad  WHERE l.dateEncaisse BETWEEN :debut and :fin  and l.taux IS NOT NULL")
    public List<String> listesSouscripteurs(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2);

    // nombre de bons de priseen charge pour une periode precis
    @Query("SELECT  p FROM Dbx45tyLignePrestation p WHERE p.dateEncaisse BETWEEN :debut and :fin  and p.taux IS NOT NULL  and p.prestataireId.id= :prestataire and p.prestationId.naturePrestation= :naturePrestation")
    public List<Dbx45tyLignePrestation> listeBonsPrestation(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2, @Param(value = "prestataire") String prestataire, @Param(value = "naturePrestation") String naturePrestation);
    // nombre de bons de priseen charge pour une periode precis consernant un adherent

    @Query("SELECT  p FROM Dbx45tyLignePrestation p WHERE p.dateEncaisse BETWEEN :debut and :fin  and p.taux IS NOT NULL  and p.prestationId.visiteId.codeAdherent= :codeAdherent and p.prestationId.naturePrestation= :naturePrestation GROUP BY p.prestationId.id")
    public List<Dbx45tyLignePrestation> listeBonsByAdherent(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2, @Param(value = "codeAdherent") String codeAdherent, @Param(value = "naturePrestation") String naturePrestation);

    public List<Dbx45tyLignePrestation> findByPrestationId(Dbx45tyPrestation prestationId);
    // nombre de bons de priseen charge pour une periode precis concernant un assure

//    @Query("""
//    SELECT p 
//    FROM Dbx45tyLignePrestation p 
//    WHERE p.dateEncaisse BETWEEN :debut AND :fin
//      AND p.taux IS NOT NULL
//      AND p.prestationId.visiteId.codeAdherent LIKE CONCAT('%', :codeAdherent, '%')
//""")
//List<Dbx45tyLignePrestation> listeBonsPrestationByAssure(
//        @Param("debut") Date date1,
//        @Param("fin") Date date2,
//        @Param("codeAdherent") String codeAdherent
//);

    @Query("SELECT  p FROM Dbx45tyLignePrestation p WHERE  p.taux IS NOT NULL  and p.prestationId.visiteId.codeAdherent= :assure ")
    public List<Dbx45tyLignePrestation> listeBonsPrestationByAssures(@Param(value = "assure") String assure);

    @Query("SELECT  p FROM Dbx45tyLignePrestation p WHERE p.dateEncaisse BETWEEN :debut and :fin  and p.taux IS NOT NULL  ")
    public List<Dbx45tyLignePrestation> listeBonsPrestationByAssuress(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2);

    @Query("SELECT  p FROM Dbx45tyLignePrestation p  inner join Dbx45tyPrestation pr  inner join Dbx45tyVisite v  inner join Dbx45tyAdherent ad  WHERE p.dateEncaisse BETWEEN :debut and :fin  and p.taux IS NOT NULL  and ad.codeAdherent= :codeAdherent ")
    public List<Dbx45tyLignePrestation> listeBonsPrestationByAssureByJoint(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2, @Param(value = "codeAdherent") String codeAdherent);
     
//// public List<Dbx45tyLignePrestation> findByPrestationId(Dbx45tyPrestation prestationId);
    @Query("SELECT new db.biometry.biometry.dtos.PrestataireStatsDTO(" + "    l.prestataireId.id,\n" + "    MAX(l.date),\n" + "    DATEDIFF(CURRENT_DATE, MAX(l.date)),\n" + "    l.prestataireId.nom,\n"  + "    l.prestataireId.categorieId.nom,\n" +"    l.prestataireId.telephone,\n" + "  l.prestataireId.villeId.code\n" + ")\n" + "FROM Dbx45tyLignePrestation l\n"  + "GROUP BY l.prestataireId, l.prestataireId.nom, l.prestataireId.telephone, l.prestataireId.villeId.code\n" + "ORDER BY DATEDIFF(CURRENT_DATE, MAX(l.date)) ASC\n")
    List<PrestataireStatsDTO> listePrestataireOnlinePrestation();
   

}
