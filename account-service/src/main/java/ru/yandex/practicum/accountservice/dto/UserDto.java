package ru.yandex.practicum.accountservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    String name;
    String password;
    String login;
    String secondPassword;
    LocalDate birthdate;
}