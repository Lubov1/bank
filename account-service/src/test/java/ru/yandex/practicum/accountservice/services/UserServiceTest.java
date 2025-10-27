package ru.yandex.practicum.accountservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.yandex.practicum.accountservice.dao.UserDao;
import ru.yandex.practicum.accountservice.exceptions.UserNotFoundException;
import ru.yandex.practicum.accountservice.repositories.UserRepository;
import ru.yandex.practicum.bankautoconfigure.configuration.NotificationService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    NotificationService notificationService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Test
    void updatesPasswordAndNotifies() {
        var login = "alice";
        var raw = "p@ss";
        var enc = "ENCODED";
        var user = new UserDao(login, "old", login, LocalDate.of(1990, 1, 1));
        when(userRepository.findByLogin(eq(login))).thenReturn(user);
        when(passwordEncoder.encode(raw)).thenReturn(enc);

        userService.editUserPassword(login, raw);

        ArgumentCaptor<UserDao> captor = ArgumentCaptor.forClass(UserDao.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo(enc);
        verify(notificationService).sendNotification(login, "password updated");
        verifyNoMoreInteractions(userRepository, passwordEncoder, notificationService);
    }

    @Test
    void updatesProfileAndNotifies() {
        var login = "bob";
        var user = new UserDao(login, "pwd", login, LocalDate.of(1991, 2, 2));
        when(userRepository.findByLogin(eq(login))).thenReturn(user);

        userService.editUserAccounts(login, "Bobby", LocalDate.of(1992, 3, 3));

        ArgumentCaptor<UserDao> captor = ArgumentCaptor.forClass(UserDao.class);
        verify(userRepository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Bobby");
        assertThat(saved.getBirthdate()).isEqualTo(LocalDate.of(1992, 3, 3));
        verify(notificationService).sendNotification(login, "user accounts updated");
        verifyNoMoreInteractions(userRepository, passwordEncoder, notificationService);
    }

    @Test
    void getByLoginNotFound() {
        var login = "ghost";
        when(userRepository.findByLogin(login)).thenReturn(null);
        assertThatThrownBy(() -> userService.getUserByLogin(login))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("user " + login + " not found");
        verify(userRepository).findByLogin(login);
        verifyNoMoreInteractions(userRepository, passwordEncoder, notificationService);
    }
}
