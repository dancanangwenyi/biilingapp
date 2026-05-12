package com.example.billingapp.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateInvoiceRequestDTO(
        Long customerId,
        BigDecimal amount,
        LocalDate dueDate
) {
}
