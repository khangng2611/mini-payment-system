package com.payment.system.payment_service.dto;

public record PaymentRequest(
        String username,
        double amount
) {}