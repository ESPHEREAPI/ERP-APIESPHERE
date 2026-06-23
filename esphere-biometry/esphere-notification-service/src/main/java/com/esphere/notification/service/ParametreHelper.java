package com.esphere.notification.service;

import com.esphere.notification.client.ParametreClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParametreHelper {

    private final ParametreClient parametreClient;

    public boolean getBoolean(String cle, boolean defaut) {
        try {
            Map<String, String> result = parametreClient.getParametre(cle);
            return "true".equalsIgnoreCase(result.get("valeur"));
        } catch (Exception e) {
            log.warn("Impossible de lire le paramètre {} : {} — défaut={}", cle, e.getMessage(), defaut);
            return defaut;
        }
    }

    public String getString(String cle, String defaut) {
        try {
            Map<String, String> result = parametreClient.getParametre(cle);
            String val = result.get("valeur");
            return (val != null && !val.isBlank()) ? val : defaut;
        } catch (Exception e) {
            log.warn("Impossible de lire le paramètre {} — défaut={}", cle, defaut);
            return defaut;
        }
    }

    public int getInt(String cle, int defaut) {
        try {
            Map<String, String> result = parametreClient.getParametre(cle);
            return Integer.parseInt(result.get("valeur"));
        } catch (Exception e) {
            return defaut;
        }
    }
}
