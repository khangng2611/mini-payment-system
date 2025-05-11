package com.payment.system.payment_service.kafka;

import com.payment.system.payment_service.dto.PaymentRequest;
import lombok.Data;

public record TransactionLogMessage(
    String transactionId,
    PaymentRequest request
) {
}
