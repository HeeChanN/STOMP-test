package com.study.testsocket.config.websocket;



import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 구독과 BroadCasting 을 처리
        registry.setApplicationDestinationPrefixes("/app"); // 메시지 전달을 할때 사용할 uri
    }


    /** 웹소켓 HandShake를 위한 endpoint */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp");
    }
}


// WebSocket을 지원하지 않는 경우 Socket.io 나 SockJS 등을 활용해 볼 수 있다.





