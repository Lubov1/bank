package ru.yandex.practicum.frontui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotBlank @Size(min = 2, max = 20)
    String password;
    @NotBlank @Size(min = 2, max = 20)
    String login;
}
