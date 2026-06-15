/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.service;

/**
 *
 * @author USER01
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service_administration_api.DTO.InfosAdminAgenceDTO;
import service_administration_api.entite.pooltpv.Infos_AdministrateurAgencePayLoad;
import service_administration_api.exception.DuplicateResourceException;
import service_administration_api.repository.InfosAdminAgenceRepository;

import java.util.List;
import service_administration_api.exception.UserNotFoundException;

@Service
@Transactional
public class InfosAdminAgenceService {

    private final InfosAdminAgenceRepository repository;

    public InfosAdminAgenceService(InfosAdminAgenceRepository repository) {
        this.repository = repository;
    }

    // ── CREATE ───────────────────────────────────────────────────────────────

    public InfosAdminAgenceDTO create(InfosAdminAgenceDTO dto) {
        if (repository.countByUsernameNative(dto.getUsername())>0)
            throw new DuplicateResourceException(
                    "Le username '" + dto.getUsername() + "' est déjà utilisé");
        if (repository.countByLoginNative(dto.getLogin())>0)
            throw new DuplicateResourceException(
                    "Le login '" + dto.getLogin() + "' est déjà utilisé");
        return toDTO(repository.save(toEntity(dto)));
    }

    // ── READ ─────────────────────────────────────────────────────────────────

    /**
     * ✅ Pagination manuelle avec ROWNUM Oracle 11g.
     *
     * Spring Data + nativeQuery=true injecte son propre ORDER BY dans le Pageable
     * même quand on ne passe pas de Sort → double ORDER BY → ORA-00933.
     *
     * Solution : on calcule minRow/maxRow manuellement et on les passe
     * en paramètres scalaires à la requête native ROWNUM.
     *
     *   page=0, size=10 → minRow=0,  maxRow=10
     *   page=1, size=10 → minRow=10, maxRow=20
     */
    @Transactional(readOnly = true)
    public Page<InfosAdminAgenceDTO> findAll(String search, int page, int size,
                                             String sortBy, String direction) {
        String s = (search == null) ? "" : search;
        int minRow = page * size;
        int maxRow = minRow + size;

        List<InfosAdminAgenceDTO> content = repository
                .searchAllPaginated(s, minRow, maxRow)
                .stream()
                .map(this::toDTO)
                .toList();

        long total = repository.countSearch(s);

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

    @Transactional(readOnly = true)
    public InfosAdminAgenceDTO findById(Long id) {
        return toDTO(getOrThrow(id));
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    public InfosAdminAgenceDTO update(Long id, InfosAdminAgenceDTO dto) {
        Infos_AdministrateurAgencePayLoad existing = getOrThrow(id);

        if (!existing.getUsername().equals(dto.getUsername())
                && repository.countByUsernameNative(dto.getUsername())>0)
            throw new DuplicateResourceException(
                    "Le username '" + dto.getUsername() + "' est déjà utilisé");

        if (!existing.getLogin().equals(dto.getLogin())
                && repository.countByLoginNative(dto.getLogin())>0)
            throw new DuplicateResourceException(
                    "Le login '" + dto.getLogin() + "' est déjà utilisé");

        existing.setCodeAgence(dto.getCodeAgence());
        existing.setLibelleAgence(dto.getLibelleAgence());
        existing.setEmail(dto.getEmail());
        existing.setLogin(dto.getLogin());
        existing.setClientName(dto.getClientName());
        existing.setExpiresAt(dto.getExpiresAt());
        existing.setOffice_code(dto.getOfficeCode());
        existing.setUsername(dto.getUsername());
        existing.setProfilAgent(dto.getProfilAgent() != null ? dto.getProfilAgent() : "PRODUCTEUR");
        existing.setCanEdit(dto.isCanEdit());

        return toDTO(repository.save(existing));
    }

    // ── BY USERNAME ──────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public InfosAdminAgenceDTO findByUsername(String username) {
        return repository.findByUsername(username)
                .map(this::toDTO)
                .orElseThrow(() -> new UserNotFoundException(
                        "Aucun agent trouvé pour le username : " + username));
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────

    private Infos_AdministrateurAgencePayLoad getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "Administrateur agence introuvable avec l'id : " + id));
    }

    private Infos_AdministrateurAgencePayLoad toEntity(InfosAdminAgenceDTO dto) {
        Infos_AdministrateurAgencePayLoad e = new Infos_AdministrateurAgencePayLoad();
        e.setCodeAgence(dto.getCodeAgence());
        e.setLibelleAgence(dto.getLibelleAgence());
        e.setEmail(dto.getEmail());
        e.setLogin(dto.getLogin());
        e.setClientName(dto.getClientName());
        e.setExpiresAt(dto.getExpiresAt());
        e.setOffice_code(dto.getOfficeCode());
        e.setUsername(dto.getUsername());
        e.setProfilAgent(dto.getProfilAgent() != null ? dto.getProfilAgent() : "PRODUCTEUR");
        e.setCanEdit(dto.isCanEdit());
        return e;
    }

    private InfosAdminAgenceDTO toDTO(Infos_AdministrateurAgencePayLoad e) {
        InfosAdminAgenceDTO dto = new InfosAdminAgenceDTO();
        dto.setId(e.getId());
        dto.setCodeAgence(e.getCodeAgence());
        dto.setLibelleAgence(e.getLibelleAgence());
        dto.setEmail(e.getEmail());
        dto.setLogin(e.getLogin());
        dto.setClientName(e.getClientName());
        dto.setExpiresAt(e.getExpiresAt());
        dto.setOfficeCode(e.getOffice_code());
        dto.setUsername(e.getUsername());
        dto.setProfilAgent(e.getProfilAgent());
        dto.setCanEdit(e.isCanEdit());
        return dto;
    }


}
