package com.payment.system.gateway_service.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RequestBlockerFilter extends AbstractGatewayFilterFactory<RequestBlockerFilter.Config> {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestBlockerFilter.class);
    
    public RequestBlockerFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            if (path.matches(config.getBlockedPath().replace("*", "[^/]+"))) {
                logger.warn("Blocked external access to internal endpoint: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }
    
    public static class Config {
        private String blockedPath;
        
        public String getBlockedPath() {
            return blockedPath;
        }
        
        public void setBlockedPath(String blockedPath) {
            this.blockedPath = blockedPath;
        }
    }
}