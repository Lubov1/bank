package ru.yandex.practicum.exchange;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.exchange.services.CurrenciesService;

@Configuration
public class RestConfig {
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean @Primary
    CurrenciesService currenciesService() {
        return Mockito.mock(CurrenciesService.class);
    }
}
