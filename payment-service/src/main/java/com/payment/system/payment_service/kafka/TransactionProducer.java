package com.payment.system.payment_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionProducer {
    private final KafkaTemplate<String, TransactionLogMessage> kafkaTemplate;
    
    public void sendTransaction(TransactionLogMessage transactionLogMessage) {
        Message<TransactionLogMessage> message = MessageBuilder
                .withPayload(transactionLogMessage)
                .setHeader(KafkaHeaders.TOPIC, "transaction-topic")
                .build();
        kafkaTemplate.send(message);
    }
    
}
