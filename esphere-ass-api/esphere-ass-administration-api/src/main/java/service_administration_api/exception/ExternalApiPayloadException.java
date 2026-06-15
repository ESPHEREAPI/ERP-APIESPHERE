/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.exception;

/**
 *
 * @author USER01
 */
public class ExternalApiPayloadException extends RuntimeException {

    private final int httpStatus;
    private final ApiErrorPayloadResponse apiErrorResponse;

    public ExternalApiPayloadException(int httpStatus, ApiErrorPayloadResponse apiErrorResponse) {
        super(apiErrorResponse.message());
        this.httpStatus = httpStatus;
        this.apiErrorResponse = apiErrorResponse;
    }

    public int getHttpStatus() { return httpStatus; }
    public ApiErrorPayloadResponse getApiErrorResponse() { return apiErrorResponse; }
}