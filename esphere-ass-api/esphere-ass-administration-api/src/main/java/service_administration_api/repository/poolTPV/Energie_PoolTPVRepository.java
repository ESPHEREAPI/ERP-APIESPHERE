/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository.poolTPV;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.pooltpv.Energie_PoolTPV;

/**
 *
 * @author USER01
 */
public interface Energie_PoolTPVRepository  extends JpaRepository<Energie_PoolTPV, Long>{
    @Query("SELECT COUNT(e) > 0 FROM Energie_PoolTPV e WHERE e.code = :code")
    boolean existsByCode(@Param("code") String code);
}
