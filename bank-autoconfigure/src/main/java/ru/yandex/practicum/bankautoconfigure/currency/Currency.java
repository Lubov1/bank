package ru.yandex.practicum.bankautoconfigure.currency;

import java.math.BigDecimal;

public class Currency {
    private String title;
    private String name;
    private BigDecimal value;

    public Currency() {
    }

    public Currency(java.util.Map.Entry<String, Double> entry){
        this.title = entry.getKey();
        this.name = entry.getKey();
        this.value = BigDecimal.valueOf(entry.getValue());
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


}
