package com.example.billingapp.repository;

import com.example.billingapp.enums.InvoiceStatus;
import com.example.billingapp.model.Customer;
import com.example.billingapp.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStatusInAndDueDateBefore(List<InvoiceStatus> pending, LocalDate today);

    List<Invoice> findByCustomerAndDueDateBeforeAndStatusNot(Customer customer, LocalDate today, InvoiceStatus invoiceStatus);

    List<Invoice> findByDueDateBeforeAndCreatedAtBetweenAndStatusNot(LocalDate today, LocalDateTime localDateTime, LocalDateTime localDateTime1, InvoiceStatus invoiceStatus);

    List<Invoice> findByDueDateBeforeAndStatusNot(LocalDate today, InvoiceStatus invoiceStatus);
}
