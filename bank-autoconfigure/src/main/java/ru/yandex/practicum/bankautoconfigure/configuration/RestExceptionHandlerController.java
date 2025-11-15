package ru.yandex.practicum.bankautoconfigure.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandlerController {
    Logger logger = LoggerFactory.getLogger(RestExceptionHandlerController.class);

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleDownstream(RestClientResponseException ex) {
        String message = ex.getMessage();
        logger.info(message, ex.getResponseBodyAsString());
        return ResponseEntity
                .status(ex.getRawStatusCode())
                .body(Map.of(
                        "error", "downstream",
                        "status", ex.getRawStatusCode(),
                        "message", message,
                        "details", ex.getResponseBodyAsString()
                ));
    }

}
