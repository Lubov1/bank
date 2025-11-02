package ru.yandex.practicum.frontui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.services.CashService;

@Controller
public class CashController {
    Logger logger = LoggerFactory.getLogger(CashController.class);

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    private final CashService cashService;


    @PostMapping("/cash/{login}")
    public String deposit(@PathVariable("login") String login, @RequestParam Long amount, @RequestParam Currencies currency,
                           @RequestParam String action) {
        logger.info(action);
        switch (action) {
            case "PUT" -> cashService.deposit(login, amount, currency);
            case "GET" -> cashService.withdraw(login, amount, currency);
            default -> throw new RuntimeException("Invalid action");
        }
        return "redirect:/main/"+login;
    }

}
