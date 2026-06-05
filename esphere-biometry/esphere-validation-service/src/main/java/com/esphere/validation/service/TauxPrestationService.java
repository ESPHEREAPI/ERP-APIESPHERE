/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.service;

import com.esphere.validation.dto.request.TauxPrestationDTO;
import com.esphere.validation.entity.TauxPrestation;
import com.esphere.validation.repository.TauxPrestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 *
 * @author USER01
 */
@Service @Transactional
public class TauxPrestationService {

    @Autowired 
    private TauxPrestationRepository repo;

    public List<TauxPrestationDTO> listerTous() {
        return repo.findAll().stream()
                .map(this::toDTO).toList();
    }

    public List<TauxPrestationDTO> parTypePrestation(
            String typeId) {
        return repo.findByTypePrestationId(typeId)
                .stream().map(this::toDTO)
                .toList();
    }

    public List<TauxPrestationDTO> parPolice(
            String police) {
        return repo.findByPolice(police)
                .stream().map(this::toDTO)
                .toList();
    }

    /** Trouve le taux exact pour police+groupe+type */
    public TauxPrestationDTO trouverTaux(
            String police, short groupe,
            String typeId) {
        return repo.findByPoliceAndGroupeAndTypePrestationId(
                police, groupe, typeId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException(
                        "Taux introuvable"));
    }

    public TauxPrestationDTO creer(TauxPrestationDTO dto) {
        TauxPrestation e = TauxPrestation.builder()
                .typePrestationId(dto.getTypePrestationId())
                .police(dto.getPolice())
                .groupe(dto.getGroupe())
                .taux(dto.getTaux())
                .plafond(dto.getPlafond()).build();
        return toDTO(repo.save(e));
    }

    private TauxPrestationDTO toDTO(TauxPrestation e) {
        return TauxPrestationDTO.builder()
                .id(e.getId())
                .typePrestationId(e.getTypePrestationId())
                .police(e.getPolice()).groupe(e.getGroupe())
                .taux(e.getTaux()).plafond(e.getPlafond()).build();
    }
    
}
