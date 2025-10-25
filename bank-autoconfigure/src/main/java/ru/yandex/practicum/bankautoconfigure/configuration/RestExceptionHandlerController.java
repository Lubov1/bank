package ru.yandex.practicum.bankautoconfigure.configuration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandlerController {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleDownstream(RestClientResponseException ex) {
        String message = ex.getMessage();
        System.out.println(message);
        return ResponseEntity
                .status(ex.getRawStatusCode())
                .body(Map.of(
                        "error", "downstream",
                        "status", ex.getRawStatusCode(),
                        "message", message
                ));
    }

}
