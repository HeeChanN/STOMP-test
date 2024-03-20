package com.study.testsocket.kafka;

import com.study.testsocket.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, MessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, MessageDto payload) {

        kafkaTemplate.send(topic, payload);

    }
}