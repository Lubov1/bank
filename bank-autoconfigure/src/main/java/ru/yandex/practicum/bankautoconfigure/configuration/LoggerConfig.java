package ru.yandex.practicum.bankautoconfigure.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class LoggerConfig {
    @Bean
    LoggerHelper loggerHelper() {
        return new LoggerHelper();
    }
}
