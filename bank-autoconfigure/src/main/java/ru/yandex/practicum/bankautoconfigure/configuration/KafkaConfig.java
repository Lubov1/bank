package ru.yandex.practicum.bankautoconfigure.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@AutoConfiguration
@ConditionalOnClass(org.springframework.security.oauth2.client.registration.ClientRegistrationRepository.class)
public class KafkaConfig {
    @Bean
    @ConditionalOnBean(NotificationService.class)
    public NewTopic exchangesTopic() {
        return TopicBuilder.name("notifications")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "notificationsService", name = "needed")
    NotificationService notificationService(KafkaTemplate<String, String> kafkaTemplate) {
        return new NotificationService(kafkaTemplate);
    }
}
