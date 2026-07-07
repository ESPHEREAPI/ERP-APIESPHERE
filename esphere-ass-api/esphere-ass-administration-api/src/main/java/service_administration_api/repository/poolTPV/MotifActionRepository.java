package service_administration_api.repository.poolTPV;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import service_administration_api.entite.pooltpv.MotifAction;

public interface MotifActionRepository extends JpaRepository<MotifAction, String> {
    List<MotifAction> findByTypeActionAndActif(String typeAction, Integer actif);
    List<MotifAction> findByActif(Integer actif);
}
