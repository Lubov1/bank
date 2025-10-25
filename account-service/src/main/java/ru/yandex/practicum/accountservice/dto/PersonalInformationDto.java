package ru.yandex.practicum.accountservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonalInformationDto {
    String name;
    LocalDate birthday;
}
