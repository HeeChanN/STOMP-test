package com.study.testsocket;

import com.study.testsocket.kafka.KafkaConsumer;
import com.study.testsocket.kafka.KafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProducerTest {


//    @Autowired
//    private KafkaProducer producer;
//
//    @Autowired
//    private KafkaConsumer consumer;
//
//    @Test
//    public void sendMessageToKafka () throws Exception {
//
//        String topic = "order";
//
//        MessageDto payload = new MessageDto("male","안녕하세요","1");
//        producer.send(topic, payload);
//
//
//        consumer.getLatch().await(3, TimeUnit.SECONDS);
//
//
//        assertEquals(payload.content,consumer.getPayloads().get(0).content);
//
//
//    }

}
