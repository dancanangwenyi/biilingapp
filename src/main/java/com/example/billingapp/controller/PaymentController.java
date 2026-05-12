package com.example.billingapp.controller;

import com.example.billingapp.dto.request.CreatePaymentRequestDTO;
import com.example.billingapp.dto.response.ApiResponse;
import com.example.billingapp.dto.response.PaymentResponseDTO;
import com.example.billingapp.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Management", description = "Endpoints for managing  payments")
public class PaymentController  extends BaseController{

    private final PaymentService paymentService;

    @Operation(
            summary = "Get all  payments",
            description = "Retrieve a paginated list of  payments",
            parameters = {
                    @Parameter(name = "page", description = "Page number (0-based)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Number of items per page", in = ParameterIn.QUERY),
                    @Parameter(name = "sort", description = "Sorting criteria (format: property,asc|desc)", in = ParameterIn.QUERY)
            }
    )
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<PaymentResponseDTO>>> getPayments(
            Pageable pageable
    ) {

        Page<PaymentResponseDTO>  payments = paymentService.getPayments(pageable);
        return wrap( payments);
    }


    @Operation(
            summary = "Create a new  payment",
            description = "Create a new  payment)"
    )
    @PostMapping()
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> createPayment(@Valid @RequestBody CreatePaymentRequestDTO request) {
        PaymentResponseDTO  paymentResponse = paymentService.createPayment(request);
        return wrap( paymentResponse);
    }

    @Operation(
            summary = "Fetch  payment by id",
            description = "Fetch  payment details by id)"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> getPaymentById(
            @Parameter(description = "Payment Id") @PathVariable Long id) {
        PaymentResponseDTO  paymentResponse = paymentService.getPaymentById(id);
        return wrap( paymentResponse);
    }

}
