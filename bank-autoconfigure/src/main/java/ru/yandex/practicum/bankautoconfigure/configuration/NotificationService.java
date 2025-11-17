package ru.yandex.practicum.bankautoconfigure.configuration;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NotificationService {

    KafkaTemplate<String, String> kafkaTemplate;
    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(String email, String message) {
        logger.info("Sending notification to " + email);
        Map<String, String> map = new HashMap<>();
        map.put("email", "lubov.88867@gmail.com");
        map.put("message", message);
        Message<Map<String, String>> kafkaMessage = MessageBuilder
                .withPayload(map)
                .setHeader(KafkaHeaders.TOPIC, "notifications")  // Топик
                .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
                .build();

        kafkaTemplate.send(kafkaMessage).whenComplete((result, e) -> {
            if (e != null) {
                logger.error("Ошибка при отправке сообщения: {}", e.getMessage(), e);
                return;
            }

            RecordMetadata metadata = result.getRecordMetadata();
            logger.info("Сообщение отправлено. Topic = {}, partition = {}, offset = {}",
                    metadata.topic(), metadata.partition(), metadata.offset());
        });
    }
}
