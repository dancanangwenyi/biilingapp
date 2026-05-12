package com.example.billingapp.dto.request;

import com.example.billingapp.enums.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
public record CreatePaymentRequestDTO(
        Long invoiceId,
        LocalDate paymentDate,
        BigDecimal amount,
        PaymentMethod paymentMethod
) {
}
