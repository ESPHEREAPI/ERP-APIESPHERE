/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository.poolTPV;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.pooltpv.Garantie_PoolTPV;

/**
 *
 * @author USER01
 */
public interface Garantie_PoolTPVRepository extends JpaRepository<Garantie_PoolTPV, Long>{
    @Query("SELECT COUNT(g) > 0 FROM Garantie_PoolTPV g WHERE g.code = :code")
    boolean existsByCode(@Param("code") String code);
}
