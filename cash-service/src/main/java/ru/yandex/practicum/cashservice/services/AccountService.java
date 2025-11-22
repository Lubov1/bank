package ru.yandex.practicum.cashservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.cashservice.dto.CashRequestDto;

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
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CashRequestDto> entity =
                new HttpEntity<>(new CashRequestDto(currency.name(), amount.toString()), headers);

//        ResponseEntity<Void> response = restTemplate.exchange(String.join("/","http:/",gatewayApiPrefix, accountPrefix, login, "withdraw"),
//                HttpMethod.POST, entity, Void.class);
        ResponseEntity<Void> response = restTemplate.exchange(String.join("/","http:/", accountPrefix+":8080", login, "withdraw"),
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
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CashRequestDto> entity =
                new HttpEntity<>(new CashRequestDto(currency.name(), amount.toString()), headers);

//        ResponseEntity<Void> response = restTemplate.exchange(String.join("/","http:/",gatewayApiPrefix, accountPrefix, login, "deposit"),
//                HttpMethod.POST, entity, Void.class);
        ResponseEntity<Void> response = restTemplate.exchange(String.join("/","http:/", accountPrefix+":8080", login, "deposit"),
                HttpMethod.POST, entity, Void.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new IOException("account not found " + currency);
            }
            throw new IOException("error with " + currency);
        }
    }
}
