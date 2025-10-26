package ru.yandex.practicum.exchange.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exchange.services.CurrenciesService;

import java.util.Map;
import java.util.Optional;

@RestController
public class CurrenciesController {
    private CurrenciesService currenciesService;

    public CurrenciesController(CurrenciesService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @PostMapping("/getCurrencies")
    public ResponseEntity<?> getCurrencies2(@RequestBody Map<String, Double> currencies) {
        System.out.println("saving currencies");
        currenciesService.saveCurrencies(currencies);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getCurrencies")
    public ResponseEntity<?> getCurrencies2() {
        System.out.println("getting currencies" + currenciesService.getCurrencies());
        return ResponseEntity.of(Optional.ofNullable(currenciesService.getCurrencies()));
    }
}
