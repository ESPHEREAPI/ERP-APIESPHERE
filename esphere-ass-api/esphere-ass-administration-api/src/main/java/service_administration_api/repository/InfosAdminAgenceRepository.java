/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;

/**
 *
 * @author USER01
 */
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service_administration_api.entite.pooltpv.Infos_AdministrateurAgencePayLoad;

import java.util.Optional;



@Repository
public interface InfosAdminAgenceRepository  extends JpaRepository<Infos_AdministrateurAgencePayLoad, Long> {

  // Optional<Infos_AdministrateurAgencePayLoad> findByUsername(String username);
   // Optional<Infos_AdministrateurAgencePayLoad> findByLogin(String login);
    //boolean existsByUsername(String username);
   // boolean existsByLogin(String login);
    //boolean existsByCodeAgence(Integer codeAgence);
    
    @Query(
    value = "SELECT COUNT(*) FROM ZEN_INFOS_ADMIN_AGENCE WHERE username_asac = :username",
    nativeQuery = true
)
int countByUsernameNative(@Param("username") String username);

@Query(
    value = "SELECT COUNT(*) FROM ZEN_INFOS_ADMIN_AGENCE WHERE login = :login",
    nativeQuery = true
)
int countByLoginNative(@Param("login") String login);

@Query(
    value = "SELECT COUNT(*) FROM ZEN_INFOS_ADMIN_AGENCE WHERE code_agence = :codeAgence",
    nativeQuery = true
)
int countByCodeAgenceNative(@Param("codeAgence") Integer codeAgence);
  /**
     * ✅ Pagination manuelle compatible Oracle 11g via ROWNUM.
     *
     * Oracle 11g ne supporte pas FETCH FIRST N ROWS ONLY.
     * Spring Data avec nativeQuery=true + Pageable génère toujours un double
     * ORDER BY → ORA-00933. Solution : requête native avec ROWNUM + offset/limit
     * passés en paramètres, gérés manuellement dans le service.
     *
     * La technique ROWNUM Oracle :
     *   SELECT * FROM (
     *     SELECT t.*, ROWNUM rn FROM (
     *       SELECT ... ORDER BY id DESC   ← tri ici
     *     ) t WHERE ROWNUM <= :maxRow     ← borne haute
     *   ) WHERE rn > :minRow             ← borne basse (offset)
     */
    @Query(
        value =
            "SELECT * FROM (" +
            "  SELECT t.*, ROWNUM rn FROM (" +
            "    SELECT * FROM ZEN_INFOS_ADMIN_AGENCE " +
            "    WHERE (:search IS NULL OR :search = '' " +
            "        OR LOWER(libelle_agence) LIKE LOWER('%' || :search || '%') " +
            "        OR LOWER(username_asac)  LIKE LOWER('%' || :search || '%') " +
            "        OR LOWER(login)          LIKE LOWER('%' || :search || '%') " +
            "        OR LOWER(email)          LIKE LOWER('%' || :search || '%') " +
            "        OR LOWER(client_name)    LIKE LOWER('%' || :search || '%')) " +
            "    ORDER BY id DESC" +
            "  ) t WHERE ROWNUM <= :maxRow" +
            ") WHERE rn > :minRow",
        nativeQuery = true
    )
    List<Infos_AdministrateurAgencePayLoad> searchAllPaginated(
            @Param("search")  String search,
            @Param("minRow")  int minRow,
            @Param("maxRow")  int maxRow);

    @Query(
        value =
            "SELECT COUNT(*) FROM ZEN_INFOS_ADMIN_AGENCE " +
            "WHERE (:search IS NULL OR :search = '' " +
            "    OR LOWER(libelle_agence) LIKE LOWER('%' || :search || '%') " +
            "    OR LOWER(username_asac)  LIKE LOWER('%' || :search || '%') " +
            "    OR LOWER(login)          LIKE LOWER('%' || :search || '%') " +
            "    OR LOWER(email)          LIKE LOWER('%' || :search || '%') " +
            "    OR LOWER(client_name)    LIKE LOWER('%' || :search || '%'))",
        nativeQuery = true
    )
    long countSearch(@Param("search") String search);

    Optional<Infos_AdministrateurAgencePayLoad> findByUsername(String username);
}
