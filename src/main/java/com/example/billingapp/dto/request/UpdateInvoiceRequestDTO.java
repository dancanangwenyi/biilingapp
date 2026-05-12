package com.example.billingapp.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInvoiceRequestDTO (
        Long id,
        BigDecimal paymentAmount,
        LocalDate paymentDate
){
}
