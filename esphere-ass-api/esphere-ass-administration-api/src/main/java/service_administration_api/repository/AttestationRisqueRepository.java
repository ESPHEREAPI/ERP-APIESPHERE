/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.AttestationRisque;
import service_administration_api.entite.AttestationRisquePK;

/**
 *
 * @author USER01
 */
public interface AttestationRisqueRepository  extends JpaRepository<AttestationRisque, AttestationRisquePK>{
    
    @Query("SELECT at FROM AttestationRisque at WHERE at.attestationRisquePK.codeinte= :codeinte and at.attestationRisquePK.numepoli= :numepoli and at.attestationRisquePK.numeaven= :numeaven ")
    Optional<AttestationRisque>findByPolice(@Param("codeinte") int codeinte,@Param("numepoli") Long numepoli,@Param("numeaven") Short numeaven);
    
}
