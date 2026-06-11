/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.entite.Dbx45tyPrestataire;
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
public interface PrestataireRepositories  extends JpaRepository<Dbx45tyPrestataire, String>{
    
    @Query("SELECT DISTINCT (v.prestataireId)  FROM Dbx45tyVisite v  WHERE v.date BETWEEN :debut and :fin  ")
    public List<String> listePrestataire(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2);
    
}
