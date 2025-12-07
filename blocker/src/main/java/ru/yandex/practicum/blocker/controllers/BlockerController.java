package ru.yandex.practicum.blocker.controllers;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.bankautoconfigure.configuration.LoggerHelper;

import java.math.BigDecimal;

@RestController
public class BlockerController {
    MeterRegistry meterRegistry;
    Logger logger = LoggerFactory.getLogger(BlockerController.class);
    LoggerHelper loggerHelper;

    public BlockerController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostMapping("/{login}/check")
    public ResponseEntity<?> check(@PathVariable String login, @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(1000))>0) {
            Counter counter = Counter.builder("transfer_blocked")
                    .tag("login", login)
                    .register(meterRegistry);
            counter.increment();
            loggerHelper.logWithUser(login, ()->
                    logger.error("operation for user " + login + " was blocked (amount is bigger than 1000 not acceptable)"));
            return ResponseEntity.badRequest().body("operation for user " + login + " was blocked (amount is bigger than 1000 not acceptable)");
        }
        return ResponseEntity.ok().build();
    }
}
