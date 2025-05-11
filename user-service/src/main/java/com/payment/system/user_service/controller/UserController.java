package com.payment.system.user_service.controller;

import com.payment.system.common.dto.PaymentRequest;
import com.payment.system.common.response.ApiResponse;
import com.payment.system.user_service.dto.LoginRequest;
import com.payment.system.user_service.dto.UserRequest;
import com.payment.system.user_service.entity.User;
import com.payment.system.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody UserRequest request) throws RuntimeException {
        User user = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) throws RuntimeException {
        String token = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success(token));
    }
    
    @GetMapping("/{username}/balance")
    public ResponseEntity<ApiResponse<Double>> getBalance(@PathVariable String username) {
        Double balance = userService.getBalance(username);
        return ResponseEntity.ok(ApiResponse.success(balance));
    }
    
    @PostMapping("/deduct")
    public ResponseEntity<ApiResponse<Void>> deductBalance (@RequestBody PaymentRequest request,
                                                            @RequestHeader("Idempotency-Key") String idempotencyKey) throws RuntimeException {
        userService.deductBalance(request, idempotencyKey);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}