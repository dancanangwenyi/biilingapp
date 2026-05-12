package com.example.billingapp.dto.response;


import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record MonthlyRevenueResponseDTO(
        String month,  // Format: YYYY-MM
        BigDecimal total
) {}
