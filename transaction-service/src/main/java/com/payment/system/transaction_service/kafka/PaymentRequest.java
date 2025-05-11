package com.payment.system.transaction_service.kafka;

import lombok.Data;

@Data
public class PaymentRequest {
    private String username;
    private double amount;
}