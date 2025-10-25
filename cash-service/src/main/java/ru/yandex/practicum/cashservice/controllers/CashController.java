package ru.yandex.practicum.cashservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.cashservice.services.CashService;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
public class CashController {
    private final CashService cashService;
    public CashController(CashService cashService) {
        this.cashService = cashService;
    }
    @PostMapping(("/{login}/withdraw"))
    public ResponseEntity<?> withdraw(@PathVariable String login, @RequestParam BigDecimal amount,
                                     @RequestParam Currencies currency) throws IOException {
        cashService.withdraw(login, amount, currency);
        return ResponseEntity.ok("Success");
    }

    @PostMapping(("/{login}/deposit"))
    public ResponseEntity<?> deposit(@PathVariable String login, @RequestParam BigDecimal amount,
                                     @RequestParam Currencies currency) throws IOException {
        cashService.deposit(login, amount, currency);
        return ResponseEntity.ok("Success");
    }
}
