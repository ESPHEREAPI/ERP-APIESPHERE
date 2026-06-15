/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER01
 */
@Component
@ConfigurationProperties("app")
@RefreshScope
@Data
public class PropertiesAdmin {

    private int limitDeProduits;
    private Long jwtExpirationMs;
}
