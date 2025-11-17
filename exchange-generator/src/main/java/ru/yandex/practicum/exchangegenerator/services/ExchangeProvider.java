package ru.yandex.practicum.exchangegenerator.services;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExchangeProvider {
    ExchangeGenerator exchangeGenerator;
    Logger logger = LoggerFactory.getLogger(ExchangeProvider.class);
    KafkaTemplate<String, Double> kafkaTemplate;


    public ExchangeProvider(
            ExchangeGenerator exchangeGenerator, KafkaTemplate<String, Double> kafkaTemplate) {
        this.exchangeGenerator = exchangeGenerator;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 10000)
    public void sendCurrenciesKafka() {
        logger.info("Sending exchange");
        Map<String, Double> exchanges = exchangeGenerator.getExchanges();
        Message<Map<String, Double>> message = MessageBuilder
                .withPayload(exchanges)
                .setHeader(KafkaHeaders.TOPIC, "exchanges")
                .setHeader(KafkaHeaders.KEY, "ex")
                .build();

        kafkaTemplate.send(message).whenComplete((result, e) -> {
            if (e != null) {
                logger.error("Ошибка при отправке сообщения: {}", e.getMessage(), e);
                return;
            }

            RecordMetadata metadata = result.getRecordMetadata();
            logger.info("Сообщение отправлено. Topic = {}, partition = {}, offset = {}",
                    metadata.topic(), metadata.partition(), metadata.offset());
        });
        logger.info("exchanges are sent");
    }
}
