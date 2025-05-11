package com.payment.system.common.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse<T> {
    private long timestamp;
    private int status;
    private String message;
    private T data;
    private String error;
    
    public ApiResponse(int status, String message, T data, String error) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "success", data, null);
    }
    
    public static <T> ApiResponse<T> error(HttpStatus status, String message, String error) {
        return new ApiResponse<>(status.value(), message, null, error);
    }
}
