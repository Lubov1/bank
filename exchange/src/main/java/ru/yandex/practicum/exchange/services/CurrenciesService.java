package ru.yandex.practicum.exchange.services;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.bankautoconfigure.currency.Currency;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CurrenciesService {
    private Map<String,Currency> currencies;
    public void saveCurrencies(Map<String, Double> currencies) {
        this.currencies = currencies.entrySet().stream().map(Currency::new).collect(Collectors.toMap(Currency::getName, Function.identity()));
    }

    public Map<String,Currency> getCurrencies() {
        return currencies;
    }
}
