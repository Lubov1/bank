package ru.yandex.practicum.frontui.controllers;


import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class ExchangeProxyController {
    Logger logger = LoggerFactory.getLogger(ExchangeProxyController.class);
    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final MeterRegistry registry;
    @Value("${exchange.prefix}")
    String exchangePrefix;

    @GetMapping("/exchange")
    public ResponseEntity<?> getExchange() {
        try {
            HttpHeaders h = new HttpHeaders();
            ResponseEntity<String> resp;
            resp = restTemplate.exchange(String.join("/", exchangePrefix, "getCurrencies"),
                    HttpMethod.GET, new HttpEntity<>(h), String.class);
            logger.info(resp.getBody());
            registry.gauge("exchanges_available", 1);
            return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
        } catch (Exception e) {
            registry.gauge("exchanges_available", 0);
            throw e;
        }

    }
}

