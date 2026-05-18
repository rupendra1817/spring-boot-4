package com.kainfosoft.producer;

import com.kainfosoft.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Value("${kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, Product> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Product> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Product product) {
        kafkaTemplate.send(topicName, product);
        System.out.println("Published: " + product);
    }
}
