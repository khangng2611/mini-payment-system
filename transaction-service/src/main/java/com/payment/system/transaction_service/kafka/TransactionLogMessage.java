package com.payment.system.transaction_service.kafka;


public record TransactionLogMessage(
    String transactionId,
    PaymentRequest request
) {
}
