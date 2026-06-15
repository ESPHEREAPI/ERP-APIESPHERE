/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import service_administration_api.entite.TypeAssure;
import service_administration_api.entite.TypeVehicule;

/**
 *
 * @author USER01
 */
public interface TypeAssureRepository 
    extends JpaRepository<TypeAssure, String> {
Optional<TypeAssure>findByLibelle(String libelle);
}