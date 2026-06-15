/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.service;

import java.time.LocalDate;
import java.util.List;
import service_administration_api.entite.pooltpv.*;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.*;

/**
 *
 * @author USER01
 */
public interface PoolTPVService {

    public String JwtLoginPoolTpv() throws Exception;

    public List<Duree_PoolTPV> listeDurees(String duree)throws Exception;

    public List<Genre_PoolTPV> listGenres(String genre) throws Exception ;

    public List<Categorie_PoolTPV> listCategorie(String categorie)throws Exception ;

    public List<Garantie_PoolTPV> listGaranties(String garantie) throws Exception ;

    public List<Civilite_PoolTPV> listCivilite(String civilite) throws Exception ;

    public List<Energie_PoolTPV> listEnergie_PoolTPVs(String energie)throws Exception ;

    public Police_PoolTPV findPoliceByApiPoolTPV(String code_demandeur, LocalDate date_debut, LocalDate date_fin)throws Exception ;

    public ReferenceAutoPageDto reference_vehicule(String reference_vehicule)throws Exception ;

    public List<Risque_PoolTPV> listRisqueePooLTPV(String code_demandeur, LocalDate date_debut, LocalDate date_fin)throws Exception ;

    public List<Quittance_PoolTPV> listEncaissementPooLTPV(String code_demandeur, LocalDate date_debut, LocalDate date_fin) throws Exception;

}
