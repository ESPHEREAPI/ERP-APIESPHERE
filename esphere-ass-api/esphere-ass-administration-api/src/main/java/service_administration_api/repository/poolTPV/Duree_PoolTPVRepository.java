/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository.poolTPV;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.pooltpv.Duree_PoolTPV;

/**
 *
 * @author USER01
 */
public interface Duree_PoolTPVRepository extends JpaRepository<Duree_PoolTPV, Long> {

    //boolean existsByCode(String code);
    @Query("SELECT COUNT(d) > 0 FROM Duree_PoolTPV d WHERE d.code = :code")
    boolean existsByCode(@Param("code") String code);
}
