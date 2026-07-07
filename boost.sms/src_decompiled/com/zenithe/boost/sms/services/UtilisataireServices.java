/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zenithe.boost.sms.dtos.UserLogin
 *  com.zenithe.boost.sms.dtos.UtilisateurDto
 *  com.zenithe.boost.sms.exceptions.UtilisateurException
 */
package com.zenithe.boost.sms.services;

import com.zenithe.boost.sms.dtos.UserLogin;
import com.zenithe.boost.sms.dtos.UtilisateurDto;
import com.zenithe.boost.sms.exceptions.UtilisateurException;

public interface UtilisataireServices {
    public UtilisateurDto findUserByLogin(UserLogin var1);

    public UtilisateurDto getUser(int var1) throws UtilisateurException;
}
