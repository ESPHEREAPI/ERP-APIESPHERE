package service_administration_api.DTO.pooltpv;

public record ActionResponse(
    int status,
    String message,
    String certificateReference,
    String typeAction,
    String motifCode,
    String motifLibelle
) {}
