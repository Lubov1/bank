package ru.yandex.practicum.frontui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    String password;
    String login;
}
