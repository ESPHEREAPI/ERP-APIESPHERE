package db.biometry.biometry.services;

import db.biometry.biometry.repositories.AdherentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service dédié aux écritures sur l'entité Adhérent.
 *
 * Séparé de DashboardAdherentService (readOnly=true) pour permettre
 * la mise à jour du plafond dans une transaction indépendante
 * (REQUIRES_NEW) sans casser la transaction de lecture principale.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdherentWriteService {

    private final AdherentRepository adherentRepository;

    /**
     * Sauvegarde le plafond récupéré depuis l'API externe dans la base locale.
     * Utilise REQUIRES_NEW pour s'exécuter dans sa propre transaction,
     * indépendamment de la transaction readOnly du dashboard.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePlafond(String codeAdherent, Double plafond) {
        log.info("[AdherentWrite] Mise à jour plafond {} → {} FCFA", codeAdherent, plafond);
        adherentRepository.updatePlafond(codeAdherent, plafond);
    }
}
