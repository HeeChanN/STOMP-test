package com.study.testsocket.service;

import com.study.testsocket.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final SimpMessagingTemplate template;

    @Override
    public void broadCastMessage(MessageDto payload) {
        String text = payload.getNickname() + " : " + payload.getContent();
        template.convertAndSend("/topic/room/chat",text);
    }
}
