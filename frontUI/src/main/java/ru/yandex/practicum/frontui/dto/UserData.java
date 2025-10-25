package ru.yandex.practicum.frontui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserData {
    @NotBlank @Size(min = 2, max = 50)
    String name;
    @NotBlank @Size(min = 2, max = 50)
    String password;
    @NotBlank @Size(min = 2, max = 50)
    String login;
    @NotBlank @Size(min = 2, max = 50)
    String secondPassword;
    LocalDate birthdate;
}
