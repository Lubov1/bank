package ru.yandex.practicum.blocker.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class BlockerController {
    @PostMapping("/{login}/check")
    public ResponseEntity<?> check(@PathVariable String login, @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(1000))>0) {
            return ResponseEntity.badRequest().body("operation for user " + login + " was blocked (amount is bigger than 1000 not acceptable)");
        }
        return ResponseEntity.ok().build();
    }
}
