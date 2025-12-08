package ru.yandex.practicum.accountservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.bankautoconfigure.configuration.LoggerHelper;

@Configuration
public class LoggerConfig {
    @Bean
    public LoggerHelper loggerHelper() {
        return new LoggerHelper();
    }
}
