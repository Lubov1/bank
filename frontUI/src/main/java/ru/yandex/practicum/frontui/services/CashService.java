package ru.yandex.practicum.frontui.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.exceptions.CashServiceResponseException;

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

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("currency", currency.name());
        map.add("amount", amount);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
        try {
            restTemplate.exchange(String.join("/", "http:/", gatewayPrefix, cashPrefix, login, withdraw),
                    HttpMethod.POST, entity, Void.class);
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new CashServiceResponseException(ex.getMessage(), login);
        }
    }
}
