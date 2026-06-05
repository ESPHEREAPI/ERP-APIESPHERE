package com.esphere.media.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StorageConfig {

    @Value("${media.storage.base-path:C:/biometry-media}")
    private String basePath;

    public String getBasePath() {
        return basePath;
    }

    public Path getBaseDirectory() {
        return Paths.get(basePath);
    }

    public Path getDossierAdherent(String souscripteur, String police, String codeAdherent) {
        return Paths.get(basePath, souscripteur, police, codeAdherent);
    }

    public void creerDossierSiAbsent(Path dossier) throws IOException {
        if (!Files.exists(dossier)) {
            Files.createDirectories(dossier);
        }
    }
}