package com.study.testsocket;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate template;
    private final MessageService messageService;

    @SubscribeMapping("/room/{roomId}") // --> return 값이 바로 clientOutBoundChannel로 감
    public String getRoomInfos(@DestinationVariable String roomId, Principal principal){
        return principal.getName() + "님 환영합니다.";
    }
    // 채팅방 구독 --> 환영 메시지 보내주기


    // 채팅방에 메시지 전달.
    @MessageMapping("/message")
    public void greeting(MessageDto message) throws Exception {
        String text = message.nickname + " : " + message.content;
        template.convertAndSend("/topic/room/" +message.getRoomId(),text);
    }
}

