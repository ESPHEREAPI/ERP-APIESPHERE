/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USER01
 */
public class RoleDTO {
     @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    /**
     * Constructeur simplifié
     */
    public RoleDTO(String name) {
        this.name = name;
    }
    
    /**
     * Constructeur avec nom et description
     */
    public RoleDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
