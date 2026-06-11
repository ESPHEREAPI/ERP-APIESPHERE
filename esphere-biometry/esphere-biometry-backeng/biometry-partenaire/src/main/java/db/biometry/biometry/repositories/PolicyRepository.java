/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

/**
 *
 * @author USER01
 */

import db.biometry.biometry.entite.Dbx45tyAdherent;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PolicyRepository extends JpaRepository<Dbx45tyAdherent, String> {
    Optional<Dbx45tyAdherent> findByPolice(String police);
    boolean existsByPolice(String police);
    @Query("SELECT  a FROM Dbx45tyAdherent a WHERE a.police = :police " +
           "AND :today BETWEEN a.effetPolice AND a.echeancePolice group by a.police")
    Optional<Dbx45tyAdherent> findByPoliceValide(
        @Param("police") String police,
        @Param("today") Date today
    );
    
      @Query("SELECT  a FROM Dbx45tyAdherent a WHERE a.police = :police " +
           " group by a.police")
    Optional<Dbx45tyAdherent> findByPoliceValide(
        @Param("police") String police
       
    );
}
