package com.example.billingapp.service;

import com.example.billingapp.dto.request.CreateInvoiceRequestDTO;
import com.example.billingapp.dto.request.UpdateInvoiceRequestDTO;
import com.example.billingapp.dto.response.InvoiceResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {
    Page<InvoiceResponseDTO> getInvoices(Pageable pageable);

    InvoiceResponseDTO createInvoice(@Valid CreateInvoiceRequestDTO request);

    InvoiceResponseDTO updateInvoiceById(Long id, @Valid UpdateInvoiceRequestDTO request);

    InvoiceResponseDTO getInvoiceById(Long id);

    String deleteInvoiceById(Long id);

    @Transactional(readOnly = true)
    List<InvoiceResponseDTO> getOverdueInvoices(Long customerId, LocalDate startDate, LocalDate endDate);

    @Transactional(readOnly = true)
    BigDecimal getTotalPaidForInvoice(Long invoiceId);

    @Transactional(readOnly = true)
    boolean isInvoiceFullyPaid(Long invoiceId);
}
