package com.example.billingapp.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record DashboardSummaryResponseDTO(
        Long totalCustomers,
        Long totalInvoices,
        BigDecimal totalAmountInvoiced,
        BigDecimal totalAmountPaid,
        BigDecimal outstandingBalance
) {}
