package service_administration_api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.EnableRetry;   // ← import manquant
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import service_administration_api.service.CertificateService;
import service_administration_api.service.PoolTPVService;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableConfigurationProperties
@EnableDiscoveryClient
@EnableAsync
@EnableRetry                // ← maintenant reconnu
@RequiredArgsConstructor
@Slf4j
public class ServiceAdministrationApiApplication {

    private final PoolTPVService poolTPVService;
    private final CertificateService certificateService;

    public static void main(String[] args) {
        SpringApplication.run(ServiceAdministrationApiApplication.class, args);
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        charger("listeDurees",   () -> poolTPVService.listeDurees("duree"));
        charger("listCategorie", () -> poolTPVService.listCategorie("categorie"));
        charger("listCivilite",  () -> poolTPVService.listCivilite("civilite"));
        charger("listEnergie",   () -> poolTPVService.listEnergie_PoolTPVs("energie"));
        charger("listGaranties", () -> poolTPVService.listGaranties("garantie"));
        charger("listGenres",    () -> poolTPVService.listGenres("genre"));
        charger("infos agence",  () -> certificateService.create("01KN4FA2FPV4FNQ96SDDC0J2Q0"));
    }

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    private void charger(String nom, ThrowingSupplier<List<?>> supplier) {
        try {
            List<?> result = supplier.get();
            log.info("[{}] {} éléments chargés", nom, result.size());
        } catch (Exception e) {
            log.warn("[{}] Chargement échoué : {}", nom, e.getMessage());
        }
    }
}