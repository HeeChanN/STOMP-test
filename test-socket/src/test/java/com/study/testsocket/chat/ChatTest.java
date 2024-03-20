package com.study.testsocket.chat;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.study.testsocket.dto.MessageDto;
import com.study.testsocket.kafka.KafkaConsumer;
import com.study.testsocket.kafka.KafkaProducer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(
        topics = {"chat"},
        partitions = 1,
        ports = {9092},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092"})
public class ChatTest {

    @LocalServerPort
    private int port;
    private WebSocketStompClient webSocketStompClient;

    @BeforeEach
    void setUp() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(transports));
        this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }


    @Test
    void subscribeSendTest() throws  Exception{

        StompSession session = webSocketStompClient.connectAsync("ws://localhost:"+port+"/ws-stomp", new StompSessionHandlerAdapter() {}).get();

        CompletableFuture<MessageDto> completableFuture = new CompletableFuture<>();


        session.subscribe("/topic/room/chat",new StompFrameHandler(){
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MessageDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                MessageDto messageDto = (MessageDto)payload;
                System.out.println(messageDto.getMessage());
                completableFuture.complete(messageDto);
            }
        });

        MessageDto messageDto = new MessageDto("Lee","안녕하세요");

        session.send("/app/kafka-stomp-test",messageDto);

        assertEquals("check", completableFuture.get(1, SECONDS).getMessage());

    }

}
