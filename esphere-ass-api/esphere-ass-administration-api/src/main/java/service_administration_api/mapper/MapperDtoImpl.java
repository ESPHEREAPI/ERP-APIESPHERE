/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service_administration_api.DTO.UserDTO;
import service_administration_api.DTO.UserSessionDTO;
import service_administration_api.PropertiesAdmin;
import service_administration_api.entite.PosteTravail;
import service_administration_api.exception.UserNotFoundException;
import service_administration_api.repository.PosteTravailRepository;
import service_administration_api.utils.JwtExpiration;

/**
 *
 * @author USER01
 */
@Service
public class MapperDtoImpl {

    @Autowired
    PropertiesAdmin properties;
    @Autowired
    PosteTravailRepository posteTravailRepository;

    public UserDTO mapUserSessionDTOByuserDTO(String usename) {
        PosteTravail p = posteTravailRepository.posteTravailByUsername(usename.toUpperCase());
        // Vérification si l'utilisateur existe
        if (p == null) {
            throw new UserNotFoundException("Utilisateur introuvable : " + usename);
        }

        // Création de l'objet UserDTO
        UserDTO userDTO = UserDTO.builder()
                .username(p.getNomUtil())
                .codeagence(p.getCodeinte())
                .nomcomplet(p.getNoefpotr())
                .profil(p.getCodpropr() == null ? 0 : p.getCodpropr().getCodeprof())
                .build();

        // Création de l'objet UserSessionDTO
        UserSessionDTO userSessionDTO = new UserSessionDTO();
        userSessionDTO.setUserDTO(userDTO);
        // userSessionDTO.setExpiresAt(JwtExpiration.expiresAt(properties.getJwtExpirationMs()));
        // System.out.println("expiration "+ userSessionDTO.getExpiresAt());
        // Génération du token JWT
        // userSessionDTO.setToken(
        //    JwtExpiration.generateJwtToken(userDTO.getUsername(), userSessionDTO.getExpiresAt())
        //  );

        return userDTO;
    }

}
