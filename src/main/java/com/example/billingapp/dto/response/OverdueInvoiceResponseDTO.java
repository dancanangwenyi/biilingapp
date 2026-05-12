package com.example.billingapp.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
public record OverdueInvoiceResponseDTO(
        Long invoiceId,
        String invoiceNumber,
        String customerName,
        BigDecimal amount,
        BigDecimal amountPaid,
        BigDecimal balance,
        LocalDate dueDate,
        Long daysOverdue,
        String status
) {}
