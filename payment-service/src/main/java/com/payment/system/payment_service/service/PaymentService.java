package com.payment.system.payment_service.service;

import com.payment.system.common.dto.PaymentRequest;
import com.payment.system.common.dto.TransactionLogMessage;
import com.payment.system.common.response.ApiResponse;
import com.payment.system.payment_service.dto.PaymentResponse;
import com.payment.system.payment_service.kafka.TransactionProducer;
import com.payment.system.payment_service.user.UserClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        Double balance = userClient.getUserBalance(request.username()).getData();
        
        if (balance != null && balance.doubleValue() < request.amount()) {
            throw new RuntimeException("Insufficient balance");
        }
        
        // Update balance
        userClient.deductBalance(request, idempotencyKey);
        
        // Log transaction asynchronously
        transactionProducer.sendTransaction(new TransactionLogMessage(transactionId, request, System.currentTimeMillis()));
        
        return new PaymentResponse(transactionId, "SUCCESS", request.amount());
        
    }
    
}