package com.payment.system.payment_service.kafka;

import com.payment.system.payment_service.dto.PaymentRequest;

public record TransactionLogMessage(
    String transactionId,
    PaymentRequest request
) {
}
