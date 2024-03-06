package com.study.testsocket.kafka;

import com.study.testsocket.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

    public void send(String topic, MessageDto payload) {

        kafkaTemplate.send(topic, payload);

    }
}