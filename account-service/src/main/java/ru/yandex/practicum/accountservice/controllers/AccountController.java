package ru.yandex.practicum.accountservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.accountservice.dto.AccountDto;
import ru.yandex.practicum.accountservice.dto.CashRequestDto;
import ru.yandex.practicum.accountservice.dto.TransferRequestDto;
import ru.yandex.practicum.accountservice.services.AccountService;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;
    @PostMapping("/{login}/deposit")
    public ResponseEntity<?> deposit(@PathVariable String login, @RequestBody CashRequestDto request) {
        accountService.deposit(login, request.getCurrency(), BigDecimal.valueOf(Double.parseDouble(request.getAmount())));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{login}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable String login, @RequestBody CashRequestDto request) {
        accountService.withdraw(login, request.getCurrency(), BigDecimal.valueOf(Double.parseDouble(request.getAmount())));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{login}/transfer")
    public ResponseEntity<?> transfer(@PathVariable String login, @RequestBody TransferRequestDto request) {
        accountService.transfer(login,request.getLoginTo(), request.getCurrencyFrom(), request.getCurrencyTo(), request.getAmountFrom(), request.getAmountTo());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{login}/addAccount")
    public ResponseEntity<?> addAccount(@PathVariable String login,
                                      @RequestParam Currencies currency) {
        accountService.addAccount(login, currency);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{login}/deleteAccount")
    public ResponseEntity<?> deleteAccount(@PathVariable String login,
                                      @RequestParam Currencies currency) {
        accountService.deleteAccount(login, currency);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{login}/getAccounts")
    public ResponseEntity<?> getAccounts(@PathVariable String login) {
        List<AccountDto> accountDaoList = accountService.getAccounts(login).stream().map(AccountDto::new).toList();
        return ResponseEntity.status(HttpStatus.OK).body(accountDaoList);
    }
}
