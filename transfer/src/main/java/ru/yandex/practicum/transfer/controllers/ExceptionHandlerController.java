package ru.yandex.practicum.transfer.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ExceptionHandlerController {
    Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({IOException.class})
    public ResponseEntity<String> handleException(IOException ex) {
        logger.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }
}
