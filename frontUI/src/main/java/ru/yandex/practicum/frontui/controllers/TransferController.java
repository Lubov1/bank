package ru.yandex.practicum.frontui.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.services.TransferService;

import java.math.BigDecimal;

@Controller
@AllArgsConstructor
public class TransferController {
    private TransferService transferService;
    @PostMapping("/{login}/transfer")
    public String transfer(@RequestParam String loginTo, @RequestParam Currencies currencyFrom, @RequestParam Currencies currencyTo
            , @RequestParam Long amount, @PathVariable String login) {
        transferService.transfer(login, loginTo, currencyFrom, currencyTo, amount);
        return "redirect:/main/"+login;
    }
}
