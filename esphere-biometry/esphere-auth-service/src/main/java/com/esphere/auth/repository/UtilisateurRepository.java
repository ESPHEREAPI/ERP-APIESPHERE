package com.esphere.auth.repository;

import com.esphere.auth.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository
        extends JpaRepository<Utilisateur, Integer> {

    Optional<Utilisateur> findByLogin(String login);

    // Recherche paginée par nom, prénom ou login (pour UserController)
    Page<Utilisateur> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrLoginContainingIgnoreCase(
            String nom, String prenom, String login, Pageable pageable);

    @Query("""
        SELECT u FROM Utilisateur u
        WHERE u.supprime = '-1'
        ORDER BY u.nom ASC
        """)
    Page<Utilisateur> findAllNonSupprimes(Pageable pageable);

    @Query("""
        SELECT u FROM Utilisateur u
        WHERE u.supprime = '-1'
          AND (LOWER(u.nom) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.prenom) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.login) LIKE LOWER(CONCAT('%', :search, '%')))
        ORDER BY u.nom ASC
        """)
    Page<Utilisateur> searchNonSupprimes(@Param("search") String search, Pageable pageable);

    Optional<Utilisateur> findByEmail(String email);

    @Query("""
        SELECT u FROM Utilisateur u
        WHERE u.login    = :login
          AND u.statut   = '1'
          AND u.supprime = '-1'
        """)
    Optional<Utilisateur> findActiveByLogin(
            @Param("login") String login);

    /**
     * Cherche un utilisateur actif par prestataireId. Utilisé lors de la
     * vérification OTP. Aligné sur findActiveByLogin : statut='1' et
     * supprime='-1'
     */
    /**
     * Retourne le premier utilisateur actif pour ce prestataire. Un prestataire
     * peut avoir plusieurs comptes (consultation/ordonnance/examen) → on prend
     * le premier actif trouvé.
     */
    @Query("""
    SELECT u FROM Utilisateur u
    JOIN u.employe e
    WHERE e.prestataireId = :prestataireId
      AND u.statut        = '1'
      AND u.supprime      = '-1'
      AND e.serialBiometrie is not null
    ORDER BY u.id ASC
    """)
    List<Utilisateur> findActiveByPrestataireId(
            @Param("prestataireId") String prestataireId);
}
