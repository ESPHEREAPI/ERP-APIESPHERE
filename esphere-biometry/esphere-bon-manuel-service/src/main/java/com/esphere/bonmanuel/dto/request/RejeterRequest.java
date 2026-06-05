package com.esphere.bonmanuel.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejeterRequest {
    private String  observations;
    private Integer employeId;
}