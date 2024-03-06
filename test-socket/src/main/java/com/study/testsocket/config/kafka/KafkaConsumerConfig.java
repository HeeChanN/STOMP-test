package com.study.testsocket.config.kafka;

import com.study.testsocket.dto.MessageDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {


    @Bean
    public ConsumerFactory<String, MessageDto> consumerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "43.202.54.158:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "foo-1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // 들어오는 record 를 객체로 받기 위한 deserializer
        JsonDeserializer<MessageDto> deserializer = new JsonDeserializer<>(MessageDto.class, false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageDto>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


    /** MessageListenerContainer 를 설정하여 Consuming을 진행한다.
     *  MessageListener 구현체를 MessageListenerContainer 설정에 제공하거나 처리와 관련된
     *  함수에 @KafkaListener 어노테이션을 붙여 Consuming을 할 수 있다.
     *  Spring Kafka에서는 2개의 구현체를 제공한다.
     *  - KafkaMessageListenerContainer --> 단일 스레드로 동작하는 컨슈머
     *  - ConcuurentMessageListenerContainer --> 내부적으로 하나 이상의 KafkaMessageListenarContainer로 구성되는 멀티 스레드 방식의 컨슈머
     *
     *  */

}