package ru.yandex.practicum.frontui.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Account implements Serializable {
    private Long number;
    private BigDecimal balance;
    private String currency;
}
