package com.example.billingapp.mapper;

import com.example.billingapp.dto.response.PaymentResponseDTO;
import com.example.billingapp.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponseDTO toResponseDTO(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .invoiceId(payment.getInvoice() == null ? null : payment.getInvoice().getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .transactionNumber(payment.getTransactionNumber())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
