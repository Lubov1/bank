package ru.yandex.practicum.exchangegenerator.services;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.exchangegenerator.Currencies;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExchangeGenerator {
    Map<String, Double> exchanges = new HashMap();

    public Map<String, Double> getExchanges() {
        exchanges.clear();
        for (Currencies currency : Currencies.values()) {
            exchanges.put(currency.name(), switch (currency) {
                case USD ->
                    ThreadLocalRandom.current().nextDouble(75, 85);
                case CNY ->
                    ThreadLocalRandom.current().nextDouble(9, 15);
                case RUB ->
                    1d;
            });
        }
        return exchanges;
    }
}
