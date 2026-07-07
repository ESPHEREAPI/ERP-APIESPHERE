/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository.poolTPV;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import service_administration_api.entite.pooltpv.CertificatePlayLoad;

/**
 *
 * @author USER01
 */
// repository/CertificateRepository.java
@Repository
public interface CertificateRepository extends JpaRepository<CertificatePlayLoad, Long> {

    // Trouve tous les certificats d'une production
    List<CertificatePlayLoad> findByProductionId(Long productionId);

    // Trouve un certificat par sa référence externe
    Optional<CertificatePlayLoad> findByReference(String reference);

    // Certificats dont le PDF n'a pas encore été téléchargé
    @Query("SELECT c FROM CertificatePlayLoad c WHERE c.pdfBytes IS NULL")
    List<CertificatePlayLoad> findCertificatesWithoutPdf();
    
    // Trouve tous les certificats d'une police number
    List<CertificatePlayLoad> findByPoliceNumber(String policeNumber);

    // Trouve tous les certificats d'une police (insensible à la casse)
    @Query(value = "SELECT * FROM ZEN_CERTIFICATES_PAYLOAD WHERE UPPER(POLICE_NUMBER) = UPPER(:policeNumber) ORDER BY ID DESC", nativeQuery = true)
    List<CertificatePlayLoad> findByPoliceNumberIgnoreCase(
            @org.springframework.data.repository.query.Param("policeNumber") String policeNumber);

    // Trouve les certificats d'une plaque d'immatriculation
    List<CertificatePlayLoad> findByLicencePlate(String licencePlate);

    // Flotte : police + immatriculation (insensible à la casse, Oracle 11g compatible)
    @Query(value = "SELECT * FROM (SELECT * FROM ZEN_CERTIFICATES_PAYLOAD WHERE UPPER(POLICE_NUMBER) = UPPER(:policeNumber) AND UPPER(LICENCE_PLATE) = UPPER(:licencePlate) ORDER BY ID DESC) WHERE ROWNUM = 1", nativeQuery = true)
    Optional<CertificatePlayLoad> findFirstByPoliceNumberAndLicencePlate(
            @org.springframework.data.repository.query.Param("policeNumber") String policeNumber,
            @org.springframework.data.repository.query.Param("licencePlate") String licencePlate);

    // Mono : premier certificat d'une police (insensible à la casse, Oracle 11g compatible)
    @Query(value = "SELECT * FROM (SELECT * FROM ZEN_CERTIFICATES_PAYLOAD WHERE UPPER(POLICE_NUMBER) = UPPER(:policeNumber) ORDER BY ID DESC) WHERE ROWNUM = 1", nativeQuery = true)
    Optional<CertificatePlayLoad> findFirstByPoliceNumberOrderByIdDesc(
            @org.springframework.data.repository.query.Param("policeNumber") String policeNumber);

    // Premier certificat d'une plaque (insensible à la casse, Oracle 11g compatible)
    @Query(value = "SELECT * FROM (SELECT * FROM ZEN_CERTIFICATES_PAYLOAD WHERE UPPER(LICENCE_PLATE) = UPPER(:licencePlate) ORDER BY ID DESC) WHERE ROWNUM = 1", nativeQuery = true)
    Optional<CertificatePlayLoad> findFirstByLicencePlateOrderByIdDesc(
            @org.springframework.data.repository.query.Param("licencePlate") String licencePlate);
}
