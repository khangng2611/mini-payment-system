package com.payment.system.payment_service.service;

import com.payment.system.payment_service.dto.DeductRequest;
import com.payment.system.payment_service.dto.PaymentRequest;
import com.payment.system.payment_service.dto.PaymentResponse;
import com.payment.system.payment_service.kafka.TransactionLogMessage;
import com.payment.system.payment_service.kafka.TransactionProducer;
import com.payment.system.payment_service.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentService {
    @Autowired
    private final UserClient userClient;
    @Autowired
    private final TransactionProducer transactionProducer;
    
    public PaymentResponse processPayment(PaymentRequest request, String transactionId, String idempotencyKey) {
        // Validate user balance (call User Service)
        Double balance = userClient.getUserBalance(request.username());
        
        if (balance != null && balance < request.amount()) {
            throw new RuntimeException("Insufficient balance");
        }
        
        // Update balance
        userClient.deduct(request.username(), new DeductRequest(request.amount()), idempotencyKey);
        
        // Log transaction asynchronously
        transactionProducer.sendTransaction(new TransactionLogMessage(transactionId, request));
        
        return new PaymentResponse(transactionId, "SUCCESS", request.amount());
    }
    
}