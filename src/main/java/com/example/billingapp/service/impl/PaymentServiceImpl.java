package com.example.billingapp.service.impl;

import com.example.billingapp.dto.request.CreatePaymentRequestDTO;
import com.example.billingapp.dto.request.UpdateInvoiceRequestDTO;
import com.example.billingapp.dto.response.PaymentResponseDTO;
import com.example.billingapp.exception.BusinessRuleViolationException;
import com.example.billingapp.exception.ResourceNotFoundException;
import com.example.billingapp.mapper.PaymentMapper;
import com.example.billingapp.model.Invoice;
import com.example.billingapp.model.Payment;
import com.example.billingapp.repository.InvoiceRepository;
import com.example.billingapp.repository.PaymentRepository;
import com.example.billingapp.service.InvoiceService;
import com.example.billingapp.service.PaymentService;
import com.example.billingapp.util.TransactionNumberGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final TransactionNumberGenerator transactionNumberGenerator;
    private final InvoiceRepository invoiceRepository;



    @Override
    public Page<PaymentResponseDTO> getPayments(Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(paymentMapper::toResponseDTO);
    }

    @Transactional
    @Override
    public PaymentResponseDTO createPayment(CreatePaymentRequestDTO request) {

        Invoice invoice = invoiceRepository.findById(request.invoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + request.invoiceId()));

        String transactionNumber = transactionNumberGenerator.generateTransactionId();
        Optional<Payment> existingPayment = paymentRepository.findPaymentByTransactionNumber(transactionNumber);
        while(existingPayment.isPresent()){
            transactionNumber = transactionNumberGenerator.generateTransactionId();
            existingPayment = paymentRepository.findPaymentByTransactionNumber(transactionNumber);
        }

        // BEFORE LOGGING PAYMENT WE CALL THE INVOICE SERVICE TO CHECK IF THE INVOICE IS FULLY PAID
        UpdateInvoiceRequestDTO invoiceRequestDTO = UpdateInvoiceRequestDTO.builder()
                .id(request.invoiceId())
                .paymentAmount(request.amount())
                .paymentDate(request.paymentDate())
                .build();

        // Calculate total payments already made
        BigDecimal existingTotalPaid = invoice.getTotalPaid();

        BigDecimal paymentAmount = request.amount();
        LocalDate paymentDate = request.paymentDate();

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

        Payment payment = new Payment();
        payment.setPaymentMethod(request.paymentMethod());
        payment.setAmount(request.amount());
        payment.setInvoice(invoice);
        payment.setPaymentDate(request.paymentDate());
        payment.setTransactionNumber(transactionNumber);

        Payment saved = paymentRepository.save(payment);

        invoice.updateStatus();
        invoiceRepository.save(invoice);

        return paymentMapper.toResponseDTO(saved);
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if(payment.isEmpty()){
            throw new EntityNotFoundException("Payment not found with ID: " + id);
        }
        return paymentMapper.toResponseDTO(payment.get());
    }
}
