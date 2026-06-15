/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import service_administration_api.entite.DetailPosteTravail;
import service_administration_api.entite.DetailPosteTravailId;

/**
 *
 * @author USER01
 */
public interface DetailPosteTravailSpecificationRepository 
     extends JpaRepository<DetailPosteTravail, DetailPosteTravailId>, 
            JpaSpecificationExecutor<DetailPosteTravail>{
    
        
}
