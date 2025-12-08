package ru.yandex.practicum.accountservice.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.accountservice.dao.AccountDao;
import ru.yandex.practicum.accountservice.dao.UserDao;
import ru.yandex.practicum.accountservice.exceptions.AccountException;
import ru.yandex.practicum.accountservice.exceptions.AccountNotFoundException;
import ru.yandex.practicum.accountservice.exceptions.InsufficientFundsException;
import ru.yandex.practicum.accountservice.repositories.AccountRepository;
import ru.yandex.practicum.bankautoconfigure.configuration.LoggerHelper;
import ru.yandex.practicum.bankautoconfigure.configuration.NotificationService;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final NotificationService notificationService;
    private final AccountRepository accountRepository;
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(AccountService.class);
    LoggerHelper loggerHelper;

    public AccountService(NotificationService notificationService, AccountRepository accountRepository, UserService userService,
                          LoggerHelper loggerHelper) {
        this.notificationService = notificationService;
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.loggerHelper = loggerHelper;
    }

    @Transactional
    public void deposit(String login, Currencies currency, BigDecimal amount) {
        UserDao userDao = userService.getUserByLogin(login);
        AccountDao accountDao = accountRepository.findByUserAndCurrency(userDao, currency.name()).orElseThrow(()->new AccountNotFoundException(currency.name()+login));
        UserDao user = accountDao.getUser();
        if (!user.getLogin().equals(login)) {
            throw new AccountNotFoundException("account " + currency.name() + " not found");
        }
        accountDao.setBalance(accountDao.getBalance().add(amount));
        loggerHelper.logWithUser(user.getLogin(), ()->
            logger.info(amount + " was deposit from account" + accountDao.getCurrency() + " user" + user.getLogin()));

    }

    @Transactional
    public void withdraw(String login, Currencies currency, BigDecimal amount) {
        UserDao userDao = userService.getUserByLogin(login);
        AccountDao accountDao = accountRepository.findByUserAndCurrency(userDao, currency.name()).orElseThrow(()->new AccountNotFoundException(currency.name() + login));
        UserDao user = accountDao.getUser();
        if (!user.getLogin().equals(login)) {
            loggerHelper.logWithUser(user.getLogin(), ()->logger.warn("different login{}",login));
            throw new AccountNotFoundException("account not found" + currency.name()+login+user.getLogin());
        }
        if (accountDao.getBalance().compareTo(amount) < 0) {
            loggerHelper.logWithUser(user.getLogin(), ()->logger.warn("insufficient funds for account" + currency.name()));
            throw new InsufficientFundsException("insufficient funds for account" + currency.name());
        }
        accountDao.setBalance(accountDao.getBalance().subtract(amount));
        loggerHelper.logWithUser(user.getLogin(), ()->
        logger.info(amount + " was withdrawn from account" + accountDao.getCurrency() + " user" + user.getLogin()));
    }

    @Transactional
    public void transfer(String login, String loginTo, Currencies currencyFrom, Currencies currencyTo, BigDecimal amountFrom,
                         BigDecimal amountTo) {
        withdraw(login, currencyFrom, amountFrom);
        deposit(loginTo, currencyTo, amountTo);
    }

    @Transactional
    public void addAccount(String login, Currencies currency) {
        UserDao userDao = userService.getUserByLogin(login);
        Optional<AccountDao> accountDao = accountRepository.findByUserAndCurrency(userDao, currency.name());
        if (accountDao.isPresent()) {
            throw new AccountException("Account with currency " + currency.name() + "already exists");
        }
        AccountDao account = new AccountDao();
        account.setUser(userDao);
        account.setCurrency(currency.name());
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);
        loggerHelper.logWithUser(login, ()->
            logger.info("Account created: " + account));
        notificationService.sendNotification(login, "account " + currency.name() + " created");
    }

    @Transactional
    public void deleteAccount(String login, Currencies currency) {
        UserDao userDao = userService.getUserByLogin(login);
        Optional<AccountDao> accountDao = accountRepository.findByUserAndCurrency(userDao, currency.name());
        if (accountDao.isEmpty()) {
            throw new AccountException("Account with currency " + currency.name() + " not exists");
        }

        if (accountDao.get().getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new AccountException("Account with currency " + currency.name() + " is not empty");
        }

        accountRepository.delete(accountDao.get());
        loggerHelper.logWithUser(login, ()->
            logger.info("Account is deleted: ", accountDao.get().getCurrency()));
        notificationService.sendNotification(login, "account " + currency.name() + " is deleted");
    }

    public List<AccountDao> getAccounts(String login) {
        UserDao userDao = userService.getUserByLogin(login);
        return accountRepository.findByUser(userDao);
    }
}
