package ru.yandex.practicum.frontui.transfer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class ExchangeClientTransfer {

    private final RestClient restClient;

    public ExchangeClientTransfer(RestClient.Builder builder,
                                  @Value("${transfer.base-url:http://localhost:9545}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public int transfer() {
        return restClient.post()
                .uri("/login/transfer")
                .body("""
          {
            "amount": 250.00,
            "currencyTo": "USD",
            "currencyFrom": "RUB",
            "loginTo": "login2"
          }
          """).contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Void.class)
                .getStatusCode().value();
    }
}