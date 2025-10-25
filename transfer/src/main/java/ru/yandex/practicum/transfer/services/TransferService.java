package ru.yandex.practicum.transfer.services;

import com.nimbusds.jose.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.configuration.BlockerService;
import ru.yandex.practicum.bankautoconfigure.configuration.NotificationService;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.bankautoconfigure.currency.Currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class TransferService {
    @Value("${gateway.prefix}")
    private String gatewayApiPrefix;

    @Value("${accounts.prefix}")
    private String accountPrefix;

    @Value("${exchange.prefix}")
    private String exchangePrefix;
    private RestTemplate restTemplate;
    private BlockerService blockerService;
    private NotificationService notificationService;

    public TransferService(@Autowired RestTemplate restTemplate, @Autowired BlockerService blockerService,
                           @Autowired NotificationService notificationService) {
        this.restTemplate = restTemplate;
        this.blockerService = blockerService;
        this.notificationService = notificationService;
    }

    public void transfer(String login, String loginTo, Currencies currencyFrom, Currencies currencyTo, BigDecimal amount) throws IOException {
        BigDecimal amountTo = amount;
        if (!currencyFrom.equals(currencyTo)) {
            System.out.println("Currencies are not equal");
            amountTo = getAmount(Pair.of(currencyFrom,currencyTo), amount);
        }
        blockerService.check(login, amount);
        transfer(login, loginTo, currencyFrom, currencyTo, amount, amountTo);
        notificationService.sendNotification(login, amount +" was transferred to " + loginTo);
    }

    public void transfer(String login, String loginTo, Currencies currencyFrom, Currencies currencyTo, BigDecimal amountFrom, BigDecimal amountTo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("loginTo", loginTo);
        params.add("currencyFrom", currencyFrom);
        params.add("currencyTo", currencyTo);
        params.add("amountFrom", amountFrom);
        params.add("amountTo", amountTo);
        restTemplate.exchange(String.join("/","http:/", gatewayApiPrefix, accountPrefix, login, "transfer"),
                HttpMethod.POST, new HttpEntity<>(params, headers), new ParameterizedTypeReference<>() {});
    }

    private BigDecimal getAmount(Pair<Currencies, Currencies> currencies, BigDecimal amount) {
        HttpHeaders h = new HttpHeaders();
        ResponseEntity<Map<String,Currency>> resp = restTemplate.exchange(
                String.join("/","http:/", gatewayApiPrefix, exchangePrefix, "getCurrencies"),
                HttpMethod.GET, new HttpEntity<>(h), new ParameterizedTypeReference<>() {});
        Map<String,Currency> exchangeCurrencies = resp.getBody();
        return amount
                .multiply(exchangeCurrencies.get(currencies.getLeft().name()).getValue())
                .divide(exchangeCurrencies.get(currencies.getRight().name()).getValue());
    }
}
