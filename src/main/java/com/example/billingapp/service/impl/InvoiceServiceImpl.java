package com.example.billingapp.service.impl;

import com.example.billingapp.dto.request.CreateInvoiceRequestDTO;
import com.example.billingapp.dto.request.UpdateInvoiceRequestDTO;
import com.example.billingapp.dto.response.InvoiceResponseDTO;
import com.example.billingapp.enums.InvoiceStatus;
import com.example.billingapp.exception.BusinessRuleViolationException;
import com.example.billingapp.exception.ResourceNotFoundException;
import com.example.billingapp.mapper.InvoiceMapper;
import com.example.billingapp.model.Customer;
import com.example.billingapp.model.Invoice;
import com.example.billingapp.repository.CustomerRepository;
import com.example.billingapp.repository.InvoiceRepository;
import com.example.billingapp.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final InvoiceMapper invoiceMapper;
    private final PaymentServiceImpl paymentService;

 
    @Override
    @Transactional
    public InvoiceResponseDTO createInvoice(CreateInvoiceRequestDTO request) {
        
        log.info("Creating invoice for customer ID: {}", request.customerId());

        // Invoice cannot be created without a valid existing customer
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + request.customerId()));

        // Amount must be positive and non-zero
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleViolationException(
                    "Invoice amount must be positive and greater than zero. Provided: " + request.amount());
        }

        // dueDate must be in the future
        if (request.dueDate() == null || request.dueDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleViolationException(
                    "Due date must be in the future. Provided: " + request.dueDate());
        }
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setAmount(request.amount());
        invoice.setDueDate(request.dueDate());
        invoice.setStatus(InvoiceStatus.PENDING);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice created successfully with ID: {}", savedInvoice.getId());

        return invoiceMapper.toResponseDTO(savedInvoice);
    }

    @Override
    public void updateInvoiceById(Long invoiceId, UpdateInvoiceRequestDTO request) {

        BigDecimal paymentAmount = request.paymentAmount();
        LocalDate paymentDate = request.paymentDate();

        log.info("Processing payment of {} for invoice ID: {}", paymentAmount, invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + invoiceId));

        // Calculate total payments already made
        BigDecimal existingTotalPaid = invoice.getTotalPaid();

        // Payment amount must be positive
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleViolationException(
                    "Payment amount must be positive. Provided: " + paymentAmount);
        }

        // Payment's date must be on or before current date
        if (paymentDate != null && paymentDate.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException(
                    "Payment date cannot be in the future. Provided: " + paymentDate);
        }

        // Total payments + previous payments must not exceed invoice amount
        BigDecimal newTotalPaid = existingTotalPaid.add(paymentAmount);
        if (newTotalPaid.compareTo(invoice.getAmount()) > 0) {
            throw new BusinessRuleViolationException(
                    String.format("Payment would exceed invoice amount. " +
                                    "Invoice amount: %.2f, Already paid: %.2f, Attempting to pay: %.2f",
                            invoice.getAmount(), existingTotalPaid, paymentAmount));
        }

        invoice.updateStatus();
        invoiceRepository.save(invoice);
        log.info("Invoice {} status updated to: {}", invoice.getId(), invoice.getStatus());
    }


    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponseDTO> getInvoices(Pageable pageable) {
        Page<Invoice> invoices =  invoiceRepository.findAll(pageable);
        invoices.forEach(Invoice::updateStatus);
        return invoices.map(invoiceMapper::toResponseDTO);
        
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));
        return invoiceMapper.toResponseDTO(invoice);
    }


    @Override
    @Transactional
    public String deleteInvoiceById(Long id) {
        log.info("Attempting to delete invoice with ID: {}", id);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));

        // An invoice with payments cannot be deleted
        if (invoice.getPayments() != null && !invoice.getPayments().isEmpty()) {
            throw new BusinessRuleViolationException(
                    "Cannot delete invoice with ID: " + id + " because it has associated payments. " +
                            "Total payments found: " + invoice.getPayments().size());
        }

        invoiceRepository.delete(invoice);
        log.info("Invoice deleted successfully with ID: {}", id);
        return "Invoice deleted successfully";
    }
    

    @Transactional
    public void updateOverdueInvoices() {
        log.info("Checking for overdue invoices");
        LocalDate today = LocalDate.now();
        List<Invoice> pendingAndPartialInvoices = invoiceRepository.findByStatusInAndDueDateBefore(
                List.of(InvoiceStatus.PENDING, InvoiceStatus.PARTIALLY_PAID), today);
        for (Invoice invoice : pendingAndPartialInvoices) {
            log.info("Invoice {} is overdue. Due date: {}, Status: {}",
                    invoice.getId(), invoice.getDueDate(), invoice.getStatus());
             invoice.setStatus(InvoiceStatus.OVERDUE);
             invoiceRepository.save(invoice);
        }
    }


    @Transactional(readOnly = true)
    @Override
    public List<InvoiceResponseDTO> getOverdueInvoices(Long customerId, LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        List<Invoice> overdueInvoices;
        if (customerId != null) {
            // Filter by specific customer
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
            overdueInvoices = invoiceRepository.findByCustomerAndDueDateBeforeAndStatusNot(
                    customer, today, InvoiceStatus.PAID);
        } else if (startDate != null && endDate != null) {
            // Filter by creation date range
            overdueInvoices = invoiceRepository.findByDueDateBeforeAndCreatedAtBetweenAndStatusNot(
                    today, startDate.atStartOfDay(), endDate.atTime(23, 59, 59), InvoiceStatus.PAID);
        } else {
            // All overdue invoices
            overdueInvoices = invoiceRepository.findByDueDateBeforeAndStatusNot(today, InvoiceStatus.PAID);
        }

        return overdueInvoices.stream()
                .map(invoiceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalPaidForInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + invoiceId));
        return invoice.getTotalPaid();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isInvoiceFullyPaid(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + invoiceId));
        return invoice.getTotalPaid().compareTo(invoice.getAmount()) >= 0;
    }
}