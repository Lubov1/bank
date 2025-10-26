package ru.yandex.practicum.transfer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.transfer.dto.Request;
import ru.yandex.practicum.transfer.services.TransferService;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class TransferController {
    private final TransferService transferService;
    @PostMapping("/{login}/transfer")
    public ResponseEntity<?> transfer(@RequestBody Request request,
                                      @PathVariable String login) throws IOException {
        transferService.transfer(login, request.getLoginTo(), request.getCurrencyFrom(), request.getCurrencyTo(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
