package com.payment.system.payment_service.user;

import com.payment.system.common.dto.PaymentRequest;
import com.payment.system.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "user-service",
        url = "${application.config.user-url}"
)
public interface UserClient {
    @GetMapping("/{username}/balance")
    ApiResponse<Double> getUserBalance (@PathVariable("username") String username);
    
    @PostMapping("/deduct")
    Void deductBalance (@RequestBody PaymentRequest request,
                @RequestHeader("Idempotency-Key") String idempotencyKey);
    
}
