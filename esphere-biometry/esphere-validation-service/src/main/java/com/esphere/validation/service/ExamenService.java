/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.service;

import com.esphere.validation.dto.request.ExamenDTO;
import com.esphere.validation.entity.Examen;
import com.esphere.validation.repository.ExamenRepository;
import com.esphere.validation.repository.LignePrestationRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 *
 * @author USER01
 */
@Service @Transactional
public class ExamenService {

    @Autowired 
    private ExamenRepository repo;
    @Autowired
    private LignePrestationRepository lignePrestationRepository;

    public List<ExamenDTO> listerTous() {
        int annee = LocalDate.now().getYear();
       
        return lignePrestationRepository.listeExamen(annee);
    }

  
    /** Même logique que médicament :
     *  cherche en BDD → crée si absent */
    public ExamenDTO rechercherOuCreer(String nom) {
        Optional<Examen> existant =
                repo.findByNomIgnoreCase(nom);

        if (existant.isPresent())
            return toDTO(existant.get(), false);

        Examen nouveau = Examen.builder()
                .nom(nom.toUpperCase())
                .code("AUTO")
                .cotation((short) 0)
                .prix(0.0)
                .statut("1")
                .supprime("-1").build();
        return toDTO(repo.save(nouveau), true);
       
    }

    private ExamenDTO toDTO(Examen e, boolean isNew) {
        return ExamenDTO.builder()
                .id(e.getId()).code(e.getCode())
                .nom(e.getNom()).cotation(e.getCotation())
                .prix(e.getPrix()).statut(e.getStatut())
                .supprime(e.getSupprime())
                .nouveau(isNew).build();
    }
}


