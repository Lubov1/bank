package ru.yandex.practicum.frontui.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.dto.CashRequest;
import ru.yandex.practicum.frontui.exceptions.CashServiceResponseException;

import java.math.BigDecimal;

@Service
public class CashService {
    @Value("${accounts.prefix}")
    String accountPrefix;
    @Value("${gateway.prefix}")
    String gatewayPrefix;

    @Value("${cash.prefix}")
    String cashPrefix;
    private RestTemplate restTemplate;
    public CashService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void withdraw(String login, Long amount, Currencies currency) {
        sendCashRequest(currency, amount, login, "withdraw");
    }

    public void deposit(String login, Long amount, Currencies currency) {
        sendCashRequest(currency, amount, login, "deposit");
    }

    private void sendCashRequest(Currencies currency, Long amount, String login, String withdraw) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);



        HttpEntity<CashRequest> entity = new HttpEntity<>(new CashRequest(BigDecimal.valueOf(amount), currency), headers);
        try {
            restTemplate.exchange(String.join("/", "http:/", gatewayPrefix, cashPrefix, login, withdraw),
                    HttpMethod.POST, entity, Void.class);
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new CashServiceResponseException(ex.getMessage(), login);
        }
    }
}
