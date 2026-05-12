package com.example.billingapp.repository;


import com.example.billingapp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findPaymentByTransactionNumber(String transactionNumber);
}
