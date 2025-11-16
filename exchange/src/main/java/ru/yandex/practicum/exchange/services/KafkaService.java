package ru.yandex.practicum.exchange.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaService {
    private CurrenciesService currenciesService;
    Logger logger = LoggerFactory.getLogger(KafkaService.class);
    public KafkaService(CurrenciesService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @KafkaListener(topics = "exchanges")
    public void listen(Map<String, Double> exchanges, Acknowledgment ack) {
        logger.info("Received exchanges: " + exchanges);
        ack.acknowledge();
        currenciesService.saveCurrencies(exchanges);
        logger.info("Saved exchanges.");
    }

}
