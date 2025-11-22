package ru.yandex.practicum.frontui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.frontui.exceptions.AccountServiceResponseException;
import ru.yandex.practicum.frontui.exceptions.CashServiceResponseException;
import ru.yandex.practicum.frontui.exceptions.ServiceException;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerController {
    Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);


    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({IOException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleException(IOException ex) {
        logger.info(ex.getMessage());
        logger.info(ex.getStackTrace().toString());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler({AccountServiceResponseException.class, CashServiceResponseException.class})
    public String handleException(ServiceException ex, RedirectAttributes ra) {
        ra.addFlashAttribute("error", ex.getMessage());
        logger.info(ex.getMessage(), ex.getStackTrace());
        return "redirect:/main/"+ex.getLogin();
    }



}
