package ru.yandex.practicum.accountservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.accountservice.dto.CredentialsDto;
import ru.yandex.practicum.accountservice.dto.PersonalInformationDto;
import ru.yandex.practicum.accountservice.dto.UserDto;
import ru.yandex.practicum.accountservice.exceptions.UserExistsException;
import ru.yandex.practicum.accountservice.services.UserService;

import java.time.LocalDate;

@RestController
@RequestMapping
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto user) {
        System.out.println("creating user");
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDto credentials) {
        System.out.println("login user");
        userService.loginUser(credentials);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{login}/editPassword")
    public ResponseEntity<?> editPassword(@PathVariable String login, @RequestParam String password) {
        System.out.println("editing password");
        userService.editUserPassword(login, password);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{login}/editUserAccounts")
    public ResponseEntity<?> editUserAccounts(@PathVariable String login, @RequestBody PersonalInformationDto personalInformation) {
        System.out.println("editing password");
        userService.editUserAccounts(login, personalInformation.getName(), personalInformation.getBirthday());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
