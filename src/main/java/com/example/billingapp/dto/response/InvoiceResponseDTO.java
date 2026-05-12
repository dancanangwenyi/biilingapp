package com.example.billingapp.dto.response;

import com.example.billingapp.enums.InvoiceStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record InvoiceResponseDTO(
        Long id,
        Long customerId,  // Customer ID instead of full customer object
        BigDecimal amount,
        LocalDate dueDate,
        InvoiceStatus status,
        LocalDateTime createdAt,
        BigDecimal totalPaid,
        BigDecimal outstandingBalance,
        List<Long> paymentIds  // List of payment IDs instead of full payments
) {}

