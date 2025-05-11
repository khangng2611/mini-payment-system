package com.payment.system.payment_service.controller;

import com.payment.system.common.dto.PaymentRequest;
import com.payment.system.common.response.ApiResponse;
import com.payment.system.payment_service.dto.PaymentResponse;
import com.payment.system.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<PaymentResponse>> initiatePayment(@Valid @RequestBody PaymentRequest request,
                                                                        @RequestHeader("Idempotency-Key") String idempotencyKey) {
        String transactionId = UUID.randomUUID().toString();
        PaymentResponse response = paymentService.processPayment(request, transactionId, idempotencyKey);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}