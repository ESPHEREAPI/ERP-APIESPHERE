/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;

import db.biometry.biometry.BiometryApplication;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author USER01
 */
@RestController
@AllArgsConstructor
public class ShutdownRestController implements ApplicationContextAware {

    private ApplicationContext context;

     @PostMapping("biometry/arret")
    public void shutdownContext() {
        ((ConfigurableApplicationContext) context).close();
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
    
    @PostMapping("biometry/restart")
    public void restart() {
        BiometryApplication.restart();
    } 
}
