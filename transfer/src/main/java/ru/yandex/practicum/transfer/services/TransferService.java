package ru.yandex.practicum.transfer.services;

import com.nimbusds.jose.util.Pair;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.configuration.BlockerService;
import ru.yandex.practicum.bankautoconfigure.configuration.NotificationService;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.bankautoconfigure.currency.Currency;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class TransferService {
    Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Value("${gateway.prefix}")
    private String gatewayApiPrefix;

    @Value("${accounts.prefix}")
    private String accountPrefix;
    @Value("${docker:false}")
    boolean docker;

    @Value("${exchange.prefix}")
    private String exchangePrefix;
    private final RestTemplate restTemplate;
    private final BlockerService blockerService;
    private final NotificationService notificationService;
    MeterRegistry meterRegistry;

    public TransferService(@Autowired RestTemplate restTemplate, @Autowired BlockerService blockerService,
                           @Autowired NotificationService notificationService, MeterRegistry meterRegistry) {
        this.restTemplate = restTemplate;
        this.blockerService = blockerService;
        this.notificationService = notificationService;
        this.meterRegistry = meterRegistry;
    }

    public void transfer(String login, String loginTo, Currencies currencyFrom, Currencies currencyTo, BigDecimal amount) throws IOException {
        try {
            BigDecimal amountTo = amount;
            if (!currencyFrom.equals(currencyTo)) {
                logger.info("Currencies are not equal");
                amountTo = getAmount(Pair.of(currencyFrom, currencyTo), amount);
            }
            blockerService.check(login, amount);
            transfer(login, loginTo, currencyFrom, currencyTo, amount, amountTo);
            notificationService.sendNotification(login, amount + " was transferred to " + loginTo);
        } catch (Exception e) {
            meterRegistry.counter("error_transfer", "loginFrom", login,"loginTo", loginTo,
                    "currencyFrom", currencyFrom.name(), "currencyTo", currencyTo.name()).increment();
            throw e;
        }
    }

    public void transfer(String login, String loginTo, Currencies currencyFrom, Currencies currencyTo, BigDecimal amountFrom, BigDecimal amountTo) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);        TransferRequestDto transferRequestDto =
                new TransferRequestDto(amountFrom, amountTo, currencyFrom, currencyTo, loginTo);
        if (!docker) {
            restTemplate.exchange(String.join("/", "http:/", gatewayApiPrefix, accountPrefix, login, "transfer"),
                    HttpMethod.POST, new HttpEntity<>(transferRequestDto, headers), new ParameterizedTypeReference<>() {
                    });
        } else {
            restTemplate.exchange(String.join("/","http:/",  accountPrefix+":8080", login, "transfer"),
                        HttpMethod.POST, new HttpEntity<>(transferRequestDto, headers), new ParameterizedTypeReference<>() {});
        }
    }

    private BigDecimal getAmount(Pair<Currencies, Currencies> currencies, BigDecimal amount) {
        HttpHeaders h = new HttpHeaders();
        ResponseEntity<Map<String, Currency>> resp;
        if (!docker) {
            resp = restTemplate.exchange(
                    String.join("/", "http:/", gatewayApiPrefix, exchangePrefix, "getCurrencies"),
                    HttpMethod.GET, new HttpEntity<>(h), new ParameterizedTypeReference<>() {
                    });
        } else {
            resp = restTemplate.exchange(
                    String.join("/","http:/", exchangePrefix+":8080", "getCurrencies"),
                    HttpMethod.GET, new HttpEntity<>(h), new ParameterizedTypeReference<>() {});
        }
        Map<String,Currency> exchangeCurrencies = resp.getBody();
        if (exchangeCurrencies==null) {
            throw new RuntimeException("ExchangeCurrencies is null");
        }
        if (exchangeCurrencies.get(currencies.getRight().name()).getValue().equals(BigDecimal.ZERO)) {
            throw new RuntimeException("ExchangeCurrencies value " + currencies.getRight().name() + "  is zero");
        }
        return amount
                .multiply(exchangeCurrencies.get(currencies.getLeft().name()).getValue())
                .divide(exchangeCurrencies.get(currencies.getRight().name()).getValue(), RoundingMode.HALF_EVEN);
    }
}
