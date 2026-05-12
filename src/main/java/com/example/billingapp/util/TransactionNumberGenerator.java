package com.example.billingapp.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TransactionNumberGenerator {
    // Prefix
    private static final String PREFIX = "BLK";
    // Sequence generator for unique number within the same day
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private static LocalDate lastDate = LocalDate.now();

    public String generateTransactionId() {
        LocalDate now = LocalDate.now();

        // Reset sequence if the day has changed
        if (!now.equals(lastDate)) {
            sequence.set(1);
            lastDate = now;
        }

        // Format: BLK-yyyyMMdd-0001
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniqueId = String.format("%04d", sequence.getAndIncrement());

        return PREFIX + "-" + datePart + "-" + uniqueId;
    }

}
