package com.example.billingapp.mapper;

import com.example.billingapp.dto.response.InvoiceResponseDTO;
import com.example.billingapp.model.Invoice;
import com.example.billingapp.model.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {

    public InvoiceResponseDTO toResponseDTO(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        BigDecimal totalPaid = invoice.getTotalPaid();
        BigDecimal outstandingBalance = invoice.getAmount().subtract(totalPaid);

        return InvoiceResponseDTO.builder()
                .id(invoice.getId())
                .customerId(invoice.getCustomer() == null ? null : invoice.getCustomer().getId())
                .amount(invoice.getAmount())
                .dueDate(invoice.getDueDate())
                .status(invoice.getStatus())
                .createdAt(invoice.getCreatedAt())
                .totalPaid(totalPaid)
                .outstandingBalance(outstandingBalance)
                .paymentIds(invoice.getPayments() == null ? null :
                        invoice.getPayments().stream()
                                .map(Payment::getId)
                                .collect(Collectors.toList()))
                .build();
    }
}
