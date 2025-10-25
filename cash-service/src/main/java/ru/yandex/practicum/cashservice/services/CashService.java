package ru.yandex.practicum.cashservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.configuration.BlockerService;
import ru.yandex.practicum.bankautoconfigure.configuration.NotificationService;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class CashService {
    private AccountService accountService;
    private BlockerService blockerService;
    private NotificationService notificationService;

    public CashService(AccountService accountService, BlockerService blockerService,
                       NotificationService notificationService) {
        this.accountService = accountService;
        this.blockerService = blockerService;
        this.notificationService = notificationService;
    }

    public void withdraw(String login, BigDecimal amount, Currencies currency) throws IOException {
        blockerService.check(login, amount);
        accountService.withdraw(login, amount, currency);
        notificationService.sendNotification(login, amount +" was withdrawn");
    }

    public void deposit(String login, BigDecimal amount, Currencies currency) throws IOException {
        blockerService.check(login, amount);
        accountService.deposit(login, amount, currency);
        notificationService.sendNotification(login, amount +" was deposited");
    }
}
