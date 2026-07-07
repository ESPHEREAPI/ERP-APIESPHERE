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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
        List<Services> listeServiceActif = this.serviceRepository.findByStatut("1");
        List<SmsDto> listesZsbDtos = new ArrayList<SmsDto>();
        try {
            listesZsbDtos = listeServiceActif.stream().flatMap(s -> this.chargeDataExpiration(s).stream()).collect(Collectors.toList());
        }
        catch (Exception exception) {
            // empty catch block
        }
        envoyerRapportTemoin("SMS en Attente", listesZsbDtos.size(), listesZsbDtos.size(), 0);
        return listesZsbDtos;
    }

    @Override
    public List<Sms> envoiSms() {
        List<Sms> listeSmsEnAttente = this.smsRepositories.findByStatut("0");
        String urlSms = this.property.getHostname() + "user=" + this.property.getUser() + "&password=" + this.property.getPassword() + "&senderid=" + this.property.getSenderid() + "&";
        List<Sms> listeSmsEnvoie = listeSmsEnAttente.stream().map(s -> this.SendSms(s, urlSms)).collect(Collectors.toList());
        long succes = listeSmsEnvoie.stream().filter(s -> "1".equals(s.getStatut())).count();
        long echec  = listeSmsEnvoie.stream().filter(s -> "0".equals(s.getStatut())).count();
        envoyerRapportTemoin("SMS Envoi", listeSmsEnvoie.size(), (int) succes, (int) echec);
        return listeSmsEnvoie;
    }

    public Sms SendSms(Sms s, String url) {
        url = url + "mobiles=" + s.getTeleassu() + "&sms=" + this.replaceURL(s.getContenu());
        try {
            String rawResponse = this.urlService.urlBiometrie(url);
            System.out.println("[SMS] " + s.getTeleassu() + " -> " + rawResponse);

            if (rawResponse != null && rawResponse.trim().startsWith("{")) {
                ObjectMapper objectMapper = new ObjectMapper();
                ApiSmsRoot apiSmsRoot = objectMapper.readValue(rawResponse, ApiSmsRoot.class);
                ApiSms apiSms = (ApiSms) apiSmsRoot.getSms().get(0);
                if (apiSms != null) {
                    s.setStatut(apiSms.getStatus().equalsIgnoreCase("success") ? "1" : "0");
                    s.setDateEnvoi(new Date());
                    s.setReponseEnvoiSms(apiSms.getStatus());
                }
            } else if (rawResponse != null && rawResponse.toLowerCase().contains("success")) {
                s.setStatut("1");
                s.setDateEnvoi(new Date());
                s.setReponseEnvoiSms(rawResponse.length() > 200 ? rawResponse.substring(0, 200) : rawResponse);
            } else {
                s.setStatut("0");
                String rep = rawResponse != null ? rawResponse : "null";
                s.setReponseEnvoiSms(rep.length() > 200 ? rep.substring(0, 200) : rep);
            }
            this.smsRepositories.save(s);
        }
        catch (Exception e) {
            System.err.println("[SMS] Erreur " + s.getTeleassu() + ": " + e.toString());
            s.setReponseEnvoiSms("ERREUR: " + e.getMessage());
            this.smsRepositories.save(s);
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
        List<ExpirationVues> listExpirations = new ArrayList<ExpirationVues>();
        List<SmsDto> listSmsDto = new ArrayList<SmsDto>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<ExpirationVues>>(){});
            if (s.getNomVue().equalsIgnoreCase("expirations")) {
                System.out.println("chemin  expiration :" + this.property.getExpirations());
                listExpirations = objectReader.readValue(this.urlService.urlBiometries(this.property.getExpirations()));
                listSmsDto = listExpirations.stream().map(ex -> this.contruireSmsEnAttente(ex, s)).collect(Collectors.toList());
            } else if (s.getNomVue().equalsIgnoreCase("expirations30")) {
                System.out.println("chemin  expiration :" + this.property.getExpirations30());
                listExpirations = objectReader.readValue(this.urlService.urlBiometries(this.property.getExpirations30()));
                listSmsDto = listExpirations.stream().map(ex -> this.contruireSmsEnAttente(ex, s)).collect(Collectors.toList());
            } else if (s.getNomVue().equalsIgnoreCase("expirations04")) {
                System.out.println("chemin  expiration :" + this.property.getExpirations04());
                listExpirations = objectReader.readValue(this.urlService.urlBiometries(this.property.getExpirations04()));
                listSmsDto = listExpirations.stream().map(ex -> this.contruireSmsEnAttente(ex, s)).collect(Collectors.toList());
            } else if (s.getNomVue().equalsIgnoreCase("sms_production")) {
                System.out.println("chemin  expiration :" + this.property.getSms_production());
                listExpirations = objectReader.readValue(this.urlService.urlBiometries(this.property.getSms_production()));
                listSmsDto = listExpirations.stream().map(ex -> this.contruireSmsEnAttente(ex, s)).collect(Collectors.toList());
            }
        }
        catch (Exception exception) {
        }
        listSmsDto.forEach(System.out::println);
        return listSmsDto;
    }

    public List<SmsDto> chargeDataSouscription(Services s) {
        List<SmsProduction> listExpirations = new ArrayList<SmsProduction>();
        List<SmsDto> listSmsDto = new ArrayList<SmsDto>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<SmsProduction>>(){});
            if (s.getNomVue().equalsIgnoreCase("sms_production")) {
                System.out.println("chemin  expiration :" + this.property.getSms_production());
                listExpirations = objectReader.readValue(this.urlService.urlBiometries(this.property.getSms_production()));
                listSmsDto = listExpirations.stream().filter(ex -> ex.getTELEASSU() != null && !"".equals(ex.getTELEASSU())).map(ex -> this.contruireSmsEnAttenteSMSProduction(ex, s)).collect(Collectors.toList());
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
        List<Services> listeServiceActif = this.serviceRepository.findByStatut("1");
        List<SmsDto> listesZsbDtos = new ArrayList<SmsDto>();
        try {
            listesZsbDtos = listeServiceActif.stream().filter(s -> s.getNomVue().equalsIgnoreCase("sms_production")).flatMap(s -> this.chargeDataSouscription(s).stream()).collect(Collectors.toList());
        }
        catch (Exception exception) {
        }
        listesZsbDtos.forEach(System.out::println);
        envoyerRapportTemoin("SMS Production", listesZsbDtos.size(), listesZsbDtos.size(), 0);
        return listesZsbDtos;
    }

    // =========================================================
    //  RAPPORT TEMOIN — envoye aux 2 numeros apres chaque operation
    // =========================================================
    private void envoyerRapportTemoin(String operation, int total, int succes, int echec) {
        try {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            String message = "[ESPHERE-SMS] Rapport " + operation
                    + " | Total: " + total
                    + " | Succes: " + succes
                    + " | Echec: " + echec
                    + " | Date: " + date;

            String urlBase = this.property.getHostname()
                    + "user=" + URLEncoder.encode(this.property.getUser(), "UTF-8")
                    + "&password=" + URLEncoder.encode(this.property.getPassword(), "UTF-8")
                    + "&senderid=" + URLEncoder.encode(this.property.getSenderid(), "UTF-8")
                    + "&sms=" + URLEncoder.encode(message, "UTF-8")
                    + "&mobiles=";

            if (this.property.getTemoin1() != null && !this.property.getTemoin1().isBlank()) {
                this.urlService.urlBiometrie(urlBase + URLEncoder.encode(this.property.getTemoin1(), "UTF-8"));
                System.out.println("[RAPPORT] Temoin1 notifie : " + this.property.getTemoin1());
            }
            if (this.property.getTemoin2() != null && !this.property.getTemoin2().isBlank()) {
                this.urlService.urlBiometrie(urlBase + URLEncoder.encode(this.property.getTemoin2(), "UTF-8"));
                System.out.println("[RAPPORT] Temoin2 notifie : " + this.property.getTemoin2());
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println("[RAPPORT] Erreur envoi rapport temoin : " + e.getMessage());
        }
    }

    public SmsServiceImpl(ServiceRepository serviceRepository, CustomProperties property, WebServices urlService, BiometrieMapperImpl mappers, SmsRepositories smsRepositories) {
        this.serviceRepository = serviceRepository;
        this.property = property;
        this.urlService = urlService;
        this.mappers = mappers;
        this.smsRepositories = smsRepositories;
    }
}
