package com.payment.system.transaction_service.kafka;

import com.payment.system.common.dto.TransactionLogMessage;
import com.payment.system.transaction_service.entity.Transaction;
import com.payment.system.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionConsumer {
    @Autowired
    private TransactionRepository transactionRepository;
    
    @KafkaListener(topics = "transaction-topic", groupId = "transactionGroup")
    public void consumeTransaction(TransactionLogMessage message) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(message.transactionId());
        transaction.setUsername(message.request().username());
        transaction.setAmount(message.request().amount());
        transactionRepository.save(transaction);
    }
}
