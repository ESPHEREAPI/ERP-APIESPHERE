/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository.poolTPV;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.pooltpv.ProductionPayload;

/**
 *
 * @author USER01
 */
public interface ProductionPayloadRepository extends JpaRepository<ProductionPayload, Long> {

    // Requête dérivée Spring Data : cherche par numéro de police
    //Optional<ProductionPayload> findByPoliceNumber(String policeNumber);

    // Requête dérivée : liste toutes les productions d'un client
    List<ProductionPayload> findByUserName(String userName);
    List<ProductionPayload>findByOfficeCode(String officeCode);
     List<ProductionPayload>findByReference(String reference);
    

    // Requête JPQL personnalisée pour Oracle
  //  @Query("SELECT p FROM ProductionPayload p WHERE p.startsAt >= :date")
   // List<ProductionPayload> findProductionsActiveAfter(@Param("date") LocalDate date);
    
}
