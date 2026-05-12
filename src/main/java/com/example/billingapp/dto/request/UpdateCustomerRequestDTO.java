package com.example.billingapp.dto.request;

public record UpdateCustomerRequestDTO(
        String name,
        String email,
        String phone
) {
}
