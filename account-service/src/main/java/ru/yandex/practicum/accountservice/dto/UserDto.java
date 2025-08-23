package ru.yandex.practicum.accountservice.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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