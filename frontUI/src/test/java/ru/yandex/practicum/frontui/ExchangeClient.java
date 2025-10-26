package ru.yandex.practicum.frontui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class ExchangeClient {

    private final RestClient restClient;

    public ExchangeClient(RestClient.Builder builder,
                          @Value("${exchange.base-url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }



    public int deposit() {
        return restClient.post()
                .uri("/login/deposit")
                .body("""
          {
            "amount": 250.00,
            "currency": "EUR"
          }
          """)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .toEntity(Void.class)
                .getStatusCode().value();
    }
    public int withdraw() {
        return restClient.post()
                .uri("/login/withdraw")
                .body("""
          {
            "amount": 250.00,
            "currency": "EUR"
          }
          """).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .toEntity(Void.class)
                .getStatusCode().value();
    }
}