package ru.yandex.practicum.frontui.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.frontui.exceptions.AccountServiceResponseException;
import ru.yandex.practicum.frontui.exceptions.CashServiceResponseException;
import ru.yandex.practicum.frontui.exceptions.LoginException;
import ru.yandex.practicum.frontui.exceptions.ServiceException;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({IOException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleException(IOException ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler({AccountServiceResponseException.class, CashServiceResponseException.class})
    public String handleException(ServiceException ex, RedirectAttributes ra) {
        ra.addFlashAttribute("error", ex.getMessage());
        System.out.println(ex.getMessage());
        return "redirect:/main/"+ex.getLogin();
    }



}
