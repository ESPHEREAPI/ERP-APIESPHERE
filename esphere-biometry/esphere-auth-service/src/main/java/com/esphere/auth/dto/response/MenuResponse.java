package com.esphere.auth.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {

    private Integer id;
    private Integer pereId;
    private String  nomModule;
    private String  nomAction;
    private String  nomControlleur;
    private String  classImage;
    private Short   numeroOrdre;    // Short car SMALLINT en base
    private String  type;
    private String  apparaitNav;
    private String  apparaitNavBar;

    private List<MenuResponse> sousMenus;
}