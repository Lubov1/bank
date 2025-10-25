package ru.yandex.practicum.cashservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

@Service
public class AccountService {
    @Value("${gateway.prefix}")
    private String gatewayApiPrefix;

    @Value("${accounts.prefix}")
    private String accountPrefix;

    private RestTemplate restTemplate;

    public AccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void withdraw(String login, BigDecimal amount, Currencies currency) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("currency", currency.name());
        body.add("amount", amount.toString());

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Void> response = restTemplate.exchange(String.join("/","http:/",gatewayApiPrefix, accountPrefix, login, "withdraw"),
                HttpMethod.POST, entity, Void.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new IOException("account not found " + currency);
            }
            if (response.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
                throw new IOException("not enough money on the account " + currency);
            }
            throw new IOException("error with " + currency);
        }
    }

    public void deposit(String login, BigDecimal amount, Currencies currency) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("currency", currency.name());
        body.add("amount", amount.toString());

        HttpEntity<MultiValueMap<String,Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Void> response = restTemplate.exchange(String.join("/","http:/",gatewayApiPrefix, accountPrefix, login, "deposit"),
                HttpMethod.POST, entity, Void.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new IOException("account not found " + currency);
            }
            throw new IOException("error with " + currency);
        }
    }
}
