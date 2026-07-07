package service_administration_api.repository.poolTPV;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import service_administration_api.entite.pooltpv.HistoriqueAction;

public interface HistoriqueActionRepository extends JpaRepository<HistoriqueAction, Long> {
    List<HistoriqueAction> findByCertificateReferenceOrderByDateActionDesc(String certificateReference);
    List<HistoriqueAction> findByUsernameExecutantOrderByDateActionDesc(String username);
    List<HistoriqueAction> findByOfficeCodeOrderByDateActionDesc(String officeCode);
    List<HistoriqueAction> findByOfficeCodeAndTypeActionOrderByDateActionDesc(String officeCode, String typeAction);
}
