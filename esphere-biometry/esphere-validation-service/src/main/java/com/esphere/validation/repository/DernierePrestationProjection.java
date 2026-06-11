/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.esphere.validation.repository;

/**
 *
 * @author USER01
 */
public interface DernierePrestationProjection {
    String        getPolice();
    String        getCodeAdherent();
    String        getCodeAyantDroit();   // NULL pour l'adhérent principal
    Double        getTaux();
    String        getEtat();
    java.sql.Date getDate();
    Long          getLignePrestationId();
    Long          getPrestationId();
}
