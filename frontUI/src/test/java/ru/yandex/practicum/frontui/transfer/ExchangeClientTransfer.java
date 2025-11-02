package ru.yandex.practicum.frontui.transfer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.dto.TransferRequest;

import java.math.BigDecimal;


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
                .body(new TransferRequest("loginTo", Currencies.CNY, Currencies.USD,
                        BigDecimal.TWO))
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Void.class)
                .getStatusCode().value();
    }
}