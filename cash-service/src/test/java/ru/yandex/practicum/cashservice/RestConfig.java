package ru.yandex.practicum.cashservice;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.cashservice.services.CashService;

@Configuration
public class RestConfig {
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean @Primary
    CashService cashService() {
        return Mockito.mock(CashService.class);
    }
}
