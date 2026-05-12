package com.example.billingapp.dto.request;

public record CreateCustomerRequestDTO(
        String name,
        String email,
        String phone
) {
}
