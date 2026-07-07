/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.scheduling.annotation.EnableScheduling
 *  org.springframework.scheduling.annotation.Scheduled
 */
package com.zenithe.boost.sms;

import com.zenithe.boost.sms.dtos.SmsDto;
import com.zenithe.boost.sms.services.SmsService;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class BoostSmsApplication {
    private SmsService smsServices;

    public static void main(String[] args) {
        SpringApplication.run(BoostSmsApplication.class, (String[])args);
    }

    @Scheduled(cron="0 35 9 * * *")
    public void serviceAutoInsertVerifierSmS() throws InterruptedException {
        List<SmsDto> listeSms = this.smsServices.verifitSms();
        this.smsServices.addSms(listeSms);
    }

    @Scheduled(cron="0 50 9 * * *")
    public void serviceAutoInsertEnvoiSmS() throws InterruptedException {
        this.smsServices.envoiSms();
    }

    @Scheduled(cron="0 0 18 * * *")
    public void serviceAutoVerifierSmSProduction() throws InterruptedException {
        List<SmsDto> listeSms = this.smsServices.verifitSmsProductions();
        this.smsServices.addSms(listeSms);
    }

    @Scheduled(cron="0 10 18 * * *")
    public void serviceAutoEnvoiSmProductionS() throws InterruptedException {
        this.smsServices.envoiSms();
    }

    public BoostSmsApplication(SmsService smsServices) {
        this.smsServices = smsServices;
    }
}
