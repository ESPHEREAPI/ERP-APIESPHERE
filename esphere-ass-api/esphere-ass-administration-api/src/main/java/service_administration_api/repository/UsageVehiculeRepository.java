/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import service_administration_api.entite.UsageVehicule;
import service_administration_api.entite.ZoneCirculation;

/**
 *
 * @author USER01
 */
public interface UsageVehiculeRepository 
    extends JpaRepository<UsageVehicule, String> {

Optional<UsageVehicule>findByLibelle(String libelle);

}
