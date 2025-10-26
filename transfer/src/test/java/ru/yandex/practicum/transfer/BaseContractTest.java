package ru.yandex.practicum.transfer;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.transfer.services.TransferService;

import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

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
    TransferService transferService;

    @BeforeEach
    void setup() throws IOException {
        RestAssuredMockMvc.mockMvc(mockMvc);

        doNothing().when(transferService).transfer(anyString(),anyString(),any(Currencies.class),any(Currencies.class), any(BigDecimal.class));
    }
}