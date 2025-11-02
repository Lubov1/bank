package ru.yandex.practicum.cashservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CashRequestDto {
    private String currency;
    private String amount;

}
