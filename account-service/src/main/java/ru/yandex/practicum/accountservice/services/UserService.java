package ru.yandex.practicum.accountservice.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.accountservice.dao.UserDao;
import ru.yandex.practicum.accountservice.dto.CredentialsDto;
import ru.yandex.practicum.accountservice.dto.UserDto;
import ru.yandex.practicum.accountservice.exceptions.IncorrectPasswordException;
import ru.yandex.practicum.accountservice.exceptions.UserExistsException;
import ru.yandex.practicum.accountservice.exceptions.UserNotFoundException;
import ru.yandex.practicum.accountservice.repositories.UserRepository;
import ru.yandex.practicum.bankautoconfigure.configuration.NotificationService;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserService {
    PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Autowired
    private final NotificationService notificationService;
    @Transactional
    public void createUser(UserDto user) {
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new UserExistsException("user " + user.getLogin() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(new UserDao(user));
        notificationService.sendNotification(user.getLogin(), "account is created");
    }

    @Transactional
    public void editUserPassword(String login, String password) {
        UserDao user = getUserByLogin(login);

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        notificationService.sendNotification(user.getLogin(), "password updated");
    }

    @Transactional
    public void editUserAccounts(String login, String name, LocalDate birthdate) {
        UserDao user = getUserByLogin(login);
        user.setName(name);
        user.setBirthdate(birthdate);
        userRepository.save(user);
        notificationService.sendNotification(user.getLogin(), "user accounts updated");
    }

    @Transactional(readOnly = true)
    public UserDao getUserByLogin(String login) {
        UserDao userDao = userRepository.findByLogin(login);
        if (userDao == null) {
            throw new UserNotFoundException("user " + login + " not found");
        }
        return userDao;
    }

    @Transactional(readOnly = true)
    public void loginUser(CredentialsDto credentials) {
        UserDao userDao = getUserByLogin(credentials.getLogin());
        if (userDao == null) {
            throw new UserNotFoundException("user " + credentials.getLogin() + " not found");
        }
        if (!passwordEncoder.matches(credentials.getPassword(), userDao.getPassword())) {
            throw new IncorrectPasswordException();
        }
    }
}
