/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.service;

import com.esphere.validation.dto.request.MedicamentDTO;
import com.esphere.validation.entity.Medicament;
import com.esphere.validation.repository.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 *
 * @author USER01
 */
@Service
@Transactional
public class MedicamentService {

    @Autowired
    private MedicamentRepository repository;

    public MedicamentDTO rechercherOuCreer(String nom) {

        // 1. Recherche insensible à la casse dans la BDD
        Optional<Medicament> existant =
            repository.findByNomIgnoreCase(nom);

        if (existant.isPresent()) {
            // Médicament trouvé → retourner tel quel
            return toDTO(existant.get(), false);
        }

        // 2. Introuvable → créer avec statut=1, supprime=-1
        Medicament nouveau = new Medicament();
        nouveau.setNom(nom.toUpperCase());
        nouveau.setStatut("1");
        nouveau.setSupprime("-1");
        nouveau.setPrix(0.0);
        nouveau.setCategorie("1");

        Medicament sauvegarde = repository.save(nouveau);
        return toDTO(sauvegarde, true);
    }

    public List<MedicamentDTO> listerTous() {
        return repository.findByStatutAndSupprime("1", "-1")
                .stream().map(m -> toDTO(m, false))
                .toList();
    }

    private MedicamentDTO toDTO(Medicament m, boolean isNew) {
        return new MedicamentDTO(
            m.getId(), m.getNom(), m.getPrix(),
            m.getCode(), m.getCategorie(), isNew
        );
    }  
}
