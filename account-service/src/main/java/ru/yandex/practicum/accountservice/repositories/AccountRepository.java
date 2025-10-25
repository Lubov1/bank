package ru.yandex.practicum.accountservice.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.practicum.accountservice.dao.AccountDao;
import ru.yandex.practicum.accountservice.dao.UserDao;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<AccountDao, Long> {
    Optional<AccountDao> findByCurrency(String currency);

    Optional<AccountDao> findByUserAndCurrency(UserDao userDao, String name);

    List<AccountDao> findByUser(UserDao userDao);
}
