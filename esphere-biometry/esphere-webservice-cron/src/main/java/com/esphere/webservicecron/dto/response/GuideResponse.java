package com.esphere.webservicecron.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GuideResponse {
    private String service;
    private String description;
    private String ordreRecommande;
    private List<EndpointDocResponse> endpoints;
}
