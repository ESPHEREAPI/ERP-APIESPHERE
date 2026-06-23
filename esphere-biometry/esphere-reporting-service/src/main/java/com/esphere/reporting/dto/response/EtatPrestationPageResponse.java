package com.esphere.reporting.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtatPrestationPageResponse {

    private List<EtatPrestationItem> content;
    private long    totalElements;
    private int     totalPages;
    private int     currentPage;
    private int     pageSize;
    private double  montantSoumisTotalPage;
    private double  montantValideTotalPage;
}
