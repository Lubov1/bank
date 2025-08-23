package ru.yandex.practicum.gatewayapi.dto;

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