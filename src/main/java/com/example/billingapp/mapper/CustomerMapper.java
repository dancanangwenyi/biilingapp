package com.example.billingapp.mapper;

import com.example.billingapp.dto.response.CustomerResponseDTO;
import com.example.billingapp.model.Customer;
import com.example.billingapp.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public CustomerResponseDTO toResponseDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .createdAt(customer.getCreatedAt())
                .invoiceIds(customer.getInvoices() == null ? null :
                        customer.getInvoices().stream()
                                .map(Invoice::getId)
                                .collect(Collectors.toList()))
                .build();
    }
}
