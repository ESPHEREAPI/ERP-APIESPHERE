/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.exceptions;


/**
 * Exception levée lorsqu'une ressource demandée n'est pas trouvée
 * 
 * @author JIATOU FRANCK
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }    
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " avec l'ID " + id + " introuvable");
    }
    public ResourceNotFoundException(String resource, String email) {
        super(resource + " avec l'Email " + email + " introuvable");
    }
}
