package service_administration_api.DTO.pooltpv;

import jakarta.validation.constraints.NotBlank;

public record SuspendCancelRequest(
    @NotBlank String motifCode
) {}
