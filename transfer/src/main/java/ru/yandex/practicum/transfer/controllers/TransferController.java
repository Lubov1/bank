package ru.yandex.practicum.transfer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.transfer.services.TransferService;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@AllArgsConstructor
public class TransferController {
    private final TransferService transferService;
    @PostMapping("/{login}/transfer")
    public ResponseEntity<?> transfer(@RequestParam String loginTo, @RequestParam Currencies currencyFrom, @RequestParam Currencies currencyTo, @RequestParam BigDecimal amount,
                                      @PathVariable String login) throws IOException {
        transferService.transfer(login, loginTo, currencyFrom, currencyTo, amount);
        return ResponseEntity.ok().build();
    }
}
