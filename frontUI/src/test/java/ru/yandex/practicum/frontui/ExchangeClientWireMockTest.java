package ru.yandex.practicum.frontui;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
        "spring.config.import=",
        "spring.cloud.consul.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "exchange.base-url=http://localhost:${stubrunner.runningstubs.exchange.port}"
})
@AutoConfigureStubRunner(
        ids = "ru.yandex.practicum:cash-service:+:stubs",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@ActiveProfiles("test")
@Import({RestConfig.class})
@EnableAutoConfiguration(exclude = {
        ru.yandex.practicum.bankautoconfigure.configuration.RestTemplateConfig.class
})
class ExchangeClientWireMockTest {

    @Autowired ExchangeClient client;

    @Test
    void deposit() {
        assertEquals(200, client.deposit());
    }

    @Test
    void withdraw() {
        assertEquals(200, client.withdraw());
    }
}

