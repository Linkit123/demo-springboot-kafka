package com.dvtt.demo.demospringbootkafka.consumer;

import com.dvtt.demo.demospringbootkafka.constant.ApplicationConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by linhtn on 3/13/2022.
 */
@Service
@Slf4j
public class LoadTestConsumer {

    @Qualifier(value = "customTaskExecutor")
    private final TaskExecutor taskExecutor;
    public LoadTestConsumer(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @KafkaListener(topics = ApplicationConstant.TOPIC_NAME, groupId = ApplicationConstant.GROUP_ID)
//    @KafkaListener(topics = "#{'${spring.kafka.consumer.topic}'}")
    public void loadTestConsumer(String data,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                 @Header(KafkaHeaders.OFFSET) int offsets, Acknowledgment acknowledgment) {
        var uuid = UUID.randomUUID().toString();
        try {
            log.info("----> START consume {}, offsets: {}, partition: {}, data: {}", uuid, offsets, partition, data);
            // process the message
            consumeMessage();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            log.info("----> END consume {}", uuid);
            //  manually acknowledgment
            acknowledgment.acknowledge();
        }
    }

    private void consumeMessageWithThread(int threads) {

        for (int i = 0; i < threads; i++) {
            taskExecutor.execute(this::consumeMessage);
        }
    }

    private void consumeMessage() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
