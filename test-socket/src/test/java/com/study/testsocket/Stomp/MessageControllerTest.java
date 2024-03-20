package com.study.testsocket.Stomp;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.testsocket.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageControllerTest {

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
    @DisplayName("웹소켓 연결을 진행하고 /topic/room/chat에 구독한 후 메시지를 보내고 수신까지 확인하기")
    void stompTest() throws Exception {
        StompSession session = webSocketStompClient.connectAsync("ws://localhost:"+port+"/ws-stomp", new StompSessionHandlerAdapter() {}).get();

        CompletableFuture<MessageDto> completableFuture = new CompletableFuture<>();

        session.subscribe("/topic/room/chat", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MessageDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                completableFuture.complete((MessageDto) payload);
            }
        });

        System.out.println("Subscription");
        MessageDto messageDto = new MessageDto("Lee","Hello");

        session.send("/app/stomp-test", messageDto);

        assertEquals("hell", completableFuture.get(1, SECONDS).getMessage());

    }
}