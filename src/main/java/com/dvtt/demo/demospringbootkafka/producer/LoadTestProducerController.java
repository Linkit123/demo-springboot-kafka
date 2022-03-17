package com.dvtt.demo.demospringbootkafka.producer;

import com.dvtt.demo.demospringbootkafka.constant.ApplicationConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by linhtn on 3/13/2022.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/load-test")
public class LoadTestProducerController {

    @Qualifier(value = "customTaskExecutor")
    private final TaskExecutor taskExecutor;

    private final KafkaTemplate<String, String> kafkaTemplate;
//    @Value("${spring.kafka.producer.topic}")
//    private String topic;

    public LoadTestProducerController(TaskExecutor taskExecutor,
                                      @Qualifier(value = "customKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.taskExecutor = taskExecutor;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public ResponseEntity<String> fireMessage(String data) {
//        sendMessages(new Date(), 100);
                        sendMessagesWithThread(30, 5);
//        kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    private void sendMessages(long messageCount) {
        log.info("Producer started...");
        for (int i = 0; i < messageCount; i++) {
            String value = RandomStringUtils.random(100, true, true);
            kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, value);
//            kafkaTemplate.send(topic, value);
        }
        log.info("Producer finished.");
    }

    @Bean
    ApplicationRunner runAdditionalClientCacheInitialization() {
        return args -> {
//            sendMessages(new Date(), 100);
//                        sendMessagesWithThread(startTime, 1000000, 10);
        };
    }

    private void sendMessagesWithThread(long totalMessages, int threads) {
        final long messagePerThread = totalMessages / threads;
        log.info("messagePerThread:{}", messagePerThread);
        for (int i = 0; i < threads; i++) {
            taskExecutor.execute(() -> {
                sendMessages(messagePerThread);
            });
        }
    }
}
