package ru.yandex.practicum.exchangegenerator;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.config.import=",
        "spring.cloud.consul.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "exchange.base-url=http://localhost:${stubrunner.runningstubs.exchange.port}"
})
@AutoConfigureStubRunner(
        ids = "ru.yandex.practicum:exchange:+:stubs", // + значит "последняя версия"
        stubsMode = StubRunnerProperties.StubsMode.LOCAL // брать из ~/.m2
)
@ActiveProfiles("test")
@Import({RestConfig.class})
@EnableAutoConfiguration(exclude = {
        ru.yandex.practicum.bankautoconfigure.configuration.RestTemplateConfig.class
})
//@AutoConfigureMockMvc(addFilters = false)
class ExchangeClientWireMockTest {

    @Autowired ExchangeClient client;

    @Test
    void get() {
        var map = client.getCurrencies();

        assertThat(map).containsKey("USD");
        assertThat(map).containsKey("RUB");
        assertThat(map).containsKey("CNY");
    }

    @Test
    void post() {
        assertEquals(200, client.postCurrencies());
    }
}

