package com.esphere.auth.repository;

import com.esphere.auth.entity.Menu;
import com.esphere.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    /**
     * Statut  :  1 = actif      | -1 = inactif
     * Supprime: -1 = non supprimé|  1 = supprimé
     */
    @Query("""
        SELECT p FROM Permission p
        JOIN FETCH p.menu m
        WHERE p.profil.id = :profilId
        AND m.statut = '1'
        AND m.supprime = '-1'
        ORDER BY m.numeroOrdre ASC
    """)
    List<Permission> findMenusByProfilId(@Param("profilId") Integer profilId);
    
    @Query("""
    SELECT m FROM Menu m
    WHERE m.statut = '1'
    AND m.supprime = '-1'
    ORDER BY m.numeroOrdre ASC
""")
List<Menu> findAllActiveMenus();
}