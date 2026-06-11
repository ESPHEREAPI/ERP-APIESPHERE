/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

/**
 *
 * @author USER01
 */

import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriberResponse {
    private Long id;
    private String policyNumber;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String username;
    private boolean active;
    private String passwordMode;
    private Instant activationSentAt;
    private Instant activationTokenExpiry;
    private Integer activationDurationHrs;
    private boolean activationTokenValid;
    private Instant accountActivatedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Date echeance;
    private Date effet;
    
}
