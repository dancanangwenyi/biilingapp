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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Invoice Management", description = "Endpoints for managing invoices")
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
            summary = "Fetch overdue invoices",
            description = "Fetch overdue invoices",
            parameters = {
                    @Parameter(name = "customerId", description = "Customer id", in = ParameterIn.QUERY, required = true),
                    @Parameter(name = "startDate", description = "Start date", in = ParameterIn.QUERY, required = true),
                    @Parameter(name = "endDate", description = "End date", in = ParameterIn.QUERY, required = true)
            }
    )
    @GetMapping()
    public ResponseEntity<ApiResponse<List<InvoiceResponseDTO>>> getOverdueInvoices(
            @RequestParam Long customerId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        List<InvoiceResponseDTO> invoices =invoiceService.getOverdueInvoices(customerId, startDate, endDate);
        return wrap(invoices);
    }

    @Operation(
            summary = "Fetch total paid for invoice",
            description = "Fetch total paid for invoice",
            parameters = {
                    @Parameter(name = "invoiceId", description = "Invoice id", in = ParameterIn.QUERY, required = true)
            }
    )
    @GetMapping()
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalPaidForInvoice(
            @RequestParam Long invoiceId
    ) {
        BigDecimal amount =invoiceService.getTotalPaidForInvoice(invoiceId);
        return wrap(amount);
    }

    @Operation(
            summary = "Check if invoice is fully paid",
            description = "Check if invoice is fully paid",
            parameters = {
                    @Parameter(name = "invoiceId", description = "Invoice id", in = ParameterIn.QUERY, required = true)
            }
    )
    @GetMapping()
    public ResponseEntity<ApiResponse<Boolean>> checkIfInvoiceFullyPaid(
            @RequestParam Long invoiceId
    ) {
        Boolean amount =invoiceService.isInvoiceFullyPaid(invoiceId);
        return wrap(amount);
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
