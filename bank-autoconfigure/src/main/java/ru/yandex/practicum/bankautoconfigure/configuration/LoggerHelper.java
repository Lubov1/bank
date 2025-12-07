package ru.yandex.practicum.bankautoconfigure.configuration;

import org.slf4j.MDC;

public class LoggerHelper {
    public void logWithUser(String user, Runnable action) {
        try {
            MDC.put("user", user);
            action.run();
        } finally {
            MDC.remove("user");
        }
    }
}
