package com.study.testsocket.kafka;

import com.study.testsocket.dto.MessageDto;
import com.study.testsocket.service.MessageServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@Getter
@RequiredArgsConstructor
public class KafkaConsumer {

    private CountDownLatch latch = new CountDownLatch(1);
    private MessageDto payload;
    private final MessageServiceImpl messageService;

    @KafkaListener(topics = "chat", containerFactory = "kafkaListenerContainerFactory")
    public void receive(ConsumerRecord<String, MessageDto> consumerRecord) {
        payload = consumerRecord.value();
        messageService.broadCastMessage(payload);
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}