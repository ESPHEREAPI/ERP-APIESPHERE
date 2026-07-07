package com.esphere.webservicecron.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Enveloppe de pagination simple, indépendante de org.springframework.data.domain.Page.
 * Page (et Pageable/Sort qu'elle expose) casse la génération du schéma
 * OpenAPI de springdoc (500 sur /v3/api-docs) — on expose donc nos propres
 * champs, sérialisables sans ambiguïté.
 */
@Data
@Builder
public class PageResponse<T> {
    private List<T> contenu;
    private int page;
    private int taille;
    private long totalElements;
    private int totalPages;
    private boolean dernierePage;

    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .contenu(page.getContent())
                .page(page.getNumber())
                .taille(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .dernierePage(page.isLast())
                .build();
    }
}
