package ru.yandex.practicum.exchange;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.yandex.practicum.exchange.services.CurrenciesService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.Map;

@SpringBootTest(properties = {
        "spring.cloud.consul.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.config.import=",
        "spring.profiles.active=test"
})
@Import({RestConfig.class})
@EnableAutoConfiguration(exclude = {
        ru.yandex.practicum.bankautoconfigure.configuration.RestTemplateConfig.class
})
@EmbeddedKafka(topics = {"exchanges"})
public class BaseContractTest {

    @Autowired
    KafkaTemplate<String, Map<String, Double>> kafkaTemplate;

    @Autowired
    CurrenciesService currenciesService;


    @Test
    void testListener() throws InterruptedException {
        Message<Map<String, Double>> message = MessageBuilder
                .withPayload(Map.of("USD", 10.2, "RUB", 10.6))
                .setHeader(KafkaHeaders.TOPIC, "exchanges")
                .setHeader(KafkaHeaders.KEY, "ex")
                .build();
        kafkaTemplate.send(message);
        Awaitility.await()
            .atMost(Duration.ofSeconds(3))
            .untilAsserted(() -> {
                var currencies = currenciesService.getCurrencies();
                assertNotNull(currencies);
                assertEquals(2, currencies.size());
                assertEquals(10.2, currencies.get("USD").getValue().doubleValue());
                assertEquals(10.6, currencies.get("RUB").getValue().doubleValue());
            });
    }
}