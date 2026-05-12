package com.example.billingapp.dto.response;

import com.example.billingapp.enums.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record PaymentResponseDTO(
        Long id,
        Long invoiceId,  // Invoice ID instead of full invoice object
        LocalDate paymentDate,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String transactionNumber,
        LocalDateTime createdAt
) {}