package com.example.billingapp.service;

import com.example.billingapp.dto.response.DashboardSummaryResponseDTO;
import com.example.billingapp.dto.response.TopCustomerResponseDTO;

public interface DashboardService {
    DashboardSummaryResponseDTO dashboardSummary();

    TopCustomerResponseDTO dashboardTopCustomers();
}
