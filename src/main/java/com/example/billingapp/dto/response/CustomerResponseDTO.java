package com.example.billingapp.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record CustomerResponseDTO(
        Long id,
        String name,
        String email,
        String phone,
        LocalDateTime createdAt,
        List<Long> invoiceIds  // List of invoice IDs instead of full invoices
) {}