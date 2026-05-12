package com.example.billingapp.controller;


import com.example.billingapp.dto.response.ApiResponse;
import org.slf4j.MDC;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * Base controller to provide common helper methods for wrapping responses.
 */
public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> wrap(T data) {
        return ResponseEntity
                .ok()
                .header("X-Request-ID", MDC.get("requestId"))
                .body(new ApiResponse<>("success", data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> wrap(T data, HttpStatusCode status) {
        return ResponseEntity
                .status(status)
                .header("X-Request-ID", MDC.get("requestId"))
                .body(new ApiResponse<>("success", data));
    }
}
