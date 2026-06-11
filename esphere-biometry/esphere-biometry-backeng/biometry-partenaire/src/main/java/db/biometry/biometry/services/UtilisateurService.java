/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dtos.UserDTO;
import db.biometry.biometry.dtos.UserLogin;
import db.biometry.biometry.exceptions.UtilisateurException;
import java.util.List;

/**
 *
 * @author JIATOU FRANCK
 */
public interface UtilisateurService {
    UserDTO findUserByLogin(UserLogin userLog);
     UserDTO findSouscripteurByLogin(UserLogin userLog);
      UserDTO findAdherentByLogin(UserLogin userLog);
    List<UserDTO>listeUtilisateur();
    UserDTO getUser(int userId)throws UtilisateurException;
    List<UserDTO>seacrhUsers(String keyword);
    // public UserDTO authetification(UserLogin loginRequest);
}
