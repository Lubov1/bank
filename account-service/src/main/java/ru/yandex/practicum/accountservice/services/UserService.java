package ru.yandex.practicum.accountservice.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.accountservice.dao.UserDao;
import ru.yandex.practicum.accountservice.dto.UserDto;
import ru.yandex.practicum.accountservice.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Autowired
    private final NotificationService notificationService;
    @Transactional
    public void createUser(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(new UserDao(user));
        notificationService.sendNotification(user.getLogin(), "account is created");
    }

    @Transactional
    public void editUserPassword(String login, String password) {
        UserDao user = userRepository.findByLogin(login);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        notificationService.sendNotification(user.getLogin(), "password updated");
    }
}
