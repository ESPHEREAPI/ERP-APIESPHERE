/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.exception;

/**
 *
 * @author USER01
 */
// exception/CertificateNotFoundException.java
public class CertificateNotFoundException extends RuntimeException {
    public CertificateNotFoundException(String reference) {
        super("Certificat introuvable avec Reference : " + reference);
    }
     public CertificateNotFoundException(Long id) {
        super("Certificat introuvable avec L ID : " + id);
    }
}
