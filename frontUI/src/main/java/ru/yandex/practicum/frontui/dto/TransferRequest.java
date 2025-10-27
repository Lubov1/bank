package ru.yandex.practicum.frontui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferRequest {
    private String loginTo;
    private Currencies currencyFrom;
    private Currencies currencyTo;
    private BigDecimal amount;
}
