package com.payment.system.gateway_service.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import reactor.core.publisher.Mono;
import org.springframework.security.core.Authentication;


@AllArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final String secret;
    
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication);
    }
}