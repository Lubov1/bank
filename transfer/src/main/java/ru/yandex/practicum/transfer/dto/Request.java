package ru.yandex.practicum.transfer.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;

import java.math.BigDecimal;

@Data
public class Request {
    private String loginTo;
    private Currencies currencyFrom;
    private Currencies currencyTo;
    private BigDecimal amount;
}
