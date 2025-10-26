package ru.yandex.practicum.transfer;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.transfer.services.TransferService;

@Configuration
public class RestConfig {
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean @Primary
    TransferService transferService() {return Mockito.mock(TransferService.class);}
}
