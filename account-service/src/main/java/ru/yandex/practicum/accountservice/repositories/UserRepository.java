package ru.yandex.practicum.accountservice.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.practicum.accountservice.dao.UserDao;
import ru.yandex.practicum.accountservice.dto.UserDto;

public interface UserRepository extends CrudRepository<UserDao, Long> {
    UserDao findByLogin(String login);
}
