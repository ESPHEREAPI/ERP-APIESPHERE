/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry;

/**
 *
 * @author USER01
 */
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration du pool de threads asynchrone pour les emails.
 *
 * Dimensionnement conseillé :
 *  - corePoolSize  : 5  → threads toujours disponibles
 *  - maxPoolSize   : 20 → pics de charge
 *  - queueCapacity : 100 → file d'attente avant rejet
 *
 * Si la file est pleine → CallerRunsPolicy (l'appelant exécute la tâche lui-même)
 * garantissant qu'aucun email n'est silencieusement perdu.
 */
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "mailTaskExecutor")
    public Executor mailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("mail-async-");
        executor.setRejectedExecutionHandler(
                new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return mailTaskExecutor();
    }
    
}
