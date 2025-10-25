package ru.yandex.practicum.accountservice.dto;

import lombok.Data;
import ru.yandex.practicum.accountservice.dao.AccountDao;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AccountDto implements Serializable {
    private Long number;
    private BigDecimal balance;
    private String currency;

    public AccountDto(AccountDao accountDao) {
        this.number = accountDao.getNumber();
        this.balance = accountDao.getBalance();
        this.currency = accountDao.getCurrency();
    }
}
