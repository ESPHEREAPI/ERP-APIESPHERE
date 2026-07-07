/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zenithe.boost.sms.dtos.ExpirationVues
 *  com.zenithe.boost.sms.dtos.SmsDto
 *  com.zenithe.boost.sms.dtos.UtilisateurDto
 *  com.zenithe.boost.sms.entites.Services
 *  com.zenithe.boost.sms.entites.Sms
 *  com.zenithe.boost.sms.entites.Utilisateur
 *  com.zenithe.boost.sms.repositories.ServiceRepository
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.zenithe.boost.sms.mappers;

import com.zenithe.boost.sms.dtos.ExpirationVues;
import com.zenithe.boost.sms.dtos.SmsDto;
import com.zenithe.boost.sms.dtos.UtilisateurDto;
import com.zenithe.boost.sms.entites.Services;
import com.zenithe.boost.sms.entites.Sms;
import com.zenithe.boost.sms.entites.Utilisateur;
import com.zenithe.boost.sms.repositories.ServiceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BiometrieMapperImpl {
    private ServiceRepository serviceRepository;

    public UtilisateurDto formUtilisateur(Utilisateur user) {
        UtilisateurDto userDto = new UtilisateurDto();
        BeanUtils.copyProperties((Object)user, (Object)userDto);
        return userDto;
    }

    public SmsDto fromExpirationVue(ExpirationVues e, Services s) {
        return null;
    }

    public Sms formCustomerDTO(SmsDto smsDto) {
        Sms sms = new Sms();
        BeanUtils.copyProperties(smsDto, sms);
        sms.setService(this.serviceRepository.findById(smsDto.getServices()).get());
        try {
            System.out.println("echeance:" + sms.getDateeche());
        }
        catch (Exception exception) {
            // empty catch block
        }
        return sms;
    }

    public BiometrieMapperImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }
}
