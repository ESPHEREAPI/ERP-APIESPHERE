/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.ObjectReader
 *  com.zenithe.boost.sms.CustomProperties
 *  com.zenithe.boost.sms.dtos.ApiSms
 *  com.zenithe.boost.sms.dtos.ApiSmsRoot
 *  com.zenithe.boost.sms.dtos.ExpirationVues
 *  com.zenithe.boost.sms.dtos.SmsDto
 *  com.zenithe.boost.sms.dtos.SmsProduction
 *  com.zenithe.boost.sms.entites.Services
 *  com.zenithe.boost.sms.entites.Sms
 *  com.zenithe.boost.sms.mappers.BiometrieMapperImpl
 *  com.zenithe.boost.sms.repositories.ServiceRepository
 *  com.zenithe.boost.sms.repositories.SmsRepositories
 *  com.zenithe.boost.sms.utils.IdleDate
 *  com.zenithe.boost.sms.utils.WebServices
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.zenithe.boost.sms.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.zenithe.boost.sms.CustomProperties;
import com.zenithe.boost.sms.dtos.ApiSms;
import com.zenithe.boost.sms.dtos.ApiSmsRoot;
import com.zenithe.boost.sms.dtos.ExpirationVues;
import com.zenithe.boost.sms.dtos.SmsDto;
import com.zenithe.boost.sms.dtos.SmsProduction;
import com.zenithe.boost.sms.entites.Services;
import com.zenithe.boost.sms.entites.Sms;
import com.zenithe.boost.sms.mappers.BiometrieMapperImpl;
import com.zenithe.boost.sms.repositories.ServiceRepository;
import com.zenithe.boost.sms.repositories.SmsRepositories;
import com.zenithe.boost.sms.services.SmsService;
import com.zenithe.boost.sms.utils.IdleDate;
import com.zenithe.boost.sms.utils.WebServices;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SmsServiceImpl
implements SmsService {
    private ServiceRepository serviceRepository;
    private CustomProperties property;
    private WebServices urlService;
    private BiometrieMapperImpl mappers;
    private SmsRepositories smsRepositories;

    @Override
    public List<SmsDto> verifitSms() {
        List listeServiceActif = this.serviceRepository.findByStatut("1");
        ArrayList listesExpirationVues = new ArrayList();
        List<Object> listesZsbDtos = new ArrayList<SmsDto>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType((TypeReference)new TypeReference<List<ExpirationVues>>(){});
            listesZsbDtos = listeServiceActif.stream().flatMap(s -> this.chargeDataExpiration((Services)s).stream()).collect(Collectors.toList());
        }
        catch (Exception exception) {
            // empty catch block
        }
        return listesZsbDtos;
    }

    @Override
    public List<Sms> envoiSms() {
        ArrayList<Sms> listeSmsEnvoie = new ArrayList();
        List listeSmsEnAttente = this.smsRepositories.findByStatut("0");
        String urlSms = this.property.getHostname() + "user=" + this.property.getUser() + "&password=" + this.property.getPassword() + "&senderid=" + this.property.getSenderid() + "&";
        listeSmsEnvoie = listeSmsEnAttente.stream().map(s -> this.SendSms((Sms)s, urlSms)).collect(Collectors.toList());
        return listeSmsEnvoie;
    }

    public Sms SendSms(Sms s, String url) {
        ObjectReader objectReader = null;
        ApiSmsRoot apiSmsRoot = null;
        url = url + "mobiles=" + s.getTeleassu() + "&sms=" + this.replaceURL(s.getContenu());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectReader = objectMapper.reader().forType(ApiSmsRoot.class);
            apiSmsRoot = (ApiSmsRoot)objectReader.readValue(this.urlService.urlBiometries(url));
            ApiSms apiSms = (ApiSms)apiSmsRoot.getSms().get(0);
            if (apiSms != null) {
                s.setStatut(apiSms.getStatus().equalsIgnoreCase("success") ? "1" : "0");
                s.setDateEnvoi(new Date());
            }
            this.smsRepositories.save((Object)s);
        }
        catch (Exception e) {
            System.err.println(e.toString());
            System.err.println("" + url);
            try {
                objectReader.readValue(this.urlService.urlBiometries(url));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return s;
    }

    public String replaceURL(String l) {
        l = l.replaceAll(" ", "%20");
        l = l.replaceAll("\"", "%22");
        l = l.replaceAll("#", "%23");
        l = l.replaceAll("\\(", "%28");
        l = l.replaceAll("\\)", "%29");
        l = l.replaceAll("\\+", "%2B");
        l = l.replaceAll(",", "%2C");
        l = l.replaceAll("\\.", "%2E");
        l = l.replaceAll(":", "%3A");
        l = l.replaceAll(";", "%3B");
        l = l.replaceAll("<", "%3C");
        l = l.replaceAll(">", "%3E");
        l = l.replaceAll("@", "%40");
        l = l.replaceAll("\\[", "%5B");
        l = l.replaceAll("]", "%5D");
        l = l.replaceAll("'", "%60");
        l = l.replaceAll("\\{", "%7B");
        l = l.replaceAll("}", "%7D");
        l = l.replaceAll("~", "%7D");
        return l;
    }

    public List<SmsDto> chargeDataExpiration(Services s) {
        List listExpirations = new ArrayList();
        List<Object> listSmsDto = new ArrayList<SmsDto>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType((TypeReference)new TypeReference<List<ExpirationVues>>(){});
            if (s.getNomVue().equalsIgnoreCase("expirations")) {
                System.out.println("chemin  expiration :" + this.property.getExpirations());
                listExpirations = (List)objectReader.readValue(this.urlService.urlBiometries(this.property.getExpirations()));
                listSmsDto = listExpirations.stream().map(ex -> this.contruireSmsEnAttente((ExpirationVues)ex, s)).collect(Collectors.toList());
            } else if (s.getNomVue().equalsIgnoreCase("expirations30")) {
                System.out.println("chemin  expiration :" + this.property.getExpirations30());
                listExpirations = (List)objectReader.readValue(this.urlService.urlBiometries(this.property.getExpirations30()));
                listSmsDto = listExpirations.stream().map(ex -> this.contruireSmsEnAttente((ExpirationVues)ex, s)).collect(Collectors.toList());
            } else if (s.getNomVue().equalsIgnoreCase("expirations04")) {
                System.out.println("chemin  expiration :" + this.property.getExpirations04());
                listExpirations = (List)objectReader.readValue(this.urlService.urlBiometries(this.property.getExpirations04()));
                listSmsDto = listExpirations.stream().map(ex -> this.contruireSmsEnAttente((ExpirationVues)ex, s)).collect(Collectors.toList());
            } else if (s.getNomVue().equalsIgnoreCase("sms_production")) {
                System.out.println("chemin  expiration :" + this.property.getSms_production());
                listExpirations = (List)objectReader.readValue(this.urlService.urlBiometries(this.property.getSms_production()));
                listSmsDto = listExpirations.stream().map(ex -> this.contruireSmsEnAttente((ExpirationVues)ex, s)).collect(Collectors.toList());
            }
        }
        catch (Exception exception) {
        }
        listSmsDto.forEach(System.out::println);
        return listSmsDto;
    }

    public List<SmsDto> chargeDataSouscription(Services s) {
        List listExpirations = new ArrayList();
        List<Object> listSmsDto = new ArrayList<SmsDto>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType((TypeReference)new TypeReference<List<SmsProduction>>(){});
            if (s.getNomVue().equalsIgnoreCase("sms_production")) {
                System.out.println("chemin  expiration :" + this.property.getSms_production());
                listExpirations = (List)objectReader.readValue(this.urlService.urlBiometries(this.property.getSms_production()));
                listSmsDto = listExpirations.stream().filter(ex -> ex.getTELEASSU() != null && !"".equals(ex.getTELEASSU())).map(ex -> this.contruireSmsEnAttenteSMSProduction((SmsProduction)ex, s)).collect(Collectors.toList());
            }
        }
        catch (Exception exception) {
        }
        listSmsDto.forEach(System.out::println);
        return listSmsDto;
    }

    public SmsDto contruireSmsEnAttente(ExpirationVues ex, Services s) {
        SmsDto smsDto = null;
        try {
            smsDto = new SmsDto();
            smsDto.setCodeinte(ex.getCODEINTE().toString());
            if (ex.getLANGUE().equalsIgnoreCase("EN")) {
                smsDto.setContenu(s.getTemplateMessageEn());
            } else if (ex.getLANGUE().equalsIgnoreCase("FR")) {
                smsDto.setContenu(s.getTemplateMessageFr());
            }
            smsDto.setContenu(this.messages(smsDto.getContenu(), ex, s));
            smsDto.setDatecreation(new Date());
            smsDto.setDateeche(IdleDate.getYear((Date)ex.getDATEECHE()) + "/" + IdleDate.getMonth((Date)ex.getDATEECHE()) + "/" + IdleDate.getDayMonth((Date)ex.getDATEECHE()));
            smsDto.setNomassure(ex.getNOMASSURE());
            smsDto.setNumeaven("" + ex.getNUMEAVEN());
            smsDto.setNumepoli("" + ex.getNUMEPOLI());
            smsDto.setStatut("0");
            smsDto.setServices(s.getId());
            smsDto.setTeleassu(ex.getTELEASSU());
            smsDto.setNumeimma(ex.getNUMEIMMA());
        }
        catch (Exception e) {
            System.err.println("erreur ..expiration" + ex.getNOMASSURE());
            System.out.println("value :" + smsDto);
            System.err.println("" + e.toString());
        }
        return smsDto;
    }

    public SmsDto contruireSmsEnAttenteSMSProduction(SmsProduction ex, Services s) {
        SmsDto smsDto = null;
        try {
            smsDto = new SmsDto();
            smsDto.setCodeinte(ex.getCODEINTE().toString());
            if (ex.getLANGUE().equalsIgnoreCase("EN")) {
                smsDto.setContenu(s.getTemplateMessageEn());
            } else if (ex.getLANGUE().equalsIgnoreCase("FR")) {
                smsDto.setContenu(s.getTemplateMessageFr());
            }
            smsDto.setContenu(this.messagesSmsProduction(smsDto.getContenu(), ex, s));
            smsDto.setDatecreation(new Date());
            smsDto.setDateeche(IdleDate.getYear((Date)ex.getDATEECHE()) + "/" + IdleDate.getMonth((Date)ex.getDATEECHE()) + "/" + IdleDate.getDayMonth((Date)ex.getDATEECHE()));
            smsDto.setNomassure(ex.getNOMASSURE());
            smsDto.setNumeaven("" + ex.getNUMEAVEN());
            smsDto.setNumepoli("" + ex.getNUMEPOLI());
            smsDto.setStatut("0");
            smsDto.setServices(s.getId());
            smsDto.setTeleassu(ex.getTELEASSU());
        }
        catch (Exception e) {
            System.err.println("erreur ..expiration" + ex.getNOMASSURE());
            System.out.println("value :" + smsDto);
            System.err.println("" + e.toString());
        }
        return smsDto;
    }

    public String messages(String contenue, ExpirationVues ex, Services s) {
        if (s.getNomVue().equalsIgnoreCase("expirations04") || s.getNomVue().equalsIgnoreCase("expirations30")) {
            if (contenue.contains("{#nomassure#}")) {
                contenue = contenue.replace("{#nomassure#}", ex.getNOMASSURE());
            }
            if (contenue.contains("{#numeimma#}")) {
                contenue = contenue.replace("{#numeimma#}", ex.getNUMEIMMA());
            }
            if (contenue.contains("{#dateeche#}")) {
                contenue = contenue.replace("{#dateeche#}", IdleDate.getYear((Date)ex.getDATEECHE()) + "/" + IdleDate.getMonth((Date)ex.getDATEECHE()) + "/" + IdleDate.getDayMonth((Date)ex.getDATEECHE()));
            }
        } else if (s.getNomVue().equalsIgnoreCase("expirations")) {
            if (ex.getNUMEIMMA() == null) {
                if (contenue.contains("{#nomassure#}")) {
                    contenue = contenue.replace("{#nomassure#}", ex.getNOMASSURE());
                }
                if (contenue.contains("vehicule {#numeimma#}")) {
                    contenue = contenue.replace("de votre vehicule {#numeimma#}", "");
                }
                if (contenue.contains("{#dateeche#}")) {
                    contenue = contenue.replace("{#dateeche#}", IdleDate.getYear((Date)ex.getDATEECHE()) + "/" + IdleDate.getMonth((Date)ex.getDATEECHE()) + "/" + IdleDate.getDayMonth((Date)ex.getDATEECHE()));
                }
            } else if (ex.getNUMEIMMA() != null) {
                if (contenue.contains("{#nomassure#}")) {
                    contenue = contenue.replace("{#nomassure#}", ex.getNOMASSURE());
                }
                if (contenue.contains("{#numeimma#}")) {
                    contenue = contenue.replace("{#numeimma#}", ex.getNUMEIMMA());
                }
                if (contenue.contains("{#dateeche#}")) {
                    contenue = contenue.replace("{#dateeche#}", IdleDate.getYear((Date)ex.getDATEECHE()) + "/" + IdleDate.getMonth((Date)ex.getDATEECHE()) + "/" + IdleDate.getDayMonth((Date)ex.getDATEECHE()));
                }
            }
        }
        return contenue;
    }

    public String messagesSmsProduction(String contenue, SmsProduction ex, Services s) {
        if (s.getNomVue().equalsIgnoreCase("sms_production")) {
            if (contenue.contains("{#charger_cli#}")) {
                contenue = contenue.replace("{#charger_cli#}", ex.getCHARGER_CLI());
            }
            if (contenue.contains("{#charger_cli_tel#}")) {
                contenue = contenue.replace("{#charger_cli_tel#}", ex.getCHARGER_CLI_TEL());
            }
        }
        return contenue;
    }

    @Override
    public List<Sms> addSms(List<SmsDto> smsDtos) {
        List<Sms> listSms = smsDtos.stream().filter(s -> s.getTeleassu() != null && !"".equals(s.getTeleassu())).map(s -> this.mappers.formCustomerDTO(s)).collect(Collectors.toList());
        if (!listSms.isEmpty()) {
            this.smsRepositories.saveAll(listSms);
        }
        listSms.forEach(System.out::println);
        return listSms;
    }

    @Override
    public List<Sms> smsAttente() {
        return this.smsRepositories.findByStatut("0");
    }

    @Override
    public List<SmsDto> verifitSmsProductions() {
        List listeServiceActif = this.serviceRepository.findByStatut("1");
        ArrayList listesExpirationVues = new ArrayList();
        List<Object> listesZsbDtos = new ArrayList<SmsDto>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType((TypeReference)new TypeReference<List<SmsProduction>>(){});
            listesZsbDtos = listeServiceActif.stream().filter(s -> s.getNomVue().equalsIgnoreCase("sms_production")).flatMap(s -> this.chargeDataSouscription((Services)s).stream()).collect(Collectors.toList());
        }
        catch (Exception exception) {
        }
        listesZsbDtos.forEach(System.out::println);
        return listesZsbDtos;
    }

    public SmsServiceImpl(ServiceRepository serviceRepository, CustomProperties property, WebServices urlService, BiometrieMapperImpl mappers, SmsRepositories smsRepositories) {
        this.serviceRepository = serviceRepository;
        this.property = property;
        this.urlService = urlService;
        this.mappers = mappers;
        this.smsRepositories = smsRepositories;
    }
}
