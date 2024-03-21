package com.study.testsocket.Stomp;



import com.study.testsocket.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;


import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageControllerTest {

    @LocalServerPort
    private int port;
    private WebSocketStompClient webSocketStompClient;

    @BeforeEach
    void setUp() {

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        webSocketStompClient = new WebSocketStompClient(webSocketClient);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

    }

    @Test
    @DisplayName("웹소켓 연결을 진행하고 /topic/room/chat에 구독한 후 메시지를 보내고 수신까지 확인하기")
    void stompTest() throws Exception {
        StompSession session = webSocketStompClient.connectAsync("ws://localhost:"+port+"/ws-stomp", new StompSessionHandlerAdapter() {}).get();

        ArrayList<MessageDto> list = new ArrayList<>();


        //StompSession.Subscription subscription =
        session.subscribe("/topic/room/chat", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MessageDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println(payload);
                list.add((MessageDto) payload);
            }
        });

        //System.out.println("Subscription :" + subscription.getSubscriptionHeaders() );
        MessageDto messageDto = new MessageDto("Lee","Hello");

        session.send("/app/stomp-test", messageDto);

        Thread.sleep(3000);
        //assertEquals("hello", list.get(0).getMessage());
        assertEquals("객체", list.get(0).getMessage());

    }
}