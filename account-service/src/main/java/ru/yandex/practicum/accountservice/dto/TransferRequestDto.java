package ru.yandex.practicum.accountservice.dto;

import lombok.Data;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {
    BigDecimal amountFrom;
    BigDecimal amountTo;
    Currencies currencyFrom;
    Currencies currencyTo;
    String loginTo;
}
