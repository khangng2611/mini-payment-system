package com.payment.system.common.dto;

public record TransactionLogMessage(
    String transactionId,
    PaymentRequest request,
    long timestamp
) {
}
