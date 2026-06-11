/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.mappers;

import db.biometry.biometry.ApplicationPropertiesConfiguration;
import db.biometry.biometry.dtos.Bon_Consultation;
import db.biometry.biometry.dtos.UserDTO;
import db.biometry.biometry.dtos.UserSessionDTO;
import db.biometry.biometry.dtos.UtilisateurDto;
import db.biometry.biometry.dtos.Zen_Sinistre_BioDto;
import db.biometry.biometry.entite.Dbx45tyAdherent;
import db.biometry.biometry.entite.Dbx45tyAyantDroit;
import db.biometry.biometry.entite.Dbx45tyConsultation;
import db.biometry.biometry.entite.Dbx45tyPrestataire;
import db.biometry.biometry.entite.Dbx45tyVisite;
import db.biometry.biometry.entite.RolePermissions;
import db.biometry.biometry.entite.Subscribers;
import db.biometry.biometry.entite.Utilisateur;
import db.biometry.biometry.repositories.AdherentRepository;
import db.biometry.biometry.repositories.AyantDroitRepository;
import db.biometry.biometry.repositories.ConsultationTableRepository;

import db.biometry.biometry.repositories.Dbx45tyLignePrestationRepositories;
import db.biometry.biometry.repositories.Dbx45tyPrestationRepositories;
import db.biometry.biometry.repositories.LignePrestationRepository;
import db.biometry.biometry.repositories.PrestataireRepositories;
import db.biometry.biometry.repositories.RegionRepositories;
import db.biometry.biometry.repositories.RolePermissionsRepositorie;
import db.biometry.biometry.repositories.VisiterRepository;
import db.biometry.biometry.utils.IdleDate;
import db.biometry.biometry.utils.RechercheFonction;
import db.biometry.biometry.vues.Consultation;
import db.biometry.biometry.vues.LignePrestation;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import db.biometry.biometry.utils.JwtExpiration;

/**
 *
 * @author JIATOU FRANCK
 */
@Service
@Data
@AllArgsConstructor
public class BiometrieMapperImpl implements Serializable{

    private LignePrestationRepository lignePrestationRepositories;
    private PrestataireRepositories prestataireRepositories;
    private VisiterRepository visiterRepository;
    private ConsultationTableRepository consultationTableRepository;
    private AdherentRepository adherentRepositories;
    private AyantDroitRepository ayantDroitRepositories;
    Dbx45tyLignePrestationRepositories dbx45tyLignePrestationRepositories;
    private AdherentRepository adherentRepositories1;
    private RegionRepositories regionRepositories;
    private Dbx45tyPrestationRepositories prestationRepositories;
    private RolePermissionsRepositorie rolePermissionsRepositorie;
    private ApplicationPropertiesConfiguration properties;

    public UserDTO formUtilisateur(Utilisateur user) {
        UserDTO userDto = new UserDTO();
       // BeanUtils.copyProperties(user, userDto);
       userDto.setUserName(user.getLogin());
       userDto.setEmail(user.getEmail());
       userDto.setFirstName(user.getPrenom());
       userDto.setLastname(user.getNom());
       userDto.setTel(user.getTelephone());
       userDto.setId(user.getId().longValue());
     
        return userDto;
    }
    
     public UserDTO formUtilisateur(Subscribers user) {
        UserDTO userDto = new UserDTO();
       userDto.setUserName(user.getPolicyNumber());
       userDto.setEmail(user.getEmail());
       userDto.setFirstName(user.getUsername());
       userDto.setTel(user.getPhoneNumber());
       userDto.setId(user.getId().longValue());

       // Résoudre le nom souscripteur depuis dbx45ty_adherent (même valeur que le dropdown DII)
       // pour que codeSouscripteur soit cohérent entre les deux profils
       String nomSouscripteur = adherentRepositories.findSouscripteurByPolice(user.getPolicyNumber());
       userDto.setLastname(nomSouscripteur != null && !nomSouscripteur.isBlank()
               ? nomSouscripteur
               : user.getFullName());

        return userDto;
    }
    
     public UserSessionDTO mapUserSessionDTOByuserDTO(UserDTO userDTO) {
        UserSessionDTO userSessionDTO = new UserSessionDTO();
        userSessionDTO.setUsersDTO(userDTO);
       userSessionDTO.setExpiresAt(
    JwtExpiration.expiresAt(properties.getJwt().getExpirationMs())
);
         List<String> permission = Optional
                .ofNullable(rolePermissionsRepositorie.listePermissionsByRoles(userDTO.getRoleid()))
                .orElse(Collections.emptyList())
                .stream()
                .map(per -> per.getName())
                .collect(Collectors.toList());
        userSessionDTO.setPermissions(permission);
        String profilCode = userDTO.getProfilType() != null ? userDTO.getProfilType().name() : null;
        userSessionDTO.setToken(JwtExpiration.generateJwtToken(userSessionDTO.getUsersDTO().getUserName(), userSessionDTO.getExpiresAt(), profilCode));

        return userSessionDTO;
    }

//    public PrestationDto formObjetlignePrestation(Object lignePrestation) {
//        PrestationDto prestationDto = new PrestationDto();
//        prestationDto.setCodeAssurePrincipal(lignePrestation.);
//        BeanUtils.copyProperties(prestationDto, lignePrestation);
//        return prestationDto;
//    }
//
//    public List<PrestationDto> formObjetlignePrestations(Object lignePrestation) {
//        List<Object> listeObjects = new ArrayList<>(Arrays.asList(lignePrestation));
//        List<PrestationDto> listePdtos = new ArrayList<>();
//        PrestationDto prestationDto = new PrestationDto();
//        prestationDto.setCodeAssurePrincipal(lignePrestation.);
//        Object t[] = (Object[]) listeObjects.get(0);
//        for (int i = 0; i < t.length; i++) {
//
//        }
//
//        return listePdtos;
//    }

    public Zen_Sinistre_BioDto ZenSinistreBiofromLignePrestation(LignePrestation lp) {
        Dbx45tyPrestataire p = prestataireRepositories.findById(lp.getCodePrestataire()).get();
        Zen_Sinistre_BioDto zen_Sinistre_BioDto = new Zen_Sinistre_BioDto();
        zen_Sinistre_BioDto.setPrestataire(p == null ? "" : p.getNom());
        zen_Sinistre_BioDto.setCode_pres_bio(lp.getCodePrestataire());
        zen_Sinistre_BioDto.setDate_ecaissement(IdleDate.toString(lp.getDate(), "dd/MM/yyyy"));
        zen_Sinistre_BioDto.setNumepoli(RechercheFonction.codepolice(lp.getCodeAssurePrincipal()));
        zen_Sinistre_BioDto.setCodeinte(RechercheFonction.codeAgence(lp.getCodeAssurePrincipal()));
        zen_Sinistre_BioDto.setCoderisq(RechercheFonction.codeRisque(lp.getCodeAssurePrincipal()));
        if (!"".equals(lp.getCodeBeneficiaire()) && lp.getCodeBeneficiaire() != null) {
            zen_Sinistre_BioDto.setCodememb(RechercheFonction.codeMembre(lp.getCodeBeneficiaire()));
        }

        zen_Sinistre_BioDto.setMontant_valid(lp.getMontant());
        zen_Sinistre_BioDto.setTaux_remb(lp.getTaux());
        zen_Sinistre_BioDto.setDate_validation(IdleDate.toString(lp.getDate(), "dd/MM/yyyy"));
        if ("".equals(lp.getCodeTypePrestation()) || lp.getCodeTypePrestation() == null) {
            zen_Sinistre_BioDto.setTypeexam("PH02");
        } else {
            zen_Sinistre_BioDto.setTypeexam(lp.getCodeTypePrestation());
        }

        return zen_Sinistre_BioDto;
    }

    public Zen_Sinistre_BioDto ZenSinistreBiofromConsultation(Consultation lp) {

        Dbx45tyPrestataire p = prestataireRepositories.findById(lp.getCodePrestataire()).get();
        Zen_Sinistre_BioDto zen_Sinistre_BioDto = new Zen_Sinistre_BioDto();
        zen_Sinistre_BioDto.setPrestataire(p == null ? "" : p.getNom());
        zen_Sinistre_BioDto.setCode_pres_bio(lp.getCodePrestataire());
        zen_Sinistre_BioDto.setPrestataire(lp.getCodePrestataire());
        zen_Sinistre_BioDto.setDate_ecaissement(IdleDate.toString(lp.getDate(), "dd/MM/yyyy"));
        zen_Sinistre_BioDto.setNumepoli(RechercheFonction.codepolice(lp.getCodeAssurePrincipal()));
        zen_Sinistre_BioDto.setCodeinte(RechercheFonction.codeAgence(lp.getCodeAssurePrincipal()));
        zen_Sinistre_BioDto.setCoderisq(RechercheFonction.codeRisque(lp.getCodeAssurePrincipal()));
        if (!"".equals(lp.getCodeBeneficiaire()) && lp.getCodeBeneficiaire() != null) {
            zen_Sinistre_BioDto.setCodememb(RechercheFonction.codeMembre(lp.getCodeBeneficiaire()));
        }

        zen_Sinistre_BioDto.setMontant_valid(lp.getMontant());
        zen_Sinistre_BioDto.setTaux_remb(lp.getTauxCouverture());
        zen_Sinistre_BioDto.setDate_validation(IdleDate.toString(lp.getDate(), "dd/MM/yyyy"));
        if ("".equals(lp.getCodeTypePrestation()) || lp.getCodeTypePrestation() == null) {
            zen_Sinistre_BioDto.setTypeexam("PH02");
        } else {
            zen_Sinistre_BioDto.setTypeexam(lp.getCodeTypePrestation());
        }

        return zen_Sinistre_BioDto;
    }

//    public LignePrestation listeLignePrestationByIdPrestation(Integer idPrestation) {
//        LignePrestation sin = new LignePrestation();
//        List<LignePrestation> lgp = this.lignePrestationRepositories.findByIdPrestation(idPrestation);
//        sin = lgp.get(0);
//        Double montantValide = lgp.stream()
//                .filter(s -> (s.getIdPrestation() == idPrestation && s.getMontant() != null))
//                .mapToDouble(si -> si.getMontant())
//                .sum();
//        sin.setMontant(montantValide);
//        return sin;
//
//    }

    public Bon_Consultation fromVisiteConsultation(Dbx45tyConsultation consultation) {
        Bon_Consultation bons = new Bon_Consultation();
        Dbx45tyVisite visites = consultation.getVisiteId();
        bons.setDate(consultation.getDate().toString());
        Dbx45tyAdherent ad = adherentRepositories.findByCodeAdherent(visites.getCodeAdherent().getCodeAdherent()).get();
        if (ad != null) {
            bons.setAssure(ad.getAssurePrincipal());
            if (visites.getCodeAyantDroit() == null) {
                bons.setMalade(ad.getAssurePrincipal());
            } else {
                Dbx45tyAyantDroit ady = ayantDroitRepositories.findByCodeAyantDroit(visites.getCodeAyantDroit()).get();
                if (ady != null) {
                    bons.setMalade(ady.getNom());
                }

            }
        }

        bons.setPrestataire(prestataireRepositories.findById(visites.getPrestataireId()).get().getNom().toUpperCase());

        bons.setType_prestation(consultation.getTypeConsultation().getNom());
        bons.setMontant_valide(Integer.getInteger("" + consultation.getMontantModif().intValue()));
        bons.setSouscripteur(ad.getSouscripteur());
        bons.setPrestation_number(visites.getCodeCourt());
        System.err.println("valeur taux depart:" + consultation.getTaux());
        System.err.println("valeur taux montant valider:" + consultation.getMontantModif());
        try {
            if (consultation.getTaux() != null && consultation.getTaux().intValue() == 100) {
                System.out.println("taux : " + consultation.getTaux());
                bons.setTaux("100");
                bons.setPart_assure(0);
                bons.setPart_zenithe(consultation.getMontantModif().intValue());
                bons.setMontant_valide(consultation.getMontantModif().intValue());
            } else {
                if (consultation.getTaux() != null && consultation.getTaux().intValue() < 100) {
                    bons.setMontant_valide(consultation.getMontantModif().intValue());
                    bons.setTaux("" + consultation.getTaux().intValue());
                    Double valeur = (bons.getMontant_valide() * (100 - consultation.getTaux())) / 100;
                    System.err.println("valeur calculer:" + valeur);
                    bons.setPart_assure(valeur.intValue());
                    valeur = (consultation.getTaux() * bons.getMontant_valide()) / 100;
                    bons.setPart_zenithe(valeur.intValue());
                }

            }
        } catch (Exception e) {
            System.err.println("valeur null:" + bons.getTaux());
            System.err.println("valeur null:" + bons.getMontant_valide());
        }

        return bons;
    }

//    public TableBons fromBonsPrestataire(String p, Date date1, Date date2) {
//        TableBons bons = new TableBons();
//        Dbx45tyPrestataire prestataire = prestataireRepositories.findById(p).get();
//        bons.setPrestataire(prestataire.getNom());
//        Long nombre_bons = 0L;
//        nombre_bons = consultationTableRepository.nombreBonsbyPrestataire(date1, date2, prestataire.getId());
//        bons.setConsultation(nombre_bons.intValue());
//        nombre de bon exament
//        nombre_bons = dbx45tyLignePrestationRepositories.nombreBonsbyPrestataire(date1, date2, prestataire.getId(), "examen");
//        bons.setExamen(nombre_bons.intValue());
//        //nombre de bon Ordonnance
//        nombre_bons = dbx45tyLignePrestationRepositories.nombreBonsbyPrestataire(date1, date2, prestataire.getId(), "ordonnance");
//        bons.setOrdonance(nombre_bons.intValue());
//        bons.setNumero(1);
//        return bons;
//
//    }

//    public Static_Souscripteur fromSouscripteurReligion1(String s, Date debut, Date fin) {
//        String religion2 = "CENTRE,SUD,EST,NORD,ADAMAOUA";
//        String religion1 = "LITTORAL,OUEST,NORD_OUEST,SUD_OUEST";
//        List<Dbx45tyAdherent> listeAdherentBySouscripteur = adherentRepositories.findBySouscripteur(s);
//        Static_Souscripteur static_Souscripteur = new Static_Souscripteur();
//        static_Souscripteur.setSouscripteur(s);
//        static_Souscripteur.setGroupe(1);
//        static_Souscripteur.setReligion("LITTORAL,OUEST,NORD-OUEST,SUD-OUEST");
//        static_Souscripteur.setReligion("LITTORAL,OUEST,NORD-OUEST,SUD-OUEST");
//        ici retrouvons le nombre de consultation
//        static_Souscripteur = nombreConsultationBysouscripteur(listeAdherentBySouscripteur, debut, fin, static_Souscripteur);
//        ici retrouvons le nombre de prestation de type examen
//        static_Souscripteur = nombrePrestationBysouscripteur(listeAdherentBySouscripteur, debut, fin, static_Souscripteur, "examen");
//        ici retrouvons le nombre de prestation de type ordonance
//        static_Souscripteur = nombrePrestationBysouscripteurOrdonnance(listeAdherentBySouscripteur, debut, fin, static_Souscripteur, "ordonnance");
//
//        return static_Souscripteur;
//    }

//    public Static_Souscripteur fromSouscripteurReligion2(String s, Date debut, Date fin) {
//        String religion2 = "CENTRE,SUD,EST,NORD,ADAMAOUA";
//        String religion1 = "LITTORAL,OUEST,NORD_OUEST,SUD_OUEST";
//        List<Dbx45tyAdherent> listeAdherentBySouscripteur = adherentRepositories.findBySouscripteur(s);
//        Static_Souscripteur static_Souscripteur = new Static_Souscripteur();
//        static_Souscripteur.setSouscripteur(s);
//        static_Souscripteur.setGroupe(1);
//        static_Souscripteur.setReligion("LITTORAL,OUEST,NORD-OUEST,SUD-OUEST");
//        static_Souscripteur.setReligion("CENTRE,SUD,EST,NORD,ADAMAOUA");
//        ici retrouvons le nombre de consultation
//        static_Souscripteur = nombreConsultationBysouscripteurRg2(listeAdherentBySouscripteur, debut, fin, static_Souscripteur);
//        ici retrouvons le nombre de prestation de type examen
//        static_Souscripteur = nombrePrestationBysouscripteurRg2(listeAdherentBySouscripteur, debut, fin, static_Souscripteur, "examen");
//        ici retrouvons le nombre de prestation de type ordonance
//        static_Souscripteur = nombrePrestationBysouscripteurRg2Ordonanace(listeAdherentBySouscripteur, debut, fin, static_Souscripteur, "ordonnance");
//
//        return static_Souscripteur;
//    }

//    public Static_Souscripteur fromSouscripteur(String s, Date debut, Date fin, String ville) {
//        String religion2 = "CENTRE,SUD,EST,NORD,ADAMAOUA";
//        String religion1 = "LITTORAL,OUEST,NORD_OUEST,SUD_OUEST";
//        List<Dbx45tyAdherent> listeAdherentBySouscripteur = adherentRepositories.findBySouscripteur(s);
//        Static_Souscripteur static_Souscripteur = new Static_Souscripteur();
//        static_Souscripteur.setSouscripteur(s);
//        static_Souscripteur.setGroupe(1);
//        static_Souscripteur.setReligion("LITTORAL,OUEST,NORD-OUEST,SUD-OUEST");
//        static_Souscripteur.setReligion(ville);
//        if (ville.compareToIgnoreCase(religion1) == 0) {
//            ici retrouvons le nombre de consultation
//            ici retrouvons le nombre de consultation
//            static_Souscripteur = nombreConsultationBysouscripteur(listeAdherentBySouscripteur, debut, fin, static_Souscripteur);
//            ici retrouvons le nombre de prestation de type examen
//            static_Souscripteur = nombrePrestationBysouscripteur(listeAdherentBySouscripteur, debut, fin, static_Souscripteur, "examen");
//            ici retrouvons le nombre de prestation de type ordonance
//            static_Souscripteur = nombrePrestationBysouscripteur(listeAdherentBySouscripteur, debut, fin, static_Souscripteur, "ordonnance");
//        } else if (ville.compareToIgnoreCase(religion2) == 0) {
//            static_Souscripteur = nombreConsultationBysouscripteurRg2(listeAdherentBySouscripteur, debut, fin, static_Souscripteur);
//            ici retrouvons le nombre de prestation de type examen
//            static_Souscripteur = nombrePrestationBysouscripteurRg2(listeAdherentBySouscripteur, debut, fin, static_Souscripteur, "examen");
//            ici retrouvons le nombre de prestation de type ordonance
//            static_Souscripteur = nombrePrestationBysouscripteurRg2(listeAdherentBySouscripteur, debut, fin, static_Souscripteur, "ordonnance");
//        }
//
//        return static_Souscripteur;
//    }

//    public Static_Souscripteur nombreConsultationBysouscripteur(List<Dbx45tyAdherent> ad, Date debut, Date fin, Static_Souscripteur stat) {
//        System.out.println("nombreConsultationBysouscripteur qvqnt le null");
//        List<Dbx45tyConsultation> listeConsultations = ad.stream().flatMap(ass -> consultationTableRepository.listeeBonsConsultationByAdherent(debut, fin, ass.getCodeAdherent()).stream())
//                .collect(Collectors.toList());
//
//        int nombre_bon = listeConsultations.stream().filter(c -> (c.getEtatConsultation().equalsIgnoreCase("encaisse") && c.getMontantModif() != null && Objects.equals(controleConsultationRegion1(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .map(c -> c).collect(Collectors.toList()).size();
//        listeConsultations.forEach(System.out::println);
//        Double montant_bon = listeConsultations.stream().filter(c -> (c.getEtatConsultation().equalsIgnoreCase("encaisse") && c.getMontantModif() != null && Objects.equals(controleConsultationRegion1(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .mapToDouble(c -> c.getTaux().intValue() == 100 ? c.getMontantModif() : (c.getTaux() * c.getMontantModif()) / 100)
//                .sum();
//        stat.setMontant_consul(montant_bon.longValue());
//        stat.setConsultation(nombre_bon);
//        System.out.println("souscripteur " + stat.getSouscripteur());
//        System.out.println("nbrevconsultation " + stat.getConsultation());
//        System.out.println("montant" + stat.getMontant_consul());
//        return stat;
//
//    }

//    public Static_Souscripteur nombrePrestationBysouscripteur(List<Dbx45tyAdherent> ad, Date debut, Date fin, Static_Souscripteur stat, String prestation) {
//        List<Dbx45tyLignePrestation> listePrestations = ad.stream().flatMap(ass -> dbx45tyLignePrestationRepositories.listeBonsByAdherent(debut, fin, ass.getCodeAdherent(), prestation).stream())
//                .collect(Collectors.toList());
//        int nombre_bon = listePrestations.stream().filter(c -> (c.getValeurModif() != null && Objects.equals(controlePrestationRegion1(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .map(c -> c).collect(Collectors.toList()).size();
//
//        Double montant_bon = listePrestations.stream().filter(c -> (c.getValeurModif() != null && Objects.equals(controlePrestationRegion1(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .mapToDouble(c -> (calculMontantPrestation(c.getPrestationId())))
//                .sum();
//        if (prestation.equalsIgnoreCase("examen")) {
//            stat.setExament(nombre_bon);
//            stat.setMontant_exam(montant_bon.longValue());
//        } else if (prestation.equalsIgnoreCase("ordonnance")) {
//            stat.setOrdonance(nombre_bon);
//            stat.setMontant_ordonance(montant_bon.longValue());
//        }
//
//        return stat;
//
//    }

//    public Static_Souscripteur nombrePrestationBysouscripteurOrdonnance(List<Dbx45tyAdherent> ad, Date debut, Date fin, Static_Souscripteur stat, String prestation) {
//        List<Dbx45tyLignePrestation> listePrestations = ad.stream().flatMap(ass -> dbx45tyLignePrestationRepositories.listeBonsByAdherent(debut, fin, ass.getCodeAdherent(), prestation).stream())
//                .collect(Collectors.toList());
//        int nombre_bon = listePrestations.stream().filter(c -> (c.getValeurModif() != null && Objects.equals(controlePrestationRegion1Ordonnace(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .map(c -> c).collect(Collectors.toList()).size();
//
//        Double montant_bon = listePrestations.stream().filter(c -> (c.getValeurModif() != null && Objects.equals(controlePrestationRegion1Ordonnace(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .mapToDouble(c -> (calculMontantPrestation(c.getPrestationId())))
//                .sum();
//        if (prestation.equalsIgnoreCase("examen")) {
//            stat.setExament(nombre_bon);
//            stat.setMontant_exam(montant_bon.longValue());
//        } else if (prestation.equalsIgnoreCase("ordonnance")) {
//            stat.setOrdonance(nombre_bon);
//            stat.setMontant_ordonance(montant_bon.longValue());
//        }
//
//        return stat;
//
//    }

//    public Static_Souscripteur nombreConsultationBysouscripteurRg2(List<Dbx45tyAdherent> ad, Date debut, Date fin, Static_Souscripteur stat) {
//        System.out.println("nombreConsultationBysouscripteur qvqnt le null");
//        List<Dbx45tyConsultation> listeConsultations = ad.stream().flatMap(ass -> consultationTableRepository.listeeBonsConsultationByAdherent(debut, fin, ass.getCodeAdherent()).stream())
//                .collect(Collectors.toList());
//
//        int nombre_bon = listeConsultations.stream().filter(c -> (c.getEtatConsultation().equalsIgnoreCase("encaisse") && c.getMontantModif() != null && Objects.equals(controleConsultationRegion2(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .map(c -> c).collect(Collectors.toList()).size();
//        listeConsultations.forEach(System.out::println);
//        Double montant_bon = listeConsultations.stream().filter(c -> (c.getEtatConsultation().equalsIgnoreCase("encaisse") && c.getMontantModif() != null && Objects.equals(controleConsultationRegion2(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .mapToDouble(c -> c.getTaux().intValue() == 100 ? c.getMontantModif() : (c.getTaux() * c.getMontantModif()) / 100)
//                .sum();
//        stat.setMontant_consul(montant_bon.longValue());
//        stat.setConsultation(nombre_bon);
//        System.out.println("souscripteur " + stat.getSouscripteur());
//        System.out.println("nbrevconsultation " + stat.getConsultation());
//        System.out.println("montant" + stat.getMontant_consul());
//        return stat;

    }

//    public Static_Souscripteur nombreConsultationBysouscripteurPrincipal(List<Dbx45tyAdherent> ad, Date debut, Date fin, Static_Souscripteur stat) {
//        System.out.println("nombreConsultationBysouscripteur qvqnt le null");
//        List<Dbx45tyConsultation> listeConsultations = ad.stream().flatMap(ass -> consultationTableRepository.listeeBonsConsultationByAdherent(debut, fin, ass.getCodeAdherent()).stream())
//                .collect(Collectors.toList());
//
//        int nombre_bon = listeConsultations.stream().filter(c -> (c.getEtatConsultation().equalsIgnoreCase("encaisse") && c.getMontantModif() != null && Objects.equals(controleConsultationRegion2(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .map(c -> c).collect(Collectors.toList()).size();
//        listeConsultations.forEach(System.out::println);
//        Double montant_bon = listeConsultations.stream().filter(c -> (c.getEtatConsultation().equalsIgnoreCase("encaisse") && c.getMontantModif() != null && Objects.equals(controleConsultationRegion2(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .mapToDouble(c -> c.getTaux().intValue() == 100 ? c.getMontantModif() : (c.getTaux() * c.getMontantModif()) / 100)
//                .sum();
//        stat.setMontant_consul(montant_bon.longValue());
//        stat.setConsultation(nombre_bon);
//        System.out.println("souscripteur " + stat.getSouscripteur());
//        System.out.println("nbrevconsultation " + stat.getConsultation());
//        System.out.println("montant" + stat.getMontant_consul());
//        return stat;
//
//    }

//    public Static_Souscripteur nombrePrestationBysouscripteurRg2(List<Dbx45tyAdherent> ad, Date debut, Date fin, Static_Souscripteur stat, String prestation) {
//        List<Dbx45tyLignePrestation> listePrestations = ad.stream().flatMap(ass -> dbx45tyLignePrestationRepositories.listeBonsByAdherent(debut, fin, ass.getCodeAdherent(), prestation).stream())
//                .collect(Collectors.toList());
//        int nombre_bon = listePrestations.stream().filter(c -> (c.getValeurModif() != null && Objects.equals(controlePrestationRegion2(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .map(c -> c).collect(Collectors.toList()).size();
//
//        Double montant_bon = listePrestations.stream().filter(c -> (c.getValeurModif() != null && Objects.equals(controlePrestationRegion2(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .mapToDouble(c -> (calculMontantPrestation(c.getPrestationId())))
//                .sum();
//        if (prestation.equalsIgnoreCase("examen")) {
//            stat.setExament(nombre_bon);
//            stat.setMontant_exam(montant_bon.longValue());
//        } else if (prestation.equalsIgnoreCase("ordonnance")) {
//            stat.setOrdonance(nombre_bon);
//            stat.setMontant_ordonance(montant_bon.longValue());
//        }
//
//        return stat;

//    }

//    public Static_Souscripteur nombrePrestationBysouscripteurRg2Ordonanace(List<Dbx45tyAdherent> ad, Date debut, Date fin, Static_Souscripteur stat, String prestation) {
//        List<Dbx45tyLignePrestation> listePrestations = ad.stream().flatMap(ass -> dbx45tyLignePrestationRepositories.listeBonsByAdherent(debut, fin, ass.getCodeAdherent(), prestation).stream())
//                .collect(Collectors.toList());
//        int nombre_bon = listePrestations.stream().filter(c -> (c.getValeurModif() != null && Objects.equals(controlePrestationRegion2Ordonance(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .map(c -> c).collect(Collectors.toList()).size();
//
//        Double montant_bon = listePrestations.stream().filter(c -> (c.getValeurModif() != null && Objects.equals(controlePrestationRegion2Ordonance(c), Boolean.TRUE) && c.getTaux() != null && c.getTaux().intValue() != 0))
//                .mapToDouble(c -> (calculMontantPrestation(c.getPrestationId())))
//                .sum();
//        if (prestation.equalsIgnoreCase("examen")) {
//            stat.setExament(nombre_bon);
//            stat.setMontant_exam(montant_bon.longValue());
//        } else if (prestation.equalsIgnoreCase("ordonnance")) {
//            stat.setOrdonance(nombre_bon);
//            stat.setMontant_ordonance(montant_bon.longValue());
//        }
//
//        return stat;
//
//    }

//    public Boolean controleConsultationRegion1(Dbx45tyConsultation c) {
//        String religion1 = "LITTORAL,OUEST,NORD_OUEST,SUD_OUEST";
//        System.out.println("code prestataire: " + c.getVisiteId().getPrestataireId());
//        Dbx45tyPrestataire p = null;
//        try {
//
//            p = this.prestataireRepositories.findById(c.getVisiteId().getPrestataireId()).get();
//            System.out.println("requette ok");
//            System.out.println("id consultation " + c.getId());
//            System.out.println("id visist " + c.getVisiteId());
//            System.out.println("tqux " + c.getTaux().toString());
//            System.out.println("mt " + c.getMontantModif().toString());
//        } catch (Exception e) {
//            System.out.println("null Exception");
//            System.out.println("id consultation " + c.getId());
//            System.out.println("id visist " + c.getVisiteId());
//            System.out.println("tqux " + c.getTaux().toString());
//            System.out.println("mt " + c.getMontantModif().toString());
//        }
//
//        if (p.getVilleId() == null) {
//            System.out.println("return false ville null");
//            return Boolean.FALSE;
//        }
//        Dbx45tyRegion r = regionRepositories.findById(p.getVilleId().getRegionId()).get();
//        System.out.println("code region " + r.getCode());
//        if (r.getCode().equalsIgnoreCase("LITTORAL") || r.getCode().equalsIgnoreCase("OUEST") || r.getCode().equalsIgnoreCase("NORD_OUEST") || r.getCode().equalsIgnoreCase("SUD_OUEST")) {
//            System.out.println("return true");
//            return Boolean.TRUE;
//
//        }
//        System.out.println("return false");
//        return Boolean.FALSE;
//    }
//
//    public Boolean controleConsultationRegion2(Dbx45tyConsultation c) {
//        String religion2 = "CENTRE,SUD,EST,NORD,ADAMAOUA";
//        Dbx45tyPrestataire p = this.prestataireRepositories.findById(c.getVisiteId().getPrestataireId()).get();
//        if (p.getVilleId() == null) {
//            return Boolean.FALSE;
//        }
//        Dbx45tyRegion r = regionRepositories.findById(p.getVilleId().getRegionId()).get();
//        if (r.getCode().equalsIgnoreCase("CENTRE") || r.getCode().equalsIgnoreCase("SUD") || r.getCode().equalsIgnoreCase("EST") || r.getCode().equalsIgnoreCase("NORD") || r.getCode().equalsIgnoreCase("ADAMAOUA")) {
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
//    }
//
//    public Boolean controlePrestationRegion1(Dbx45tyLignePrestation pr) {
//        String religion1 = "LITTORAL,OUEST,NORD_OUEST,SUD_OUEST";
//
//        Dbx45tyPrestataire p = this.prestataireRepositories.findById(pr.getPrestationId().getPrestataireId().getId()).get();
//        if (p.getVilleId() == null) {
//            return Boolean.FALSE;
//        }
//        Dbx45tyRegion r = regionRepositories.findById(p.getVilleId().getRegionId()).get();
//        if (r.getCode().equalsIgnoreCase("LITTORAL") || r.getCode().equalsIgnoreCase("OUEST") || r.getCode().equalsIgnoreCase("NORD_OUEST") || r.getCode().equalsIgnoreCase("SUD_OUEST")) {
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
//    }
//
//    public Boolean controlePrestationRegion1Ordonnace(Dbx45tyLignePrestation pr) {
//        String religion1 = "LITTORAL,OUEST,NORD_OUEST,SUD_OUEST";
//
//        Dbx45tyPrestataire p = this.prestataireRepositories.findById(pr.getPrestationId().getPrestataireId().getId()).get();
//        if (p.getVilleId() == null) {
//            return Boolean.FALSE;
//        }
//        Dbx45tyRegion r = regionRepositories.findById(p.getVilleId().getRegionId()).get();
//        if (r.getCode().equalsIgnoreCase("LITTORAL") || r.getCode().equalsIgnoreCase("OUEST") || r.getCode().equalsIgnoreCase("NORD_OUEST") || r.getCode().equalsIgnoreCase("SUD_OUEST")) {
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
//    }

//    public Boolean controlePrestationRegion2(Dbx45tyLignePrestation pr) {
//        String religion2 = "CENTRE,SUD,EST,NORD,ADAMAOUA";
//        Dbx45tyPrestataire p = this.prestataireRepositories.findById(pr.getPrestationId().getPrestataireId().getId()).get();
//        if (p.getVilleId() == null) {
//            return Boolean.FALSE;
//        }
//        Dbx45tyRegion r = regionRepositories.findById(p.getVilleId().getRegionId()).get();
//        System.out.println("region2: " + r.getCode());
//        if (r.getCode().equalsIgnoreCase("CENTRE") || r.getCode().equalsIgnoreCase("SUD") || r.getCode().equalsIgnoreCase("EST") || r.getCode().equalsIgnoreCase("NORD") || r.getCode().equalsIgnoreCase("ADAMAOUA")) {
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
//
//    }
//
//    public Boolean controlePrestationRegion2Ordonance(Dbx45tyLignePrestation pr) {
//        String religion2 = "CENTRE,SUD,EST,NORD,ADAMAOUA";
//        Dbx45tyPrestataire p = this.prestataireRepositories.findById(pr.getPrestationId().getPrestataireId().getId()).get();
//        if (p.getVilleId() == null) {
//            return Boolean.FALSE;
//        }
//        Dbx45tyRegion r = regionRepositories.findById(p.getVilleId().getRegionId()).get();
//        System.out.println("region2: " + r.getCode());
//        if (r.getCode().equalsIgnoreCase("CENTRE") || r.getCode().equalsIgnoreCase("SUD") || r.getCode().equalsIgnoreCase("EST") || r.getCode().equalsIgnoreCase("NORD") || r.getCode().equalsIgnoreCase("ADAMAOUA")) {
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
//
//    }

//    public Double calculMontantPrestation(Dbx45tyPrestation prestation_id) {
//        System.out.println("calcul du montant---");
//        dbx45tyLignePrestationRepositories.findByPrestationId(prestation_id).forEach(System.out::println);
//        Double montant = dbx45tyLignePrestationRepositories.findByPrestationId(prestation_id).stream()
//                .filter(pr -> (pr.getValeurModif() != null && pr.getNbreModif() != null && pr.getEtat().equalsIgnoreCase("encaisse")))
//                .mapToDouble(pr -> pr.getTaux().intValue() == 100 ? pr.getValeurModif() * pr.getNbreModif() : (pr.getTaux() * (pr.getValeurModif() * pr.getNbreModif())) / 100)
//                .sum();
//        return montant;
//    }

//    public SouscripteurBons lignePrestationFronSouscripteurBons(List<Dbx45tyLignePrestation> l, String Souscripteur, String assure, Date debut, Date fin) {
//
//        SouscripteurBons s = new SouscripteurBons();
//        s.setSouscripteur(Souscripteur);
//        s.setDate_debut(debut);
//        s.setDate_fin(fin);
//        s.setAssure(adherentRepositories.findByCodeAdherent(assure).getAssurePrincipal());
//        s.setTaux("");
//        s = valueTaux(l, s);
//        Double examen = l.stream()
//                .filter(e -> e.getPrestationId().getNaturePrestation().equalsIgnoreCase("examen") && e.getEtat().equalsIgnoreCase("encaisse"))
//                .mapToDouble(e -> e.getTaux().intValue() == 100 ? 0.0 : ((100 - e.getTaux()) * (e.getValeurModif() * e.getNbreModif())) / 100)
//                .sum();
//
//        Double ordonance = l.stream()
//                .filter(e -> e.getPrestationId().getNaturePrestation().equalsIgnoreCase("ordonnance") && e.getEtat().equalsIgnoreCase("encaisse"))
//                .mapToDouble(e -> e.getTaux().intValue() == 100 ? 0.0 : ((100 - e.getTaux()) * (e.getValeurModif() * e.getNbreModif())) / 100)
//                .sum();
//        s.setExament(examen);
//        s.setOrdonance(ordonance);
//        return consultationByAssure(s, assure, debut, fin);
//
//    }

//    public List<Bons_adherent_details> lignePrestationFronSouscripteurBonsDetails(List<Dbx45tyVisite> v, String Souscripteur, String assure, Date debut, Date fin) {
//
//        System.out.println("assure :" + assure);
//        for (Dbx45tyVisite vi : v) {
//            System.out.println("visite : " + vi.getId());
//        }
//        List<Bons_adherent_details> bd = new ArrayList<>();
//        List<Bons_adherent_details> bd_consul = new ArrayList<>();
//        List<Dbx45tyLignePrestation> l = lignePrestation(v);
//        Dbx45tyConsultation consul;
//        Dbx45tyConsultation connError = null;
//        Dbx45tyLignePrestation lignePrestationError = null;
//
//        List<Dbx45tyConsultation> consul = new ArrayList<>();
//        Double consultation = 0.0;
//        Double examen = 0.0;
//        Double ordonnance = 0.0;
//        Double taux_couverture = 0.0;
//        if (l != null && !l.isEmpty()) {
//            while (l.contains(lignePrestationError)) {
//                l.remove(lignePrestationError);
//            }
//
//            bd = l.stream()
//                    .filter(e -> (e.getPrestationId().getNaturePrestation().equalsIgnoreCase("examen") || e.getPrestationId().getNaturePrestation().equalsIgnoreCase("ordonnance")) && e.getEtat().equalsIgnoreCase("encaisse"))
//                    .map(e -> fronPrestationAndConsultation(e, null, "prestation", assure))
//                    .collect(Collectors.toList());
//        }
//
//        consul = v.stream().map(vi -> consultationTableRepository.findByVisiteId(vi))
//                .collect(Collectors.toList());
//        while (consul.contains(connError)) {
//            consul.remove(connError);
//        }
//        consul.forEach(cn -> System.out.println("id " + cn.getId() + "nature consultation " + cn.getNatureConsultation() + " etat consultation " + cn.getEtatConsultation() + " montant " + cn.getMontantModif() + " taux " + cn.getTaux()));
//
//        if (consul != null && !consul.isEmpty()) {
//            il ny aucune prestationpour cette d assure verifions au niveau de la consulstation  
//            v.forEach(vi -> {
//                consul.add(consultationTableRepository.findByVisiteId(vi.getId()));
//            });
//
//            System.out.println("");
//            bd_consul = consul.stream()
//                    .filter(c -> c.getMontantModif() != null && c != null && c.getNatureConsultation().equalsIgnoreCase("payante") && c.getTaux().intValue() != 0 && c.getEtatConsultation().equalsIgnoreCase("encaisse"))
//                    .map(c -> fronPrestationAndConsultation(null, c, "consultation", assure))
//                    .collect(Collectors.toList());
//
//            bd.addAll(bd_consul);
//        }
//        return bd;
//    }

//    public Bons_adherent_details fronPrestationAndConsultation(Dbx45tyLignePrestation pr, Dbx45tyConsultation con, String prestation, String assure) {
//        Bons_adherent_details bd = new Bons_adherent_details();
//        if (prestation.equalsIgnoreCase("prestation")) {
//            bd.setAssure(adherentRepositories.findByCodeAdherent(assure).getAssurePrincipal());
//            bd.setDate_debut(pr.getDateEncaisse());
//            bd.setMontant(BigInteger.valueOf(pr.getValeurModif().longValue()));
//            bd.setNombre(BigInteger.valueOf(pr.getNbreModif().longValue()));
//            bd.setTotal(bd.getMontant().multiply(bd.getNombre()));
//            bd.setNom(pr.getNom());
//            bd.setPrestataire(pr.getPrestataireId().getNom());
//            bd.setPrestation(pr.getPrestationId().getNaturePrestation());
//            bd.setTaux_couverture("" + pr.getTaux() + "%");
//
//        } else if (prestation.equalsIgnoreCase("consultation")) {
//            bd.setAssure(adherentRepositories.findByCodeAdherent(assure).getAssurePrincipal());
//            bd.setDate_debut(con.getDate());
//            bd.setMontant(BigInteger.valueOf(con.getMontantModif().longValue()));
//            bd.setNombre(BigInteger.ONE);
//            bd.setTotal(bd.getMontant().multiply(bd.getNombre()));
//            bd.setNom(con.getTypeConsultation().getNom());
//            bd.setPrestataire(con.getVisiteId().getPrestataireId());
//            bd.setPrestation("consultation");
//            bd.setTaux_couverture("" + con.getTaux() + "%");
//        }
//
//        return bd;
//    }

//    public SouscripteurBons lignePrestationFronSouscripteurBonsWithVisit(List<Dbx45tyVisite> v, String Souscripteur, String assure, Date debut, Date fin) {
//
//        System.out.println("valeur de i:"+i);
//        System.out.println("assure :" + assure);
//        for (Dbx45tyVisite vi : v) {
//            System.out.println("visite : " + vi.getId());
//        }
//        SouscripteurBons s = new SouscripteurBons();
//        List<Dbx45tyLignePrestation> l = lignePrestation(v);
//        Dbx45tyConsultation consul;
//        Dbx45tyConsultation connError = null;
//        Dbx45tyLignePrestation lignePrestationError = null;
//
//        List<Dbx45tyConsultation> consul = new ArrayList<>();
//        Double consultation = 0.0;
//        Double examen = 0.0;
//        Double ordonnance = 0.0;
//        Double taux_couverture = 0.0;
//        if (l != null && !l.isEmpty()) {
//            while (l.contains(lignePrestationError)) {
//                l.remove(lignePrestationError);
//            }
//
//            examen = l.stream()
//                    .filter(e -> e.getPrestationId().getNaturePrestation().equalsIgnoreCase("examen") && e.getEtat().equalsIgnoreCase("encaisse"))
//                    .mapToDouble(e -> e.getTaux().intValue() == 100 ? e.getValeurModif() * e.getNbreModif() : ((100 - e.getTaux()) * (e.getValeurModif() * e.getNbreModif())) / 100)
//                    .sum();
//
//            s.setExament(examen);
//            ordonnance = l.stream()
//                    .filter(e -> e.getPrestationId().getNaturePrestation().equalsIgnoreCase("ordonnance") && e.getEtat().equalsIgnoreCase("encaisse"))
//                    .mapToDouble(e -> e.getTaux().intValue() == 100 ? e.getValeurModif() * e.getNbreModif() : ((100 - e.getTaux()) * (e.getValeurModif() * e.getNbreModif())) / 100)
//                    .sum();
//            s.setOrdonance(ordonnance);
//            if (ordonnance.intValue() != 0 || examen.intValue() != 0) {
//                for (int i = 0; i < l.size(); i++) {
//                    Dbx45tyLignePrestation e = l.get(i);
//                    if ((e.getPrestationId().getNaturePrestation().equalsIgnoreCase("ordonnance") && e.getEtat().equalsIgnoreCase("encaisse")) || e.getPrestationId().getNaturePrestation().equalsIgnoreCase("examen") && e.getEtat().equalsIgnoreCase("encaisse")) {
//                        s.setTaux_exam_ordon("" + e.getTaux() + "%");
//                        taux_couverture = e.getTaux();
//                        if (taux_couverture.intValue() < 100) {
//
//                taux_couverture = 100 - taux_couverture;
//                            s.setPart_assure_exam_ordon(((100 - taux_couverture.intValue()) * (examen + ordonnance)) / 100);
//                        } else {
//                s.setTaux_exam_ordon("" + taux_couverture + "%");
//                            s.setPart_assure_exam_ordon(0.0);
//                        }
//
//                    }
//                    else {
//                        s.setExament(0.0);
//                        s.setOrdonance(0.0);
//
//                    }
//                    break;
//                }
//            } else {
//                s.setPart_assure_exam_ordon(0.0);
//                s.setTaux_exam_ordon("0%");
//            }
//
//        } else {
//            s.setExament(0.0);
//            s.setOrdonance(0.0);
//            s.setPart_assure_exam_ordon(0.0);
//            s.setTaux_exam_ordon("0%");
//
//        }
//
//        consul = v.stream().map(vi -> consultationTableRepository.findByVisiteId(vi))
//                .collect(Collectors.toList());
//        while (consul.contains(connError)) {
//            consul.remove(connError);
//        }
//        consul.forEach(cn -> System.out.println("id " + cn.getId() + "nature consultation " + cn.getNatureConsultation() + " etat consultation " + cn.getEtatConsultation() + " montant " + cn.getMontantModif() + " taux " + cn.getTaux()));
//
//        if (consul != null && !consul.isEmpty()) {
//            il ny aucune prestationpour cette d assure verifions au niveau de la consulstation  
//            v.forEach(vi -> {
//                consul.add(consultationTableRepository.findByVisiteId(vi.getId()));
//            });
//
//            System.out.println("");
//            consultation = consul.stream()
//                    .filter(c -> c.getMontantModif() != null && c != null && c.getNatureConsultation().equalsIgnoreCase("payante") && c.getTaux().intValue() != 0 && c.getEtatConsultation().equalsIgnoreCase("encaisse"))
//                    .mapToDouble(c -> c.getTaux().intValue() == 100 ? c.getMontantModif() : ((c.getTaux()) * (c.getMontantModif()) / 100))
//                    .sum();
//
//            s.setConsultation(consultation);
//            if (consultation.intValue() != 0) {
//                for (int i = 0; i < consul.size(); i++) {
//                    Dbx45tyConsultation c = consul.get(i);
//                    if (c.getMontantModif() != null && c != null && c.getNatureConsultation().equalsIgnoreCase("payante") && c.getTaux().intValue() != 0 && c.getEtatConsultation().equalsIgnoreCase("encaisse")) {
//                        s.setTaux_consul("" + taux_couverture + "%");
//                        taux_couverture = c.getTaux();
//                        if (taux_couverture.intValue() < 100) {
//
//                taux_couverture = 100 - taux_couverture;
//                            s.setPart_assure_consul((taux_couverture * consultation) / 100);
//                        } else {
//                            s.setTaux_consul("" + taux_couverture + "%");
//                            s.setPart_assure_consul(0.0);
//                        }
//                        break;
//                    }
//
//                }
//            } else {
//                s.setTaux_consul("0%");
//                s.setPart_assure_consul(0.0);
//            }
//
//        }
//
//        s.setSouscripteur(Souscripteur);
//        s.setDate_debut(debut);
//        s.setDate_fin(fin);
//        s.setAssure(adherentRepositories.findByCodeAdherent(assure).getAssurePrincipal());
//        s.setTaux("");
//        s = valueTaux(l, s);
//
//        return consultationByAssure(s, assure, debut, fin);
//        return s;
//    }

//    public List<Dbx45tyLignePrestation> lignePrestation(List<Dbx45tyVisite> v) {
//        List<Dbx45tyLignePrestation> l = new ArrayList<>();
//        v.forEach(vi -> {
//            try {
//                Dbx45tyPrestation p = prestationRepositories.findByVisiteId(vi);
//                if (p != null && p.getId() != null) {
//                    l.addAll(dbx45tyLignePrestationRepositories.findByPrestationId(p));
//                }
//            } catch (Exception e) {
//
//            }
//
//        });
//
//        return l;

//    }

//    public SouscripteurBons consultationByAssure(SouscripteurBons s, String assure, Date debut, Date fin) {
//        Double consultation = this.consultationTableRepository.listeeBonsConsultationByAdherent(debut, fin, assure).stream()
//                .filter(c -> c.getEtatConsultation().equalsIgnoreCase("encaisse"))
//                .mapToDouble(c -> c.getTaux().intValue() == 100 ? 0.0 : ((100 - c.getTaux()) * (c.getMontantModif())) / 100)
//                .sum();
//        s.setConsultation(consultation);
//        return s;
//    }

//    public SouscripteurBons valueTaux(List<Dbx45tyLignePrestation> l, SouscripteurBons s) {
//        l.forEach(pr -> {
//            System.err.println("taux" + pr.getTaux());
//            if (pr.getEtat().equalsIgnoreCase("encaisse")) {
////                s.setTaux(pr.getTaux().intValue() == 100 ? "0%": ""+(100-pr.getTaux())+"%");
//                s.setTaux("" + pr.getTaux().intValue());
//                System.err.println("taux encaisse" + pr.getTaux());
//                return;
//            }
//
//        });
//        return s;
//    }
//}
