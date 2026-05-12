package com.example.billingapp.dto.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
public record UpdateInvoiceRequestDTO (
        Long id,
        BigDecimal paymentAmount,
        LocalDate paymentDate
){
}
