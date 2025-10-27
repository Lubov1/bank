package ru.yandex.practicum.frontui.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.dto.TransferRequest;
import ru.yandex.practicum.frontui.exceptions.AccountServiceResponseException;

import java.math.BigDecimal;

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
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<TransferRequest> entity = new HttpEntity<>(new TransferRequest(loginTo,currencyFrom,currencyTo, BigDecimal.valueOf(amount)), headers);
        try {
            restTemplate.exchange(String.join("/","http:/",gatewayPrefix, transferPrefix, loginFrom, "transfer"),
                    HttpMethod.POST, entity, Void.class);
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new AccountServiceResponseException(ex.getMessage(), loginFrom);
        }
    }
}
