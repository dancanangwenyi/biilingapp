package com.example.billingapp.service;

import com.example.billingapp.dto.request.CreatePaymentRequestDTO;
import com.example.billingapp.dto.response.PaymentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    Page<PaymentResponseDTO> getPayments(Pageable pageable);

    PaymentResponseDTO createPayment(@Valid CreatePaymentRequestDTO request);

    PaymentResponseDTO getPaymentById(Long id);
}
