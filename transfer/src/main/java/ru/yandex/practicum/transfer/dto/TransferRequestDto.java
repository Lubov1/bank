package ru.yandex.practicum.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferRequestDto {
    BigDecimal amountFrom;
    BigDecimal amountTo;
    Currencies currencyFrom;
    Currencies currencyTo;
    String loginTo;
}
