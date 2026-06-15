/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository.poolTPV;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.pooltpv.Civilite_PoolTPV;

/**
 *
 * @author USER01
 */
public interface Civilite_PoolTPVRepository extends JpaRepository<Civilite_PoolTPV, Long>{
    @Query("SELECT COUNT(ci) > 0 FROM Civilite_PoolTPV ci WHERE ci.code = :code")
    boolean existsByCode(@Param("code") String code);
    
}
