package ru.yandex.practicum.exchangegenerator.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ExchangeProvider {
    RestTemplate restTemplate;
    ExchangeGenerator exchangeGenerator;

    @Value("${gateway.prefix}")
    String gatewayPrefix;
    @Value("${exchange.prefix}")
    String exchangePrefix;

    public ExchangeProvider(RestTemplate restTemplate, ExchangeGenerator exchangeGenerator) {
        this.restTemplate = restTemplate;
        this.exchangeGenerator = exchangeGenerator;
    }
    @Scheduled(fixedRate = 60000)
    public void sendCurrencies() {
        System.out.println("Sending exchange");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,Double>> entity = new HttpEntity<>(exchangeGenerator.getExchanges(), headers);
        restTemplate.exchange("http://" + gatewayPrefix + "/"+ exchangePrefix +"/getCurrencies",
                HttpMethod.POST, entity, Void.class);
    }
}
