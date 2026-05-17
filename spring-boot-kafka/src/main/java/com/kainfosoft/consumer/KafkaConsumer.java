package com.kainfosoft.consumer;

import com.kainfosoft.model.Product;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Product product) {
        System.out.println("Received: " + product);
    }
}
