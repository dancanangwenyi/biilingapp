package com.example.billingapp.controller;

import com.example.billingapp.dto.response.ApiResponse;
import com.example.billingapp.dto.response.DashboardSummaryResponseDTO;
import com.example.billingapp.dto.response.TopCustomerResponseDTO;
import com.example.billingapp.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard Management", description = "Endpoints for managing dashboard")
public class DashboardController extends BaseController{

    private final DashboardService dashboardService;

    @Operation(
            summary = "Dashboard Summary",
            description = "Dashboard Summary)"
    )
    @PutMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponseDTO>> dashboardSummary() {
        DashboardSummaryResponseDTO dashboardSummary = dashboardService.dashboardSummary();
        return wrap(dashboardSummary);
    }

    @Operation(
            summary = "Dashboard top-customers",
            description = "Dashboard top-customers)"
    )
    @PutMapping("/top-customers")
    public ResponseEntity<ApiResponse<TopCustomerResponseDTO>> dashboardTopCustomers() {
        TopCustomerResponseDTO dashboardTopCustomers = dashboardService.dashboardTopCustomers();
        return wrap(dashboardTopCustomers);
    }

}
