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
}
