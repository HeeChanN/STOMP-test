package com.study.testsocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.nio.charset.Charset;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        StompTest.TestWebSocketConfig.class
})
public class StompTest {

    @Autowired
    private AbstractSubscribableChannel clientInboundChannel;

    @Autowired private AbstractSubscribableChannel clientOutboundChannel;

    @Autowired private AbstractSubscribableChannel brokerChannel;

    private TestChannelInterceptor clientOutboundChannelInterceptor;

    private TestChannelInterceptor brokerChannelInterceptor;


    @Before
    public void setUp() throws Exception {
        this.brokerChannelInterceptor = new TestChannelInterceptor();
        this.clientOutboundChannelInterceptor = new TestChannelInterceptor();
        this.brokerChannel.addInterceptor(this.brokerChannelInterceptor);
        this.clientOutboundChannel.addInterceptor(this.clientOutboundChannelInterceptor);
    }


    /** subscribe 테스트 */
    @Test
    public void enterChatRoom() throws Exception {

        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        headers.setSubscriptionId("0");
        headers.setDestination("/app/room/1");
        headers.setSessionId("0");
        headers.setUser(new TestPrincipal("Ahn"));
        headers.setSessionAttributes(new HashMap<>());
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());

        this.clientOutboundChannelInterceptor.setIncludedDestinations("/app/room/1");
        this.clientInboundChannel.send(message);

        Message<?> reply = this.clientOutboundChannelInterceptor.awaitMessage(5);
        assertNotNull(reply);

        StompHeaderAccessor replyHeaders = StompHeaderAccessor.wrap(reply);
        assertEquals("0", replyHeaders.getSessionId());
        assertEquals("0", replyHeaders.getSubscriptionId());
        assertEquals("/app/room/1", replyHeaders.getDestination());

        String json = new String((byte[]) reply.getPayload(), Charset.forName("UTF-8"));
        assertEquals(json, "Ahn님 환영합니다.");



    }

    @Test
    public void sendMessageTest()throws Exception{
        MessageDto messageDto = new MessageDto("male","안녕하세요","1");

        StompHeaderAccessor headers2 = StompHeaderAccessor.create(StompCommand.SEND);
        headers2.setSubscriptionId("0");
        headers2.setDestination("/app/message");
        headers2.setSessionId("1");
        headers2.setUser(new TestPrincipal("Ahn"));
        headers2.setSessionAttributes(new HashMap<>());
        byte[] payload = new ObjectMapper().writeValueAsBytes(messageDto);
        Message<byte[]> message2 = MessageBuilder.createMessage(payload, headers2.getMessageHeaders());


        this.brokerChannelInterceptor.setIncludedDestinations("/topic/room/1");
        this.clientInboundChannel.send(message2);
        Message<?> reply2 = this.brokerChannelInterceptor.awaitMessage(5);
        assertNotNull(reply2);

        StompHeaderAccessor replyHeaders2 = StompHeaderAccessor.wrap(reply2);
        assertEquals("/topic/room/1", replyHeaders2.getDestination());

        String json2 = new String((byte[]) reply2.getPayload(), Charset.forName("UTF-8"));
        assertEquals(json2,  "male : 안녕하세요");
    }

    @Configuration
    @EnableScheduling
    @ComponentScan(
            basePackages="com.study.testsocket",
            excludeFilters = @ComponentScan.Filter(type= FilterType.ANNOTATION, value = Configuration.class)
    )
    @EnableWebSocketMessageBroker
    static class TestWebSocketConfig implements WebSocketMessageBrokerConfigurer {

        @Autowired
        Environment env;

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/ws-stomp").withSockJS();
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.enableSimpleBroker("/queue/", "/topic/");
            registry.setApplicationDestinationPrefixes("/app");
        }
    }

}
