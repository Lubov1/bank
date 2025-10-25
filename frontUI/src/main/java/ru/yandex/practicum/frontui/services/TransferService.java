package ru.yandex.practicum.frontui.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.exceptions.AccountServiceResponseException;

@Service
public class TransferService {
    public TransferService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${gateway.prefix}")
    String gatewayPrefix;

    @Value("${transfer.prefix}")
    String transferPrefix;
    private RestTemplate restTemplate;

    public void transfer(String loginFrom, String loginTo,  Currencies currencyFrom, Currencies currencyTo, Long amount) {

        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("currencyTo", currencyTo.name());
        map.add("currencyFrom", currencyFrom.name());
        map.add("amount", amount);
        map.add("loginTo", loginTo);
        System.out.println("loginTo: " + loginTo);
        HttpEntity<MultiValueMap<String,Object>> entity = new HttpEntity<>(map, headers);
        try {
            restTemplate.exchange(String.join("/","http:/",gatewayPrefix, transferPrefix, loginFrom, "transfer"),
                    HttpMethod.POST, entity, Void.class);
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new AccountServiceResponseException(ex.getMessage(), loginFrom);
        }
    }
}
