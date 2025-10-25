package ru.yandex.practicum.frontui.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.services.CashService;

@Controller
@AllArgsConstructor
public class CashController {
    private final CashService cashService;
//    @PostMapping("/{login}/withdraw")
//    public String withdraw(@PathVariable("login") String login, @RequestParam Long amount,
//                           @RequestParam Currencies currency) {
//        cashService.withdraw(login, amount, currency);
//        return "redirect:main";
//    }
//
//    @PostMapping("/{login}/deposit")
//    public String deposit(@PathVariable("login") String login, @RequestParam Long amount,
//                           @RequestParam Currencies currency) {
//        cashService.deposit(login, amount, currency);
//        return "redirect:/main/"+login;
//    }


    @PostMapping("/cash/{login}")
    public String deposit(@PathVariable("login") String login, @RequestParam Long amount, @RequestParam Currencies currency,
                           @RequestParam String action) {
        System.out.println(action);
        switch (action) {
            case "PUT" -> cashService.deposit(login, amount, currency);
            case "GET" -> cashService.withdraw(login, amount, currency);
            default -> throw new RuntimeException("Invalid action");
        }
        return "redirect:/main/"+login;
    }

}
