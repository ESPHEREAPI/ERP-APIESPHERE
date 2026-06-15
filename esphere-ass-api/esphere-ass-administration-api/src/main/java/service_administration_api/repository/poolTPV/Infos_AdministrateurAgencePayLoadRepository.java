/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository.poolTPV;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service_administration_api.entite.pooltpv.Infos_AdministrateurAgencePayLoad;

/**
 *
 * @author USER01
 */
@Repository
public interface Infos_AdministrateurAgencePayLoadRepository extends JpaRepository<Infos_AdministrateurAgencePayLoad, Long> {

    Optional<Infos_AdministrateurAgencePayLoad> findByUsername(String username);
    


    // Option 1 — Spring Data JPA (plus simple)
    Infos_AdministrateurAgencePayLoad findByLoginIgnoreCase(String login);

    // Option 2 — JPQL explicite
    @Query("SELECT i FROM Infos_AdministrateurAgencePayLoad i WHERE LOWER(i.login) = LOWER(:login)")
    Optional<Infos_AdministrateurAgencePayLoad> findByLogin(@Param("login") String login);
}

