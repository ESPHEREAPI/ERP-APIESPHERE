/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zenithe.boost.sms.entites.Sms
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.stereotype.Repository
 */
package com.zenithe.boost.sms.repositories;

import com.zenithe.boost.sms.entites.Sms;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepositories
extends JpaRepository<Sms, Integer> {
    public List<Sms> findByStatut(String var1);
}
