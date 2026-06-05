package com.esphere.media.util;

import com.esphere.media.exception.MediaException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class MediaValidator {

    // Tailles max en octets
    private static final long MAX_IMAGE    = 10L  * 1024 * 1024; // 10 MB
    private static final long MAX_DOCUMENT = 20L  * 1024 * 1024; // 20 MB
    private static final long MAX_VIDEO    = 100L * 1024 * 1024; // 100 MB
    private static final long MAX_AUTRE    = 10L  * 1024 * 1024; // 10 MB

    private static final Set<String> IMAGES    = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Set<String> DOCUMENTS = Set.of("pdf");
    private static final Set<String> VIDEOS    = Set.of("mp4", "avi", "mov", "mkv", "webm");

    public static String detecterTypeMedia(String extension) {
        String ext = extension.toLowerCase();
        if (IMAGES.contains(ext))    return "image";
        if (DOCUMENTS.contains(ext)) return "document";
        if (VIDEOS.contains(ext))    return "video";
        return "autre";
    }

    public static void valider(MultipartFile fichier, String extension) {
        if (fichier.isEmpty()) {
            throw new MediaException("Le fichier est vide.", 400);
        }

        String typeMedia = detecterTypeMedia(extension);
        long taille = fichier.getSize();

        switch (typeMedia) {
            case "image"    -> { if (taille > MAX_IMAGE)
                throw new MediaException("Image trop volumineuse. Max 10 MB.", 400); }
            case "document" -> { if (taille > MAX_DOCUMENT)
                throw new MediaException("Document trop volumineux. Max 20 MB.", 400); }
            case "video"    -> { if (taille > MAX_VIDEO)
                throw new MediaException("Vidéo trop volumineuse. Max 100 MB.", 400); }
            default         -> { if (taille > MAX_AUTRE)
                throw new MediaException("Fichier trop volumineux. Max 10 MB.", 400); }
        }
    }

    public static String extraireExtension(String nomFichier) {
        if (nomFichier == null || !nomFichier.contains(".")) {
            throw new MediaException("Extension de fichier manquante.", 400);
        }
        return nomFichier.substring(nomFichier.lastIndexOf('.') + 1).toLowerCase();
    }
}