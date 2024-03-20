package com.study.testsocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.testsocket.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService{

    private final SimpMessagingTemplate template;
    @Override
    public void broadCastMessage(MessageDto payload) {
        log.info("received : {}",payload.getMessage());
        template.convertAndSend("/topic/room/chat",payload);
    }
}
