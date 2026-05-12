package com.example.billingapp.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import org.slf4j.MDC;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Wrapper class for all API responses.
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiResponse<T>(
        String message,
        T data,
        String requestId,
        ZonedDateTime timestamp
) {
    public ApiResponse(String message, T data) {
        this(message, data, MDC.get("requestId"), ZonedDateTime.now(ZoneId.of("UTC")));
    }

    public boolean isSuccess(ApiResponse<?> response) {
        return response != null && "success".equalsIgnoreCase(response.message());
    }

    /**
     * Check if the API response is successful
     */
    public boolean isSuccess() {
        return "success".equalsIgnoreCase(this.message);
    }
}
