package ru.yandex.practicum.frontui.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.TokenProvider;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class ExchangeProxyController {
    @Autowired
    private final RestTemplate restTemplate;

    @GetMapping("/exchange")
    public ResponseEntity<?> getExchange() {
        HttpHeaders h = new HttpHeaders();
        ResponseEntity<String> resp = restTemplate.exchange(
                "http://gateway/exchange/getCurrencies",
                HttpMethod.GET, new HttpEntity<>(h), String.class);
        System.out.println(resp.getBody());
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }
}

