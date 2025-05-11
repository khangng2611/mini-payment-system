package com.payment.system.gateway_service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RequestBlockerFilter extends AbstractGatewayFilterFactory<RequestBlockerFilterConfig> {
    public RequestBlockerFilter() {
        super(RequestBlockerFilterConfig.class);
    }
    
    @Override
    public GatewayFilter apply(RequestBlockerFilterConfig config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            if (path.matches(config.getBlockedPath().replace("*", "[^/]+"))) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }
}