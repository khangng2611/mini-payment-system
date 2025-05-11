package com.payment.system.gateway_service.filter;
import lombok.Data;

@Data
public class RequestBlockerFilterConfig {
    private String blockedPath;
}
