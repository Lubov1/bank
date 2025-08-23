package ru.yandex.practicum.accountservice.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import ru.yandex.practicum.accountservice.dto.UserDto;

import java.time.LocalDate;

@Entity
@Data
public class UserDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    String name;
    String password;
    String login;
    LocalDate birthdate;

    public UserDao(String name, String password, String login, LocalDate birthdate) {
        this.name = name;
        this.password = password;
        this.login = login;
        this.birthdate = birthdate;
    }

    public UserDao(UserDto userDto) {
        this.name = userDto.getName();
        this.password = userDto.getPassword();
        this.login = userDto.getLogin();
        this.birthdate = userDto.getBirthdate();
    }

    public UserDao() {

    }
}