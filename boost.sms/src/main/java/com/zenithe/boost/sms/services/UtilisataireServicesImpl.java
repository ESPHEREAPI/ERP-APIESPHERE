/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zenithe.boost.sms.dtos.UserLogin
 *  com.zenithe.boost.sms.dtos.UtilisateurDto
 *  com.zenithe.boost.sms.entites.Utilisateur
 *  com.zenithe.boost.sms.exceptions.UtilisateurException
 *  com.zenithe.boost.sms.mappers.BiometrieMapperImpl
 *  com.zenithe.boost.sms.repositories.UtilisateurRepository
 *  com.zenithe.boost.sms.utils.Crypto
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.zenithe.boost.sms.services;

import com.zenithe.boost.sms.dtos.UserLogin;
import com.zenithe.boost.sms.dtos.UtilisateurDto;
import com.zenithe.boost.sms.entites.Utilisateur;
import com.zenithe.boost.sms.exceptions.UtilisateurException;
import com.zenithe.boost.sms.mappers.BiometrieMapperImpl;
import com.zenithe.boost.sms.repositories.UtilisateurRepository;
import com.zenithe.boost.sms.services.UtilisataireServices;
import com.zenithe.boost.sms.utils.Crypto;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UtilisataireServicesImpl
implements UtilisataireServices {
    private UtilisateurRepository utilisateurRepository;
    private BiometrieMapperImpl mappers;

    @Override
    public UtilisateurDto findUserByLogin(UserLogin userLog) {
        UtilisateurDto userdto = new UtilisateurDto();
        Utilisateur user = this.utilisateurRepository.findByLogin(userLog.getUserName());
        if (user == null) {
            userdto.setEcheck_connection(true);
            userdto.setMessageEcheck("USER NOT EXISTS... PLEASE TRY AGAINST");
            return userdto;
        }
        if (Objects.equals(Crypto.loginBiometrie((String)userLog.getPassword()), user.getMotPasse()) == Boolean.FALSE) {
            userdto.setEcheck_connection(true);
            userdto.setMessageEcheck("LOGIN OR PASSWORD NOT CORRECT");
            return userdto;
        }
        if (Objects.equals(userLog.getPassword(), user.getMotPasse()) == Boolean.TRUE && !"1".equals(user.getStatut())) {
            userdto.setEcheck_connection(true);
            userdto.setMessageEcheck("USER IS NOT ACTIVED... CHECK YOUR ADMINISTRATOR");
            return userdto;
        }
        userdto = this.mappers.formUtilisateur(user);
        userdto.setProfil(user.getProfilId().getCode());
        return userdto;
    }

    @Override
    public UtilisateurDto getUser(int userId) throws UtilisateurException {
        Utilisateur user = this.utilisateurRepository.findById(userId).orElseThrow(() -> new UtilisateurException("USER NOT EXISTS... PLEASE TRY AGAINST"));
        UtilisateurDto userDto = this.mappers.formUtilisateur(user);
        return userDto;
    }

    public UtilisataireServicesImpl(UtilisateurRepository utilisateurRepository, BiometrieMapperImpl mappers) {
        this.utilisateurRepository = utilisateurRepository;
        this.mappers = mappers;
    }
}
