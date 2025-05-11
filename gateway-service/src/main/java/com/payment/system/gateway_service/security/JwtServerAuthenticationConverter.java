package com.payment.system.gateway_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.security.core.Authentication;
import java.util.Collections;

@AllArgsConstructor
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {
    private final String secret;
    
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Hex.decodeHex(secret))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                return Mono.just(new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        token,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                ));
            } catch (Exception e) {
                return Mono.empty();
            }
        }
        return Mono.empty();
    }
}


