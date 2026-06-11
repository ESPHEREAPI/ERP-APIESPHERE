/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dtos.ActivationResponse;
import db.biometry.biometry.dtos.CreateSubscriberRequest;
import db.biometry.biometry.dtos.PagedResponse;
import db.biometry.biometry.dtos.PolicyCheckResponse;
import db.biometry.biometry.dtos.SendActivationRequest;
import db.biometry.biometry.dtos.SetPasswordRequest;
import db.biometry.biometry.dtos.SubscriberResponse;
import db.biometry.biometry.dtos.UpdateSubscriberRequest;

/**
 *
 * @author USER01
 */
public interface SubscriberService {
    // ── CRUD ──────────────────────────────────────────────────────────────────
    SubscriberResponse create(CreateSubscriberRequest request);

    SubscriberResponse findById(Long id);

    PagedResponse<SubscriberResponse> findAll(String search, Boolean isActive, int page, int limit);

    SubscriberResponse update(Long id, UpdateSubscriberRequest request);

    void delete(Long id);

    // ── Statut ────────────────────────────────────────────────────────────────
    SubscriberResponse toggleStatus(Long id, boolean isActive);

    // ── Vérification police ───────────────────────────────────────────────────
    PolicyCheckResponse checkPolicy(String policyNumber);

    // ── Activation par lien ───────────────────────────────────────────────────
    ActivationResponse sendActivationLink(Long subscriberId, SendActivationRequest request);

    SubscriberResponse setPasswordViaToken(SetPasswordRequest request);

    // ── Réinitialisation mot de passe (admin) ─────────────────────────────────
    SubscriberResponse resetPassword(Long id, String newPassword);
    SubscriberResponse resetPassword(String email, String newPassword);
    // ── Disponibilité username ────────────────────────────────────────────────
    boolean isUsernameAvailable(String username, Long excludeId);
}
