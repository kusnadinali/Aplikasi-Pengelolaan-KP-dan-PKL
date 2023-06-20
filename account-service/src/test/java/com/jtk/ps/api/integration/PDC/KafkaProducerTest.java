package com.jtk.ps.api.integration.PDC;

import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtk.ps.api.dto.kafka.AccountKafka;
import com.jtk.ps.api.model.Account;
import com.jtk.ps.api.model.ERole;

@SpringBootTest
@EmbeddedKafka
public class KafkaProducerTest {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private KafkaProducer<String, String> kafkaProducer;

    @BeforeEach
    public void setup() {
        kafkaTemplate.execute(producer -> {
            kafkaProducer = (KafkaProducer<String, String>) producer;
            return null;
        });

        // Create Kafka consumer properties
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
    }

    @AfterEach
    public void cleanup() {
        kafkaProducer.close();
    }

    private String convertToKafka(Account account, String operation){
        AccountKafka accountKafka = new AccountKafka();

        accountKafka.setId(account.getId());
        accountKafka.setUsername(account.getUsername());
        accountKafka.setRole_id(account.getRole().id);
        accountKafka.setOperation(operation);
        String pesanJson = "";
        try {
            // Mengubah objek menjadi string JSON
            ObjectMapper objectMapper = new ObjectMapper();
            pesanJson = objectMapper.writeValueAsString(accountKafka);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return pesanJson;
    }

    @Test
    void sendAccountKafkaTest(){
        // Account account = new Account(0, "admin", "1234", ERole.COMMITTEE);
        // KafkaConsumer<String, String> kafkaConsumer = mock(KafkaConsumer.class);
        
        // doNothing().when(kafkaConsumer).subscribe(anyCollection());
        
        // String pesanJson = convertToKafka(account, "ADDED");

        // kafkaTemplate.send("account_topic_test", pesanJson);

        // verify(kafkaConsumer, times(1)).subscribe(Collections.singleton("account_topic_test"));
        // verify(kafkaConsumer, times(1)).close();
    }
}
