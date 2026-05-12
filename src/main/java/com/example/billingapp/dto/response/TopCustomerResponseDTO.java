package com.example.billingapp.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record TopCustomerResponseDTO(
        String customerName,
        BigDecimal totalPaid
) {}
