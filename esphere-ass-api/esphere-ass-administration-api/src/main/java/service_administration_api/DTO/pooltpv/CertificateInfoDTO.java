package service_administration_api.DTO.pooltpv;

public record CertificateInfoDTO(
    String reference,
    String policeNumber,
    String licencePlate,
    String chassisNumber,
    String insuredName,
    String insuredPhone,
    String certTypeName,
    String certVariantName,
    String startsAt,
    String endsAt,
    String stateName,
    String stateLabel,
    String officeCode
) {}
