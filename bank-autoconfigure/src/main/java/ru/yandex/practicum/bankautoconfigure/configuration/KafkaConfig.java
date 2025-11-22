package ru.yandex.practicum.bankautoconfigure.configuration;

import io.micrometer.observation.ObservationRegistry;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

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
    @ConditionalOnBean(KafkaTemplate.class)
    public static BeanPostProcessor kafkaTemplateObservationPostProcessor(
            ObservationRegistry registry) {

        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                if (bean instanceof KafkaTemplate<?, ?> template) {
                    template.setObservationRegistry(registry);
                    template.setObservationEnabled(true);
                }
                return bean;
            }
        };
    }
    @Bean
    @ConditionalOnBean(ConcurrentKafkaListenerContainerFactory.class)
    public static BeanPostProcessor kafkaListenerFactoryObservationPostProcessor(
            ObservationRegistry registry) {

        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                if (bean instanceof ConcurrentKafkaListenerContainerFactory<?, ?> factory) {
                    factory.getContainerProperties().setObservationEnabled(true);
                    factory.getContainerProperties().setObservationRegistry(registry);
                }
                return bean;
            }
        };
    }

    @Bean
    @ConditionalOnProperty(prefix = "notificationsService", name = "needed")
    NotificationService notificationService(KafkaTemplate<String, String> kafkaTemplate) {
        return new NotificationService(kafkaTemplate);
    }
}
