package ru.yandex.practicum.frontui.cash;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.dto.CashRequest;

import java.math.BigDecimal;


@Component
public class ExchangeClientCash {

    private final RestClient restClient;

    public ExchangeClientCash(RestClient.Builder builder,
                          @Value("${cash-service.base-url:http://localhost:9546}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }



    public int deposit() {
        return restClient.post()
                .uri("/login/deposit")
                .body(new CashRequest(BigDecimal.TEN, Currencies.CNY))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .toEntity(String.class)
                .getStatusCode().value();
    }
    public int withdraw() {
        return restClient.post()
                .uri("/login/withdraw")
                .body(new CashRequest(BigDecimal.TEN, Currencies.CNY))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .toEntity(String.class)
                .getStatusCode().value();
    }
}