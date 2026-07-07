/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zenithe.boost.sms.entites.Utilisateur
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 */
package com.zenithe.boost.sms.repositories;

import com.zenithe.boost.sms.entites.Utilisateur;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UtilisateurRepository
extends JpaRepository<Utilisateur, Integer> {
    public Utilisateur findByLogin(String var1);

    @Query(value="SELECT u FROM Utilisateur u WHERE u.nom  like :kw or u.login like :kw")
    public List<Utilisateur> searchUser(@Param(value="kw") String var1);
}
