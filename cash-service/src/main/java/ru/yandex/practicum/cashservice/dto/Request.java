package ru.yandex.practicum.cashservice.dto;

import lombok.Data;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.math.BigDecimal;

@Data
public class Request {
    private BigDecimal amount;
    private Currencies currency;
}
