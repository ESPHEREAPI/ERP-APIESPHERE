/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.auth.dto.request;
import java.time.Instant;

/**
 *
 * @author USER01
 */
 public record OtpEntry(
            String prestataireId,
            String codeVisite,
            String annee,
            String naturePrestation,
            Instant expiresAt
    ) {}