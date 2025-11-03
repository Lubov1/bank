package ru.yandex.practicum.exchange.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exchange.services.CurrenciesService;

import java.util.Map;
import java.util.Optional;

@RestController
public class CurrenciesController {
    private CurrenciesService currenciesService;
    Logger logger = LoggerFactory.getLogger(CurrenciesController.class);

    public CurrenciesController(CurrenciesService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @PostMapping("/getCurrencies")
    public ResponseEntity<?> getCurrencies2(@RequestBody Map<String, Double> currencies) {
        logger.info("saving currencies");
        currenciesService.saveCurrencies(currencies);
        logger.info("currencies saved {}", currencies);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getCurrencies")
    public ResponseEntity<?> getCurrencies2() {
        logger.info("getting currencies" + currenciesService.getCurrencies());
        return ResponseEntity.of(Optional.ofNullable(currenciesService.getCurrencies()));
    }
}
