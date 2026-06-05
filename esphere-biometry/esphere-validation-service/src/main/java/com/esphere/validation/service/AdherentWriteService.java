/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.service;

import com.esphere.validation.repository.AdherentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER01
 */
// ── AdherentWriteService.java ──────────────────────────────────────────
@Service
@RequiredArgsConstructor
public class AdherentWriteService {

    private final AdherentRepository adherentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // ← clé du fix
    public void updatePlafond(String codeAdherent, Double plafond) {
        adherentRepository.updatePlafond(codeAdherent, plafond);
    }
}