package ru.yandex.practicum.accountservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.accountservice.dto.UserDto;
import ru.yandex.practicum.accountservice.services.AuthentificationService;
import ru.yandex.practicum.accountservice.services.UserService;

@RestController
@RequestMapping("/accounts/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody UserDto user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{login}/editPassword")
    public ResponseEntity<?> editPassword(@PathVariable String login, @RequestParam String password) {
        userService.editUserPassword(login, password);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @PostMapping("/{login}/editPassword")
//    public ResponseEntity<?> editPassword2(@PathVariable String secondPassword, @RequestParam String password) {
//        userService.editUserPassword(secondPassword, password);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
}
