package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    private String customerName;
    private String customerRequirements;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerRequirements() {
        return customerRequirements;
    }

    public void setCustomerRequirements(String customerRequirements) {
        this.customerRequirements = customerRequirements;
    }

    @PostPersist
    private void publishStart() {
        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        ProductRequired productRequired = new ProductRequired();
        productRequired.setProductName("리모콘");
        try {
            BeanUtils.copyProperties(this, productRequired);
            json = objectMapper.writeValueAsString(productRequired);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        if( json != null ){
            ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
            kafkaTemplate.send(producerRecord);
        }
    }
}
