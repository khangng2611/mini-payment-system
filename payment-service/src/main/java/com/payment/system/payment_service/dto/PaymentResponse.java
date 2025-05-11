package com.payment.system.payment_service.dto;

public record PaymentResponse(
        String transactionId,
        String status,
        double amount
) {}