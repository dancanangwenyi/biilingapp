package com.example.billingapp.controller;

import com.example.billingapp.dto.request.CreateCustomerRequestDTO;
import com.example.billingapp.dto.request.UpdateCustomerRequestDTO;
import com.example.billingapp.dto.response.ApiResponse;
import com.example.billingapp.dto.response.CustomerResponseDTO;
import com.example.billingapp.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customers Management", description = "Endpoints for managing customers")
public class CustomerController  extends BaseController{

    private final CustomerService customerService;

    @Operation(
            summary = "Get all customers",
            description = "Retrieve a paginated list of customers",
            parameters = {
                    @Parameter(name = "page", description = "Page number (0-based)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Number of items per page", in = ParameterIn.QUERY),
                    @Parameter(name = "sort", description = "Sorting criteria (format: property,asc|desc)", in = ParameterIn.QUERY)
            }
    )
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<CustomerResponseDTO>>> getCustomers(
            Pageable pageable
    ) {

        Page<CustomerResponseDTO> customers =customerService.getCustomers(pageable);
        return wrap(customers);
    }

    @Operation(
            summary = "Create a new customer",
            description = "Create a new customer)"
    )
    @PostMapping()
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO request) {
        CustomerResponseDTO customerResponse =customerService.createCustomer(request);
        return wrap(customerResponse);
    }

    @Operation(
            summary = "Update customer",
            description = "Update customer)"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomerById(
            @Parameter(description = "Customer Id") @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequestDTO request) {
        CustomerResponseDTO customerResponse =customerService.updateCustomerById(id, request);
        return wrap(customerResponse);
    }

    @Operation(
            summary = "Fetch customer by id",
            description = "Fetch customer details by id)"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(
            @Parameter(description = "Customer Id") @PathVariable Long id) {
        CustomerResponseDTO customerResponse =customerService.getCustomerById(id);
        return wrap(customerResponse);
    }

    @Operation(
            summary = "Delete Customer by Id",
            description = "Delete Customer by Id)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomerById(
            @Parameter(description = "Customer Id") @PathVariable Long id) {
        String customerResponse =customerService.deleteCustomerById(id);
        return wrap(customerResponse);
    }

}
