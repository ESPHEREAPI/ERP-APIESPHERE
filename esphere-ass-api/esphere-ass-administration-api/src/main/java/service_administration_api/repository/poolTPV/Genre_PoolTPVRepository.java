/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository.poolTPV;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.pooltpv.Genre_PoolTPV;

/**
 *
 * @author USER01
 */
public interface Genre_PoolTPVRepository extends JpaRepository<Genre_PoolTPV, Long>{
     @Query("SELECT COUNT(g) > 0 FROM Genre_PoolTPV g WHERE g.code = :code")
    boolean existsByCode(@Param("code") String code);
}
