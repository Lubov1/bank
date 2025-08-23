package ru.yandex.practicum.frontui.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.frontui.services.UserService;

@Controller
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/{login}/editPassword")
    public String changePassword(@PathVariable String login, @RequestParam String password
            ,@RequestParam String secondPassword, HttpServletResponse servletResponse,
                                 HttpServletRequest servletRequest) {
        userService.changePassword(login, password, secondPassword, servletResponse, servletRequest);
        return "main";
    }
}
