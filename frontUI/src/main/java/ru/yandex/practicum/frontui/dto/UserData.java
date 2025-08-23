package ru.yandex.practicum.frontui.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserData {
    String name;
    String password;
    String login;
    String secondPassword;
    LocalDate birthdate;
}
