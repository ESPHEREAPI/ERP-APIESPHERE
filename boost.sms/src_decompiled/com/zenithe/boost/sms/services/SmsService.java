/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zenithe.boost.sms.dtos.SmsDto
 *  com.zenithe.boost.sms.entites.Sms
 */
package com.zenithe.boost.sms.services;

import com.zenithe.boost.sms.dtos.SmsDto;
import com.zenithe.boost.sms.entites.Sms;
import java.util.List;

public interface SmsService {
    public List<SmsDto> verifitSms();

    public List<SmsDto> verifitSmsProductions();

    public List<Sms> addSms(List<SmsDto> var1);

    public List<Sms> envoiSms();

    public List<Sms> smsAttente();
}
