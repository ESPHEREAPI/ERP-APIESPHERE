package com.esphere.webservicecron.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EndpointDocResponse {
    private String methode;
    private String chemin;
    private String description;
    private List<String> parametres;
    private String exemple;
}
