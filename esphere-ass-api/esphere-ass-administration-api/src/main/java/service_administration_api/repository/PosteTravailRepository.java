/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.PosteTravail;

/**
 *
 * @author USER01
 */
public interface PosteTravailRepository extends JpaRepository< PosteTravail, String>{
    @Query("SELECT p FROM PosteTravail p WHERE p.nomUtil = :username")
    public PosteTravail posteTravailByUsername(@Param("username") String username);
    
}
