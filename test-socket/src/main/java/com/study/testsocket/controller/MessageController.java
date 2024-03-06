package com.study.testsocket.controller;


import com.study.testsocket.dto.MessageDto;
import com.study.testsocket.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MessageController {

//    private final SimpMessagingTemplate template;
//    private final MessageService messageService;

    private final KafkaProducer producer;

    @SubscribeMapping("/room/chat") // --> return 값이 바로 clientOutBoundChannel로 감
    public String getRoomInfos(Principal principal){
        return principal.getName() + "님 환영합니다.";
    }
    // 채팅방 구독 --> 환영 메시지 보내주기



    // 채팅방에 메시지 전달.
    @MessageMapping("/message")
    public void greeting(MessageDto message) throws Exception {
        producer.send("chat",message);
//        String text = message.getNickname() + " : " + message.getContent();
//        template.convertAndSend("/topic/room/chat",text);
    }
}



