package com.dvtt.demo.demospringbootkafka.config;

import com.dvtt.demo.demospringbootkafka.constant.ApplicationConstant;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linhtn on 3/13/2022.
 */
@Configuration
@EnableKafka
public class KafkaProduceConfig {

//    @Bean("customKafkaTemplate")
//    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
//        return new KafkaTemplate<>(producerFactory);
//    }

//    @Bean
//    public ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties) {
//        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
//    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ApplicationConstant.KAFKA_LOCAL_SERVER_CONFIG);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "1");
        configProps.put(ProducerConfig.RETRIES_CONFIG, "3");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean("customKafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
