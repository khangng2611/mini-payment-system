package com.payment.system.transaction_service.repository;

import com.payment.system.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
