package com.payment.system.payment_service.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.payment.system.payment_service.dto.DeductRequest;

@FeignClient(
        name = "user-service",
        url = "${application.config.user-url}"
)
public interface UserClient {
    @GetMapping("/{username}/balance")
    Double getUserBalance (@PathVariable("username") String username);
    
    @PostMapping("/{username}/deduct")
    Void deduct(@PathVariable("username") String username,
                @RequestBody DeductRequest deductRequest,
                @RequestHeader("Idempotency-Key") String idempotencyKey);
    
}
