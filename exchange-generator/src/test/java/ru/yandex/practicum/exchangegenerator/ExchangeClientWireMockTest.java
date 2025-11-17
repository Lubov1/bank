package ru.yandex.practicum.exchangegenerator;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.exchangegenerator.services.ExchangeProvider;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.config.import=",
        "spring.cloud.consul.enabled=false",
        "spring.cloud.discovery.enabled=false",
})
@ActiveProfiles("test")
@Import({RestConfig.class})
@EnableAutoConfiguration(exclude = {
        ru.yandex.practicum.bankautoconfigure.configuration.RestTemplateConfig.class
})
@EmbeddedKafka(topics={"exchanges"})
class ExchangeClientWireMockTest {

    @Autowired
    private ExchangeProvider exchangeProvider;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    void post() {
        Map<String, Object> consumerProps =
                KafkaTestUtils.consumerProps("producer-test-consumer", "false", embeddedKafkaBroker);

        DefaultKafkaConsumerFactory<String, Map<String, Double>> cf =
                new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new JsonDeserializer<>());

        Consumer<String, Map<String, Double>> consumer = cf.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "exchanges");

        exchangeProvider.sendCurrenciesKafka();
        ConsumerRecords<String, Map<String, Double>> inputMessage = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        ConsumerRecord<String, Map<String, Double>> record = inputMessage.iterator().next();
        assertThat(record.key()).isEqualTo("ex");
        assertThat(record.value().size()).isEqualTo(3);

    }
}

