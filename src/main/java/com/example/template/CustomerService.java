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

    @Autowired
    KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = "${eventTopic}")
    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("##### listener : " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SurveyCompleted surveyCompleted = null;
        try {
            surveyCompleted = objectMapper.readValue(message, SurveyCompleted.class);

            if (surveyCompleted.getEventType().equals(SurveyCompleted.class.getSimpleName())) {

                // 고객 상품 만족도가 1 이하라면 블랙리스트로 만드는 이벤트 발송
                if( surveyCompleted.getProductSatisfaction() <= 1 ){
                    String json = null;

                    try {
                        BlackListAdded blackListAdded = new BlackListAdded();
                        blackListAdded.setCustomerName(surveyCompleted.getCustomerName());

                        json = objectMapper.writeValueAsString(blackListAdded);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("JSON format exception", e);
                    }

                    Environment env = Application.applicationContext.getEnvironment();
                    String topicName = env.getProperty("eventTopic");
                    ProducerRecord producerRecord = new ProducerRecord<>(topicName, json);
                    kafkaTemplate.send(producerRecord);
                }
            }

        }catch (Exception e){

        }
    }
}
