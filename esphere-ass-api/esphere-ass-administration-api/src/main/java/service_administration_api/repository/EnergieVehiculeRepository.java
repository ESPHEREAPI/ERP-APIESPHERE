/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import service_administration_api.entite.EnergieVehicule;
import service_administration_api.entite.GenreVehicule;
/**
 *
 * @author USER01
 */
public interface EnergieVehiculeRepository 
    extends JpaRepository<EnergieVehicule, String> {
Optional<EnergieVehicule>findByLibelle(String libelle);

}