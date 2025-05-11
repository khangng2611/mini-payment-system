package com.payment.system.payment_service.user;

public record CustomerResponse (
    String id,
    String firstName,
    String lastName,
    String email
){
}
