package com.payment.system.transaction_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Data
public class Transaction {
    @Id
    private String transactionId;
    private String username;
    private double amount;
    @Version
    private int version;
}