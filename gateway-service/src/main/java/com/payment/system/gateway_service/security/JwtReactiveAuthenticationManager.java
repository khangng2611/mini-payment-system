package com.payment.system.gateway_service.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import reactor.core.publisher.Mono;
import org.springframework.security.core.Authentication;


public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final String secret;
    
    public JwtReactiveAuthenticationManager(String secret) {
        this.secret = secret;
    }
    
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication);
    }
}