package db.biometry.biometry;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Classe principale de l'application Spring Boot
 * Point d'entrée de l'application de gestion de couverture santé
 * 
 * @author JIATOU FRANCK
 * @version 1.0
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableDiscoveryClient
@RefreshScope
public class BiometryApplication {

    private static ConfigurableApplicationContext context;
  

    public static void main(String[] args) {
        context = SpringApplication.run(BiometryApplication.class, args);
        System.out.println("\n" +
                "==============================================\n" +
                "   Application de Gestion Couverture Santé   \n" +
                "          ESPACE SOUSCRIPTEUR v1.0           \n" +
                "==============================================\n" +
                "   Swagger UI: http://localhost:8080/swagger-ui.html\n" +
                "   API Docs: http://localhost:8080/api-docs\n" +
                "==============================================\n");
    }

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(BiometryApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
    
    @Bean
    CommandLineRunner start(){
        return  new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                try {
//                     File file=new File("C:\\Users\\USER01\\Downloads\\ADOM_PNUI.jpg")   ;     
//              downloadFile.downloadFile(file);


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
             
            }
        };
    }
    
   

}
