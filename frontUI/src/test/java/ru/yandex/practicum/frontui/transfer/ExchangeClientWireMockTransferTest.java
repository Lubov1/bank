package ru.yandex.practicum.frontui.transfer;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.frontui.RestConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
        "spring.config.import=",
        "spring.cloud.consul.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "transfer.base-url=http://localhost:${stubrunner.runningstubs.transfer.port}"
})
@AutoConfigureStubRunner(
        ids = "ru.yandex.practicum:transfer:+:stubs",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@ActiveProfiles("test")
@Import({RestConfig.class})
@EnableAutoConfiguration(exclude = {
        ru.yandex.practicum.bankautoconfigure.configuration.RestTemplateConfig.class
})
class ExchangeClientWireMockTransferTest {

    @Autowired
    ExchangeClientTransfer client;

    @Test
    void transfer() {
        assertEquals(200, client.transfer());
    }

}

