/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.entite.Dbx45tyVisite;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author USER01
 */
public interface VisiterRepository extends JpaRepository<Dbx45tyVisite, String>{
    @Query("SELECT v FROM Dbx45tyVisite v WHERE v.date BETWEEN :debut and :fin  and v.codeAdherent= :codeAdherent ")
     public List<Dbx45tyVisite> listeVisiteByAssurre(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2, @Param(value = "codeAdherent")  String codeAdherent );

   
}
