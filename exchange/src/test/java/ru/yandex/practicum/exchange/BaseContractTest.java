package ru.yandex.practicum.exchange;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.bankautoconfigure.currency.Currency;
import ru.yandex.practicum.exchange.services.CurrenciesService;


import static org.mockito.Mockito.when;

import java.util.Map;

@SpringBootTest(properties = {
        "spring.cloud.consul.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.config.import=",
        "spring.profiles.active=test"
})
@AutoConfigureMockMvc(addFilters = false)
@Import({RestConfig.class})
@EnableAutoConfiguration(exclude = {
        ru.yandex.practicum.bankautoconfigure.configuration.RestTemplateConfig.class
})
public abstract class BaseContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CurrenciesService currenciesService;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        Map<String, Currency> body = Map.of(
                "USD", new Currency(new CurrencyEntry("USD", 82.24)),
                "RUB", new Currency(new CurrencyEntry("RUB", 1.00)),
                "CNY", new Currency(new CurrencyEntry("CNY", 11.71)));
        when(currenciesService.getCurrencies()).thenReturn(body);
    }

    class CurrencyEntry implements Map.Entry<String, Double> {
        String key;
        Double value;
        CurrencyEntry(String key, Double value) {
            this.key = key;
            this.value = value;
        }
        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Double getValue() {
            return value;
        }

        @Override
        public Double setValue(Double value) {
            this.value = value;
            return value;
        }
    }
}