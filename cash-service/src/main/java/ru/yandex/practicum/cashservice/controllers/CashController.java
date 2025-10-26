package ru.yandex.practicum.cashservice.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.cashservice.dto.Request;
import ru.yandex.practicum.cashservice.services.CashService;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
public class CashController {
    private final CashService cashService;
    public CashController(CashService cashService) {
        this.cashService = cashService;
    }
    @PostMapping(value = ("/{login}/withdraw"), consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> withdraw(@PathVariable String login, @RequestBody Request request) throws IOException {
        cashService.withdraw(login, request.getAmount(), request.getCurrency());
        return ResponseEntity.ok("Success");
    }

    @PostMapping(value = ("/{login}/deposit"), consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deposit(@PathVariable String login, @RequestBody Request request) throws IOException {
        cashService.deposit(login, request.getAmount(), request.getCurrency());
        return ResponseEntity.ok("Success");
    }
}
