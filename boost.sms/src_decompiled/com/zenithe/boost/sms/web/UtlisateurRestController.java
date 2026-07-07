/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zenithe.boost.sms.dtos.UserLogin
 *  com.zenithe.boost.sms.dtos.UtilisateurDto
 *  com.zenithe.boost.sms.exceptions.UtilisateurException
 *  com.zenithe.boost.sms.services.UtilisataireServices
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RestController
 */
package com.zenithe.boost.sms.web;

import com.zenithe.boost.sms.dtos.UserLogin;
import com.zenithe.boost.sms.dtos.UtilisateurDto;
import com.zenithe.boost.sms.exceptions.UtilisateurException;
import com.zenithe.boost.sms.services.UtilisataireServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtlisateurRestController {
    private static final Logger log = LoggerFactory.getLogger(UtlisateurRestController.class);
    private UtilisataireServices utilisataireServices;

    @GetMapping(value={"/users/{id}"})
    public UtilisateurDto getUser(@PathVariable(name="id") int userId) {
        UtilisateurDto userDto = new UtilisateurDto();
        try {
            userDto = this.utilisataireServices.getUser(userId);
        }
        catch (UtilisateurException u) {
            userDto = new UtilisateurDto();
            userDto.setEcheck_connection(true);
            userDto.setMessageEcheck(u.getMessage());
        }
        return userDto;
    }

    @PostMapping(value={"user/login"})
    public UtilisateurDto connect(@RequestBody UserLogin userLogin) {
        UtilisateurDto user = this.utilisataireServices.findUserByLogin(userLogin);
        return user;
    }

    public UtlisateurRestController(UtilisataireServices utilisataireServices) {
        this.utilisataireServices = utilisataireServices;
    }
}
