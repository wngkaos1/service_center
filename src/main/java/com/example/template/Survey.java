package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;

import javax.persistence.*;

@Entity
public class Survey {

    @Id
    @GeneratedValue
    private Long id;

    private String orderId;
    private String customerId;
    private String customerName;
    private String surveyMessage;

    private int surveyRecommend;
    private int surveyDelivery;
    // 만족 불만족 여부 - 불만족이 있으면 블랙리스트에 추가된다.
    private int productSatisfaction;


    @PostPersist
    private void onComplete() {
        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        SurveyCompleted surveyCompleted = new SurveyCompleted();
        try {
            BeanUtils.copyProperties(this, surveyCompleted);
            json = objectMapper.writeValueAsString(surveyCompleted);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        if( json != null ){
            Environment env = Application.applicationContext.getEnvironment();
            String topicName = env.getProperty("eventTopic");
            ProducerRecord producerRecord = new ProducerRecord<>(topicName, json);
            kafkaTemplate.send(producerRecord);
        }
    }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSurveyMessage() {
        return surveyMessage;
    }

    public void setSurveyMessage(String surveyMessage) {
        this.surveyMessage = surveyMessage;
    }

    public int getSurveyRecommend() {
        return surveyRecommend;
    }

    public void setSurveyRecommend(int surveyRecommend) {
        this.surveyRecommend = surveyRecommend;
    }

    public int getSurveyDelivery() {
        return surveyDelivery;
    }

    public void setSurveyDelivery(int surveyDelivery) {
        this.surveyDelivery = surveyDelivery;
    }

    public int getProductSatisfaction() {
        return productSatisfaction;
    }

    public void setProductSatisfaction(int productSatisfaction) {
        this.productSatisfaction = productSatisfaction;
    }
}
