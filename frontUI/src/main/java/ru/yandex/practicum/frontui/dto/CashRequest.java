package ru.yandex.practicum.frontui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CashRequest {
    private BigDecimal amount;
    private Currencies currency;
}
