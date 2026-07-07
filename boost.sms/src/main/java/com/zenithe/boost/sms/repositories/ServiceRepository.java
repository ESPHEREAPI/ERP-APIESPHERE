/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zenithe.boost.sms.entites.Services
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.stereotype.Repository
 */
package com.zenithe.boost.sms.repositories;

import com.zenithe.boost.sms.entites.Services;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository
extends JpaRepository<Services, Integer> {
    public List<Services> findByStatut(String statut);
}
