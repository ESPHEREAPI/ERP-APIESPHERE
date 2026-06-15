/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.Police;
import service_administration_api.entite.PolicePK;

/**
 *
 * @author USER01
 */
public interface PoliceRepository  extends JpaRepository<Police, PolicePK>{
    
    @Query("SELECT p FROM Police p WHERE p.policePK.codeinte= :codeinte and p.policePK.numepoli= :numepoli and p.avenmodi= :avenmodi")
    Optional<Police>findByPoliceAndCodeAgent(@Param("codeinte") int codeagent,@Param("numepoli") Long numepoli,@Param("avenmodi") Short avenmodi );
}
