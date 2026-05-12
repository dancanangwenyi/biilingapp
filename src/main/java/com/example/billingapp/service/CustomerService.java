package com.example.billingapp.service;

import com.example.billingapp.dto.request.CreateCustomerRequestDTO;
import com.example.billingapp.dto.request.UpdateCustomerRequestDTO;
import com.example.billingapp.dto.response.CustomerResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<CustomerResponseDTO> getCustomers(Pageable pageable);

    CustomerResponseDTO createCustomer(@Valid CreateCustomerRequestDTO request);

    CustomerResponseDTO updateCustomerById(Long id, @Valid UpdateCustomerRequestDTO request);

    CustomerResponseDTO getCustomerById(Long id);

    String deleteCustomerById(Long id);
}
