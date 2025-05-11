package com.payment.system.user_service.controller;

import com.payment.system.user_service.dto.DeductRequest;
import com.payment.system.user_service.dto.LoginRequest;
import com.payment.system.user_service.dto.UserRequest;
import com.payment.system.user_service.entity.User;
import com.payment.system.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest request) {
        try {
            User user = userService.register(request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(userService.login(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @GetMapping("/{username}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable String username) {
        return ResponseEntity.ok(userService.getBalance(username));
    }
    
    @PostMapping("/{username}/deduct")
    public ResponseEntity<String> deductBalance(@PathVariable String username,
                                              @RequestBody DeductRequest deductRequest,
                                              @RequestHeader("Idempotency-Key") String idempotencyKey) {
        try {
            userService.deductBalance(username, deductRequest.getAmount(), idempotencyKey);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}