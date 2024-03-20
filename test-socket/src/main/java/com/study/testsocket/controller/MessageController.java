package com.study.testsocket.controller;


import com.study.testsocket.dto.MessageDto;
import com.study.testsocket.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final SimpMessagingTemplate template;

    private final KafkaProducer producer;

    @MessageMapping("/stomp-test")
    public void go(MessageDto message) throws Exception{

        String text = message.getNickname() + " : " + message.getContent();
        message.setMessage(text);
        template.convertAndSend("/topic/room/chat",message);
    }

    @MessageMapping("/kafka-stomp-test")
    public void greeting(MessageDto message) throws Exception {
        String text = message.getNickname() + " : " + message.getContent();
        message.setMessage(text);
        producer.send("chat",message);
    }
}



