package ru.yandex.practicum.exchangegenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.exchangegenerator.services.ExchangeGenerator;

import java.util.Map;

@Component
public class ExchangeClient {

    private final RestClient restClient;

    @Autowired
    ExchangeGenerator exchangeGenerator;

    public ExchangeClient(RestClient.Builder builder,
                          @Value("${exchange.base-url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public Map<String, Object> getCurrencies() {
        return restClient.get()
                .uri("/getCurrencies")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .retrieve()
                .body(Map.class);
    }

    public int postCurrencies() {
        return restClient.post()
                .uri("/getCurrencies")
                .body(exchangeGenerator.getExchanges())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .retrieve()
                .toEntity(Void.class)
                .getStatusCode().value();
    }
}