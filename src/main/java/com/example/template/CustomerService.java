package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

//    @Autowired
//    SurveyRepository surveyRepository;
//
//    @Autowired
//    KafkaTemplate kafkaTemplate;
//
//    @KafkaListener(topics = "${eventTopic}")
//    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
//        System.out.println("##### listener : " + message);
//
//    }
}
