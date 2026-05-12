package com.example.billingapp.service.impl;

import com.example.billingapp.dto.response.DashboardSummaryResponseDTO;
import com.example.billingapp.dto.response.TopCustomerResponseDTO;
import com.example.billingapp.mapper.DashboardMapper;
import com.example.billingapp.model.Invoice;
import com.example.billingapp.repository.CustomerRepository;
import com.example.billingapp.repository.InvoiceRepository;
import com.example.billingapp.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final PaymentServiceImpl paymentService;
    private final InvoiceServiceImpl invoiceService;
    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final DashboardMapper dashboardMapper;

    @Override
    public DashboardSummaryResponseDTO dashboardSummary() {
        Long totalCustomers = customerRepository.count();
        Long totalInvoices =invoiceRepository.count();
        BigDecimal totalAmountInvoiced = invoiceRepository.findAll().stream()
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmountPaid = invoiceRepository.findAll().stream()
                .map(Invoice::getTotalPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal outstandingBalance = totalAmountInvoiced.subtract(totalAmountPaid);


        return dashboardMapper.toSummaryResponseDTO(totalCustomers, totalInvoices, totalAmountInvoiced, totalAmountPaid, outstandingBalance);
    }

    @Override
    public TopCustomerResponseDTO dashboardTopCustomers() {
        return null;
    }
}
