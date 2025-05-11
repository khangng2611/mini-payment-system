package com.payment.system.user_service.controller;

import com.payment.system.common.response.ApiResponse;
import com.payment.system.user_service.dto.DeductRequest;
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
    
    @PostMapping("/{username}/deduct")
    public ResponseEntity<ApiResponse<Void>> deductBalance(@PathVariable String username,
                                              @RequestBody DeductRequest deductRequest,
                                              @RequestHeader("Idempotency-Key") String idempotencyKey) throws RuntimeException {
        userService.deductBalance(username, deductRequest.getAmount(), idempotencyKey);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}