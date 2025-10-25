package ru.yandex.practicum.accountservice.dto;

import lombok.Data;

@Data
public class CredentialsDto {
    String login;
    String password;
}
