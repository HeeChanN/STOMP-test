package com.study.testsocket.kafka;

import com.study.testsocket.dto.MessageDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.concurrent.TimeUnit;


@SpringBootTest
@EmbeddedKafka(
        topics = {"chat"},
        partitions = 1,
        ports = {9092},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092"})
public class KafkaTest {

    @Autowired
    KafkaProducer producer;

    @Autowired
    KafkaConsumer consumer;

    @Test
    @DisplayName("내장 카프카에 메시지 produce하고 consume까지 진행")
    void consumeTest() throws  Exception{
        MessageDto messageDto = new MessageDto("Lee","Hi");
        MessageDto messageDto1 = new MessageDto("Hee","By");
        for(int i = 0; i<10;i++){
            if(i%2 == 0)
                producer.send("chat",messageDto);
            else
                producer.send("chat",messageDto1);
        }

        consumer.getLatch().await(10, TimeUnit.SECONDS);

        Assertions.assertEquals(consumer.getList().size(),10);
        consumer.getList().stream().forEach(o->System.out.println(o.getNickname() +" : "+o.getContent()));
    }
}

//    // Kafka Producer 설정
//    public ProducerFactory<String, MessageDto> factory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//
//        return new DefaultKafkaProducerFactory<>(props);
//    }
//
//    public KafkaTemplate<String, MessageDto> kafkaTemplate(){
//        return new KafkaTemplate<>(factory());
//    }
//
//
//    // Consumer 설정
//    public ConsumerFactory<String, MessageDto> consumerFactory() {
//
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "foo-1");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//
//        // 들어오는 record 를 객체로 받기 위한 deserializer
//        JsonDeserializer<MessageDto> deserializer = new JsonDeserializer<>(MessageDto.class, false);
//
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
//    }
//
//    public ConcurrentKafkaListenerContainerFactory<String, MessageDto>
//    kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, MessageDto> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }

/** 처음에는 Spring 컨테이너의 도움 없이 테스트를 진행하려고 했지만
 *  결국 내가 KafkaConsumer에 @KafkaListener를 사용한 부분 때문에 해당 코드는 스프링 컨테이너 없이는 테스트할 수 없었다.
 *  스프링컨테이너 없이 테스트하려면 따로 poll()을 하는 Consumer를 생성해서 그 Consumer를 사용한다면 가능해 보인다.
 *  이 코드는 스프링 컨테이너를 이용하여 임베디드 카프카에 Produce하고 Consume한 테스트이다.
 *
 *
 * */
