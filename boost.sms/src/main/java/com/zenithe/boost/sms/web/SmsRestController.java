/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zenithe.boost.sms.entites.Sms
 *  com.zenithe.boost.sms.services.SmsService
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.zenithe.boost.sms.web;

import com.zenithe.boost.sms.entites.Sms;
import com.zenithe.boost.sms.services.SmsService;
import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value={"*"})
public class SmsRestController
implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(SmsRestController.class);
    private SmsService smsServices;

    @GetMapping(value={"biometry/sms-attente"})
    List<Sms> verifierSms() {
        List listeSms = this.smsServices.verifitSms();
        return this.smsServices.addSms(listeSms);
    }

    @GetMapping(value={"biometry/sms-envoi"})
    List<Sms> envoiSms() {
        return this.smsServices.envoiSms();
    }

    @GetMapping(value={"biometry/sms-attente-envoi"})
    List<Sms> smsMiseEnAttente() {
        return this.smsServices.smsAttente();
    }

    @GetMapping(value={"biometry/sms-production-attente"})
    List<Sms> smsProductionMiseEnAttente() {
        List listeSms = this.smsServices.verifitSmsProductions();
        return this.smsServices.addSms(listeSms);
    }

    public SmsRestController(SmsService smsServices) {
        this.smsServices = smsServices;
    }
}
