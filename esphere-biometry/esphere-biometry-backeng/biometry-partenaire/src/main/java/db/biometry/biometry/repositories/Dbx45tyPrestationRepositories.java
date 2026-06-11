/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.entite.Dbx45tyPrestation;
import db.biometry.biometry.entite.Dbx45tyVisite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER01
 */
@Repository
public interface Dbx45tyPrestationRepositories extends JpaRepository<Dbx45tyPrestation, Integer>{
    
     public Dbx45tyPrestation findByVisiteId(Dbx45tyVisite visiteId);
    
     }
