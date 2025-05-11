package com.payment.system.user_service.service;

import com.payment.system.user_service.dto.LoginRequest;
import com.payment.system.user_service.dto.UserRequest;
import com.payment.system.user_service.entity.User;
import com.payment.system.user_service.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.OptimisticLockException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.concurrent.TimeUnit;
import java.util.Date;

@Service
public class UserService {
    
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    public User register(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(1000.0); // Default balance
        return userRepository.save(user);
    }
    
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        try {
            byte[] keyBytes = Hex.decodeHex(jwtSecret);
            Key signingKey = Keys.hmacShaKeyFor(keyBytes);
            return Jwts.builder()
                    .setSubject(request.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(signingKey, SignatureAlgorithm.HS512)
                    .compact();
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }
    
    public double getBalance(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getBalance();
    }
    
    @Transactional
    public void deductBalance(String username, double amount, String idempotencyKey) {
        // Check idempotency
        String redisKey = "idempotency:user:" + username + ":" + idempotencyKey;
        Boolean isProcessed = redisTemplate.opsForValue().setIfAbsent(redisKey, "processed", 1, TimeUnit.HOURS);
        if (isProcessed == null || !isProcessed) {
            throw new RuntimeException("Duplicate request detected");
        }
        
        // Deduct balance with optimistic locking
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        
        try {
            user.setBalance(user.getBalance() - amount);
            userRepository.save(user);
        } catch (OptimisticLockException e) {
            throw new RuntimeException("Concurrent update detected, please retry");
        }
    }
}