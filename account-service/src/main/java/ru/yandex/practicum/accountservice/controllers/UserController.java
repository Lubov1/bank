package ru.yandex.practicum.accountservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.accountservice.dto.CredentialsDto;
import ru.yandex.practicum.accountservice.dto.PersonalInformationDto;
import ru.yandex.practicum.accountservice.dto.UserDto;
import ru.yandex.practicum.accountservice.services.UserService;


@RestController
@RequestMapping
public class UserController {
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto user) {
        logger.info("creating user");
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDto credentials) {
        logger.info("login user");
        userService.loginUser(credentials);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{login}/editPassword")
    public ResponseEntity<?> editPassword(@PathVariable String login, @RequestParam String password) {
        logger.info("editing password");
        userService.editUserPassword(login, password);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{login}/editUserAccounts")
    public ResponseEntity<?> editUserAccounts(@PathVariable String login, @RequestBody PersonalInformationDto personalInformation) {
        logger.info("editing password");
        userService.editUserAccounts(login, personalInformation.getName(), personalInformation.getBirthday());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
