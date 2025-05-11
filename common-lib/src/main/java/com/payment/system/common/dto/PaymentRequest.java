package com.payment.system.common.dto;

public record PaymentRequest (
    String username,
    double amount
){}