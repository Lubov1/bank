package ru.yandex.practicum.accountservice.dto;

import lombok.Data;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

@Data
public class CashRequestDto {
    private String amount;
    private Currencies currency;
}
