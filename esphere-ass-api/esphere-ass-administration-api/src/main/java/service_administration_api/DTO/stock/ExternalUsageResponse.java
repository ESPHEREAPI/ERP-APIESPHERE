package service_administration_api.DTO.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Réponse de l'endpoint ppeatt-api GET /api/v1/transactions/statistics/usage
 * Format RFC standard retourné par l'API externe.
 */
public record ExternalUsageResponse(
        int status,
        String message,
        ExternalUsageData data
) {
    public record ExternalUsageData(
            List<ExternalOfficeUsage> items,
            @JsonProperty("total_attributed") int totalAttributed,
            @JsonProperty("total_used")       int totalUsed,
            @JsonProperty("total_available")  int totalAvailable
    ) {}

    public record ExternalOfficeUsage(
            @JsonProperty("organization_code") String organizationCode,
            @JsonProperty("office_code")       String officeCode,
            String name,
            List<ExternalCertTypeUsage> values
    ) {}

    public record ExternalCertTypeUsage(
            @JsonProperty("certificate_type") String certificateType,
            ExternalUsageDetails details
    ) {}

    public record ExternalUsageDetails(
            int attributed,
            int used,
            int available
    ) {}
}
