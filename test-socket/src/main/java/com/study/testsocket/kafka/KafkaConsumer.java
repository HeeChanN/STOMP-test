package com.study.testsocket.kafka;

import com.study.testsocket.dto.MessageDto;
import com.study.testsocket.service.MessageService;
import com.study.testsocket.service.MessageServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@Getter
@ToString
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private CountDownLatch latch = new CountDownLatch(10); // 테스트 코드 작성시 10개의 메시지를 처리할 때 까지 대기하기 위함
    private MessageDto payload;
    private final MessageService messageService;
    private List<MessageDto> list = new ArrayList<>();



    @KafkaListener(topics = "chat", containerFactory = "kafkaListenerContainerFactory")
    public void receive(ConsumerRecord<String, MessageDto> consumerRecord) {
        payload = consumerRecord.value();
        list.add(payload);
        messageService.broadCastMessage(payload);
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}