package com.kainfosoft.controller;

import com.kainfosoft.model.Product;
import com.kainfosoft.producer.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Product product) {
        kafkaProducer.sendMessage(product);
        return ResponseEntity.ok("Product published: " + product);
    }
}
