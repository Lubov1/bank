package ru.yandex.practicum.cashservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.configuration.LoggerHelper;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.cashservice.dto.CashRequestDto;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class AccountService {

    @Value("${accounts.prefix}")
    private String accountPrefix;

    private RestTemplate restTemplate;
    Logger logger = LoggerFactory.getLogger(AccountService.class);
    LoggerHelper loggerHelper;

    public AccountService(RestTemplate restTemplate, LoggerHelper loggerHelper) {
        this.restTemplate = restTemplate;
        this.loggerHelper = loggerHelper;
    }

    public void withdraw(String login, BigDecimal amount, Currencies currency) throws IOException {
        loggerHelper.logWithUser(login, ()->
                logger.info("withdrawing {} to {}", amount, currency));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CashRequestDto> entity =
                new HttpEntity<>(new CashRequestDto(currency.name(), amount.toString()), headers);
        ResponseEntity<Void> response;
        response = restTemplate.exchange(String.join("/", accountPrefix, login, "withdraw"),
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
        loggerHelper.logWithUser(login, ()->
                logger.info("depositing {} to {}", amount, currency));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CashRequestDto> entity =
                new HttpEntity<>(new CashRequestDto(currency.name(), amount.toString()), headers);
        ResponseEntity<Void> response;
        response = restTemplate.exchange(String.join("/", accountPrefix, login, "deposit"),
                    HttpMethod.POST, entity, Void.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new IOException("account not found " + currency);
            }
            throw new IOException("error with " + currency);
        }
    }
}
