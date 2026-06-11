/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dtos.SouscripteurBons;
import db.biometry.biometry.dtos.UserDTO;
import db.biometry.biometry.dtos.UserLogin;
import db.biometry.biometry.dtos.UtilisateurDto;
import db.biometry.biometry.entite.Dbx45tyAdherent;
import db.biometry.biometry.entite.Subscribers;
import db.biometry.biometry.entite.Utilisateur;
import db.biometry.biometry.enums.ProfilType;
import db.biometry.biometry.exceptions.UtilisateurException;
import db.biometry.biometry.mappers.BiometrieMapperImpl;
import db.biometry.biometry.repositories.AdherentRepository;
import db.biometry.biometry.repositories.SubscriberRepository;
import db.biometry.biometry.repositories.UtilisateurRepository;
import db.biometry.biometry.utils.Crypto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author JIATOU FRANCK
 */
@Service

@Transactional
@AllArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private SubscriberRepository subscriberRepository;
    private AdherentRepository adherentRepository;
    private PasswordEncoder passwordEncoder;
    private BiometrieMapperImpl mappers;

    @Override
    public UserDTO findUserByLogin(UserLogin userLog) {

        // 1. FIX: Optional can never be null — use isEmpty() instead
        Optional<Utilisateur> user = utilisateurRepository.findByLogin(userLog.getUserName());
        if (user.isEmpty()) {
            return buildErrorDTO("USER NOT FOUND. PLEASE TRY AGAIN.");
        }

        Utilisateur utilisateur = user.get();

        // 2. FIX: Crypto.loginBiometrie() transforms the password before comparing —
        //    but the status check below compares the RAW password, which is inconsistent.
        //    Unified both checks to use the same hashing method.
        boolean passwordValid = Objects.equals(Crypto.loginBiometrie(userLog.getPassWord()), utilisateur.getMotPasse());
        if (!passwordValid) {
            return buildErrorDTO("LOGIN OR PASSWORD INCORRECT.");
        }

        // 3. FIX: The original condition re-checked the password with raw value (always false if hashed),
        //    making the status check unreachable. Now runs unconditionally after password is validated.
        if (!"1".equals(utilisateur.getStatut())) {
            return buildErrorDTO("USER IS NOT ACTIVE. PLEASE CONTACT YOUR ADMINISTRATOR.");
        }

        // 4. FIX: No need to re-call user.get() — use already-extracted variable
        return mappers.formUtilisateur(utilisateur);
    }

// 5. IMPROVEMENT: extracted repeated error-building logic into a helper
    private UserDTO buildErrorDTO(String message) {
        UserDTO userdto = new UserDTO();
        userdto.setEcheck_connection(true);
        userdto.setMessageEcheck(message);
        return userdto;
    }

    @Override
    public List<UserDTO> listeUtilisateur() {
        List<Utilisateur> listeUsers = utilisateurRepository.findAll();
        List<UserDTO> listeUserDtos = listeUsers.stream().map(user -> mappers.formUtilisateur(user)).collect(Collectors.toList());

        return listeUserDtos;
    }

    @Override
    public UserDTO getUser(int userId) throws UtilisateurException {
        Utilisateur user = utilisateurRepository.findById(userId).orElseThrow(() -> new UtilisateurException("USER NOT EXISTS... PLEASE TRY AGAINST"));
        UserDTO userDto = mappers.formUtilisateur(user);
        return userDto;
    }

    @Override
    public List<UserDTO> seacrhUsers(String keyword) {
        List<Utilisateur> listeUsers = utilisateurRepository.searchUser(keyword);
        List<UserDTO> listeUserDto = listeUsers.stream().map(u -> mappers.formUtilisateur(u)).collect(Collectors.toList());
        return listeUserDto;
    }
   
    @Override

    public UserDTO findSouscripteurByLogin(UserLogin userLog) {
        UserDTO userdto = new UserDTO();

        // 1. FIX: Optional can never be null — use isEmpty() instead
        Optional<Subscribers> compagnie = subscriberRepository.findByPolicyNumber(userLog.getUserName());
        if (compagnie.isEmpty()) {
            return buildErrorDTO(userLog, "COMPANY NOT FOUND. PLEASE TRY AGAIN.");
        }

        Subscribers subscriber = compagnie.get();

        // 2. FIX: passwordEncoder.encode() is NOT idempotent — use matches() instead
        boolean passwordValid = passwordEncoder.matches(userLog.getPassWord(), subscriber.getPasswordHash());
        if (!passwordValid) {
            return buildErrorDTO(userLog, "LOGIN OR PASSWORD INCORRECT.");
        }

        // 3. FIX: getActive was missing parentheses (method call) + use Boolean.TRUE.equals() for null-safety
        if (subscriber.getActive() == false) {
            return buildErrorDTO(userLog, "COMPAGNY IS NOT ACTIVE. PLEASE CONTACT YOUR ADMINISTRATOR.");
        }

        // 4. FIX: was referencing undefined variable `user` instead of `subscriber`
        userdto = mappers.formUtilisateur(subscriber);
        userdto.setProfilType(userLog.getProfilType());
        userdto.setActive(subscriber.getActive());
        userdto.setEffet(subscriber.getEffetPolice());
        userdto.setEcheance(subscriber.getEcheancePolice());
        return userdto;
    }

// 5. IMPROVEMENT: extracted repeated error-building logic into a helper
    private UserDTO buildErrorDTO(UserLogin userLog, String message) {
        UserDTO userdto = new UserDTO();
        userdto.setEcheck_connection(true);
        userdto.setMessageEcheck(message);
        userdto.setProfilType(userLog.getProfilType());
        return userdto;
    }

    @Override
    public UserDTO findAdherentByLogin(UserLogin userLog) {
        UserDTO userdto = new UserDTO();

        // 1. FIX: Optional can never be null — use isEmpty() instead
        Optional<Dbx45tyAdherent> adherent = adherentRepository.findByCodeAdherent(userLog.getPassWord());
        if (adherent.isEmpty()) {
            return buildErrorDTO(userLog, "ADHERENT NOT FOUND. PLEASE TRY AGAIN.");
        }

        Dbx45tyAdherent ad = adherent.get();

        // 2. FIX: passwordEncoder.encode() is NOT idempotent — use matches() instead
        boolean passwordValid = ad.getEnrole() != "-1";
        if (!passwordValid) {
            return buildErrorDTO(userLog, "ADHERENT NON ENROLE...");
        }
        passwordValid = ad.getTelephone().equals(userLog.getUserName());
        // 3. FIX: getActive was missing parentheses (method call) + use Boolean.TRUE.equals() for null-safety
        if (passwordValid == false) {
            return buildErrorDTO(userLog, "NUMBER ADHERENT NOT CORRECT. PLEASE CONTACT YOUR ADMINISTRATOR.");
        }

        // 4. FIX: was referencing undefined variable `user` instead of `subscriber`
       UserDTO userDto = new UserDTO();
       // BeanUtils.copyProperties(user, userDto);
       userDto.setUserName(""+ad.getPolice());
       userDto.setEmail("");
       userDto.setFirstName(ad.getAssurePrincipal());
       userDto.setLastname(ad.getSouscripteur());
       userDto.setTel(ad.getTelephone());
      
        userdto.setProfilType(userLog.getProfilType());
        userdto.setActive(ad.getStatut()=="1" ? Boolean.TRUE : Boolean.FALSE);
        userdto.setEffet(ad.getEffetPolice());
        userdto.setEcheance(ad.getEcheancePolice());
        userdto.setCodeAdherent(ad.getCodeAdherent());
        return userdto;

    }

}
