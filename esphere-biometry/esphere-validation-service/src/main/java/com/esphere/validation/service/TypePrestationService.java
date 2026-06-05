/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.service;

import com.esphere.validation.dto.request.TypePrestationDTO;
import com.esphere.validation.entity.TypePrestation;
import com.esphere.validation.repository.TypePrestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 *
 * @author USER01
 */
@Service @Transactional
public class TypePrestationService {

    @Autowired
    private TypePrestationRepository repo;

    public List<TypePrestationDTO> listerTous() {
        return repo.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TypePrestationDTO> listerAffiches() {
        return repo.findByAffiche(1).stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TypePrestationDTO> listerParCategorie(
            String categorie) {
        return repo.findByCategorieAndAffiche(categorie, 1)
                .stream().map(this::toDTO)
                .toList();
    }

    public TypePrestationDTO creer(
            TypePrestationDTO dto) {
        TypePrestation e = TypePrestation.builder()
                .id(dto.getId()).nom(dto.getNom())
                .affiche(dto.getAffiche())
                .categorie(dto.getCategorie()).build();
        return toDTO(repo.save(e));
    }

    private TypePrestationDTO toDTO(TypePrestation e) {
        return TypePrestationDTO.builder()
                .id(e.getId()).nom(e.getNom())
                .affiche(e.getAffiche())
                .categorie(e.getCategorie()).build();
    }
}

