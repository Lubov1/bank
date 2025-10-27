package ru.yandex.practicum.accountservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.accountservice.dao.AccountDao;
import ru.yandex.practicum.accountservice.dao.UserDao;
import ru.yandex.practicum.accountservice.exceptions.AccountException;
import ru.yandex.practicum.accountservice.exceptions.AccountNotFoundException;
import ru.yandex.practicum.accountservice.exceptions.InsufficientFundsException;
import ru.yandex.practicum.accountservice.repositories.AccountRepository;
import ru.yandex.practicum.bankautoconfigure.configuration.NotificationService;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserService userService;

    @InjectMocks
    AccountService accountService;

    @Test
    void depositTest() {
        String login = "login";
        Currencies currency = Currencies.USD;
        UserDao userDao = new UserDao(login, "password",login, LocalDate.of(1999,10,16) );
        AccountDao accountDao = new AccountDao(10L, BigDecimal.TWO, Currencies.USD.name(), userDao);
        when(userService.getUserByLogin(login)).thenReturn(userDao);

        when(accountRepository.findByUserAndCurrency(eq(userDao), eq(Currencies.USD.name())))
                .thenReturn(Optional.of(accountDao));
        accountService.deposit(login, currency, BigDecimal.TEN);

        assertThat(accountDao.getBalance()).isEqualByComparingTo("12");
        verify(accountRepository).findByUserAndCurrency(userDao, Currencies.USD.name());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void withdrawInsufficientFunds() {
        var login = "alice";
        UserDao userDao = new UserDao(login, "password",login, LocalDate.of(1999,10,16) );
        AccountDao accountDao = new AccountDao(1L, new BigDecimal("5.00"), Currencies.USD.name(), userDao);
        when(userService.getUserByLogin(login)).thenReturn(userDao);
        when(accountRepository.findByUserAndCurrency(userDao, Currencies.USD.name())).thenReturn(Optional.of(accountDao));
        assertThatThrownBy(() -> accountService.withdraw(login, Currencies.USD, new BigDecimal("10.00")))
                .isInstanceOf(InsufficientFundsException.class);
        assertThat(accountDao.getBalance()).isEqualByComparingTo("5.00");
        verify(userService).getUserByLogin(login);
        verify(accountRepository).findByUserAndCurrency(userDao, Currencies.USD.name());
        verifyNoMoreInteractions(accountRepository, userService, notificationService);
    }

    @Test
    void withdrawMismatchedOwner() {
        var login = "alice";
        UserDao userDao = new UserDao(login, "password",login, LocalDate.of(1999,10,16) );
        UserDao userBob = new UserDao("bob", "password","bob", LocalDate.of(1999,10,16) );

        var a = new AccountDao(1L, new BigDecimal("20.00"), Currencies.USD.name(), userBob);
        when(userService.getUserByLogin(login)).thenReturn(userDao);
        when(accountRepository.findByUserAndCurrency(userDao, Currencies.USD.name())).thenReturn(Optional.of(a));
        assertThatThrownBy(() -> accountService.withdraw(login, Currencies.USD, new BigDecimal("1.00")))
                .isInstanceOf(AccountNotFoundException.class);
        verify(userService).getUserByLogin(login);
        verify(accountRepository).findByUserAndCurrency(userDao, Currencies.USD.name());
        verifyNoMoreInteractions(accountRepository, userService, notificationService);
    }

    @Test
    void transferFundsSameCurrency() {
        var fromLogin = "alice";
        var toLogin = "bob";
        UserDao from = new UserDao(fromLogin, "password",fromLogin, LocalDate.of(1999,10,16) );
        UserDao to = new UserDao(toLogin, "password",toLogin, LocalDate.of(1999,10,16) );
        from.setId(1L);
        to.setId(2L);

        var accFrom = new AccountDao(1L, new BigDecimal("20.00"), Currencies.USD.name(), from);
        var accTo = new AccountDao(2L, new BigDecimal("3.00"), Currencies.USD.name(), to);

        when(userService.getUserByLogin(eq(fromLogin))).thenReturn(from);
        when(userService.getUserByLogin(eq(toLogin))).thenReturn(to);
        when(accountRepository.findByUserAndCurrency(
                argThat(u -> u != null && fromLogin.equals(u.getLogin())),
                eq(Currencies.USD.name()))
        ).thenReturn(Optional.of(accFrom));
        when(accountRepository.findByUserAndCurrency(
                argThat(u -> u != null && toLogin.equals(u.getLogin())),
                eq(Currencies.USD.name()))
        ).thenReturn(Optional.of(accTo));
        accountService.transfer(fromLogin, toLogin, Currencies.USD, Currencies.USD, new BigDecimal("10.00"), new BigDecimal("10.00"));
        assertThat(accFrom.getBalance()).isEqualByComparingTo("10.00");
        assertThat(accTo.getBalance()).isEqualByComparingTo("13.00");
        verify(userService, times(2)).getUserByLogin(anyString());
        verify(accountRepository, times(2)).findByUserAndCurrency(any(UserDao.class), eq(Currencies.USD.name()));
//        verifyNoMoreInteractions(accountRepository, userService, notificationService);
    }

    @Test
    void transferInsufficientFundsNoCredit() {
        var fromLogin = "alice";
        var toLogin = "bob";
        UserDao from = new UserDao(fromLogin, "password",fromLogin, LocalDate.of(1999,10,16) );
        UserDao to = new UserDao(toLogin, "password",toLogin, LocalDate.of(1999,10,16) );
        from.setId(1L);
        to.setId(2L);
        var accFrom = new AccountDao(1L, new BigDecimal("5.00"), Currencies.USD.name(), from);
        var accTo = new AccountDao(2L, new BigDecimal("3.00"), Currencies.USD.name(), to);
        when(userService.getUserByLogin(eq(fromLogin))).thenReturn(from);
//        when(accountRepository.findByUserAndCurrency(eq(from), eq(Currencies.USD.name()))).thenReturn(Optional.of(accFrom));
//        when(accountRepository.findByUserAndCurrency(eq(to), eq(Currencies.USD.name()))).thenReturn(Optional.of(accTo));

//        when(userService.getUserByLogin(fromLogin)).thenReturn(from);
//        when(userService.getUserByLogin(toLogin)).thenReturn(to);

        when(accountRepository.findByUserAndCurrency(
                argThat(u -> u != null && fromLogin.equals(u.getLogin())),
                eq(Currencies.USD.name()))
        ).thenReturn(Optional.of(accFrom));

        assertThatThrownBy(() -> accountService.transfer(fromLogin, toLogin, Currencies.USD, Currencies.USD, new BigDecimal("10.00"), new BigDecimal("10.00")))
                .isInstanceOf(InsufficientFundsException.class);
        assertThat(accTo.getBalance()).isEqualByComparingTo("3.00");
        verify(userService, times(1)).getUserByLogin(anyString());
        verify(accountRepository, times(1)).findByUserAndCurrency(any(UserDao.class), eq(Currencies.USD.name()));
        verifyNoMoreInteractions(accountRepository, userService, notificationService);
    }

    @Test
    void emplaceZeroedLedgerAndNotify() {
        var login = "alice";
        UserDao userDao = new UserDao(login, "password",login, LocalDate.of(1999,10,16) );
        when(userService.getUserByLogin(login)).thenReturn(userDao);
        when(accountRepository.findByUserAndCurrency(userDao, Currencies.RUB.name())).thenReturn(Optional.empty());
        ArgumentCaptor<AccountDao> captor = ArgumentCaptor.forClass(AccountDao.class);
        accountService.addAccount(login, Currencies.RUB);
        verify(accountRepository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.getUser()).isEqualTo(userDao);
        assertThat(saved.getCurrency()).isEqualTo(Currencies.RUB.name());
        assertThat(saved.getBalance()).isEqualByComparingTo("0");
        verify(notificationService).sendNotification(login, "account RUB created");
        verifyNoMoreInteractions(accountRepository, userService, notificationService);
    }

    @Test
    void preventDupOnEmplace() {
        var login = "alice";
        UserDao userDao = new UserDao(login, "password",login, LocalDate.of(1999,10,16) );
        AccountDao existing = new AccountDao(1L, BigDecimal.ZERO, Currencies.RUB.name(), userDao);
        when(userService.getUserByLogin(login)).thenReturn(userDao);
        when(accountRepository.findByUserAndCurrency(userDao, Currencies.RUB.name())).thenReturn(Optional.of(existing));
        assertThatThrownBy(() -> accountService.addAccount(login, Currencies.RUB)).isInstanceOf(AccountException.class);
        verify(accountRepository, never()).save(any());
        verify(notificationService, never()).sendNotification(anyString(), anyString());
        verifyNoMoreInteractions(accountRepository, userService, notificationService);
    }

    @Test
    void deleteZeroedLedgerAndNotify() {
        var login = "alice";
        UserDao userDao = new UserDao(login, "password",login, LocalDate.of(1999,10,16) );
        var a = new AccountDao(1L, BigDecimal.ZERO, Currencies.USD.name(), userDao);
        when(userService.getUserByLogin(login)).thenReturn(userDao);
        when(accountRepository.findByUserAndCurrency(userDao, Currencies.USD.name())).thenReturn(Optional.of(a));
        accountService.deleteAccount(login, Currencies.USD);
        verify(accountRepository).delete(a);
        verify(notificationService).sendNotification(login, "account USD is deleted");
        verifyNoMoreInteractions(accountRepository, userService, notificationService);
    }

    @Test
    void deleteNonexistentThrows() {
        var login = "alice";
        UserDao userDao = new UserDao(login, "password",login, LocalDate.of(1999,10,16) );
        when(userService.getUserByLogin(login)).thenReturn(userDao);
        when(accountRepository.findByUserAndCurrency(userDao, Currencies.USD.name())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.deleteAccount(login, Currencies.USD)).isInstanceOf(AccountException.class);
        verify(accountRepository, never()).delete(any());
        verify(notificationService, never()).sendNotification(anyString(), anyString());
        verifyNoMoreInteractions(accountRepository, userService, notificationService);
    }

    @Test
    void deleteNonZeroBalanceThrows() {
        var login = "alice";
        UserDao userDao = new UserDao(login, "password",login, LocalDate.of(1999,10,16) );
        var a = new AccountDao(1L, new BigDecimal("1.00"), Currencies.USD.name(), userDao);
        when(userService.getUserByLogin(login)).thenReturn(userDao);
        when(accountRepository.findByUserAndCurrency(userDao, Currencies.USD.name())).thenReturn(Optional.of(a));
        assertThatThrownBy(() -> accountService.deleteAccount(login, Currencies.USD)).isInstanceOf(AccountException.class);
        verify(accountRepository, never()).delete(any());
        verify(notificationService, never()).sendNotification(anyString(), anyString());
        verifyNoMoreInteractions(accountRepository, userService, notificationService);
    }
}
