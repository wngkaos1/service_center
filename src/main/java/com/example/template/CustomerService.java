package com.example.template;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @KafkaListener(topics = "${eventTopic}")
    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("##### listener : " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SurveyCompleted surveyCompleted = null;
        try {
            surveyCompleted = objectMapper.readValue(message, SurveyCompleted.class);

            System.out.println(" #### type = " + surveyCompleted.getEventType());

            /**
             * 설문조사 완료시 설문조사 결과를 분석하여 추가 물량을 요청함
             */
            if( surveyCompleted.getEventType().equals(SurveyCompleted.class.getSimpleName())){

                Customer customer = new Customer();
                customer.setCustomerName(surveyCompleted.getCustomerName());
                customer.setCustomerRequirements(surveyCompleted.getSurveyMessage());

                customerRepository.save(customer);

            }

        }catch (Exception e){

        }
    }
}
