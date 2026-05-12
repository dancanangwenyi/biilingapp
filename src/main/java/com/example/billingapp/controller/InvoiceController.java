package com.example.billingapp.controller;

import com.example.billingapp.dto.request.CreateInvoiceRequestDTO;
import com.example.billingapp.dto.request.UpdateInvoiceRequestDTO;
import com.example.billingapp.dto.response.ApiResponse;
import com.example.billingapp.dto.response.InvoiceResponseDTO;
import com.example.billingapp.service.InvoiceService;
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
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Management", description = "Endpoints for managing invoices")
public class InvoiceController  extends BaseController{

    private final InvoiceService invoiceService;

    @Operation(
            summary = "Get all invoices",
            description = "Retrieve a paginated list of invoices",
            parameters = {
                    @Parameter(name = "page", description = "Page number (0-based)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Number of items per page", in = ParameterIn.QUERY),
                    @Parameter(name = "sort", description = "Sorting criteria (format: property,asc|desc)", in = ParameterIn.QUERY)
            }
    )
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<InvoiceResponseDTO>>> getInvoices(
            Pageable pageable
    ) {

        Page<InvoiceResponseDTO> invoices =invoiceService.getInvoices(pageable);
        return wrap(invoices);
    }

    @Operation(
            summary = "Create a new invoice",
            description = "Create a new invoice)"
    )
    @PostMapping()
    public ResponseEntity<ApiResponse<InvoiceResponseDTO>> createInvoice(@Valid @RequestBody CreateInvoiceRequestDTO request) {
        InvoiceResponseDTO invoiceResponse =invoiceService.createInvoice(request);
        return wrap(invoiceResponse);
    }

    @Operation(
            summary = "Update invoice",
            description = "Update invoice)"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponseDTO>> updateInvoiceById(
            @Parameter(description = "Invoice Id") @PathVariable Long id,
            @Valid @RequestBody UpdateInvoiceRequestDTO request) {
        InvoiceResponseDTO invoiceResponse =invoiceService.updateInvoiceById(id, request);
        return wrap(invoiceResponse);
    }

    @Operation(
            summary = "Fetch invoice by id",
            description = "Fetch invoice details by id)"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponseDTO>> getInvoiceById(
            @Parameter(description = "Invoice Id") @PathVariable Long id) {
        InvoiceResponseDTO invoiceResponse =invoiceService.getInvoiceById(id);
        return wrap(invoiceResponse);
    }

    @Operation(
            summary = "Delete Invoice by Id",
            description = "Delete Invoice by Id)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteInvoiceById(
            @Parameter(description = "Invoice Id") @PathVariable Long id) {
        String invoiceResponse =invoiceService.deleteInvoiceById(id);
        return wrap(invoiceResponse);
    }

}
