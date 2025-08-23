package ru.yandex.practicum.frontui.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.frontui.dto.UserData;
import ru.yandex.practicum.frontui.services.UserService;

@Controller
@RequestMapping("/signup")
@AllArgsConstructor
public class SignUpController {
    private UserService userService;

    @GetMapping
    public String signUp() {
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute UserData user, Model model, HttpServletResponse servletResponse) {
        userService.createUser(user, servletResponse);
        model.addAttribute("login", user.getLogin());
        return "main";
    }
}
