package com.example.billingapp.mapper;

import com.example.billingapp.dto.response.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DashboardMapper {

    public DashboardSummaryResponseDTO toSummaryResponseDTO(
            Long totalCustomers,
            Long totalInvoices,
            BigDecimal totalAmountInvoiced,
            BigDecimal totalAmountPaid,
            BigDecimal outstandingBalance) {

        return DashboardSummaryResponseDTO.builder()
                .totalCustomers(totalCustomers)
                .totalInvoices(totalInvoices)
                .totalAmountInvoiced(totalAmountInvoiced)
                .totalAmountPaid(totalAmountPaid)
                .outstandingBalance(outstandingBalance)
                .build();
    }

    public TopCustomerResponseDTO toTopCustomerResponseDTO(String customerName, BigDecimal totalPaid) {
        return TopCustomerResponseDTO.builder()
                .customerName(customerName)
                .totalPaid(totalPaid)
                .build();
    }

    public MonthlyRevenueResponseDTO toMonthlyRevenueResponseDTO(String month, BigDecimal total) {
        return MonthlyRevenueResponseDTO.builder()
                .month(month)
                .total(total)
                .build();
    }

    public OverdueInvoiceResponseDTO toOverdueInvoiceResponseDTO(
            Long invoiceId,
            String invoiceNumber,
            String customerName,
            BigDecimal amount,
            BigDecimal amountPaid,
            BigDecimal balance,
            LocalDate dueDate,
            Long daysOverdue,
            String status) {

        return OverdueInvoiceResponseDTO.builder()
                .invoiceId(invoiceId)
                .invoiceNumber(invoiceNumber)
                .customerName(customerName)
                .amount(amount)
                .amountPaid(amountPaid)
                .balance(balance)
                .dueDate(dueDate)
                .daysOverdue(daysOverdue)
                .status(status)
                .build();
    }
}