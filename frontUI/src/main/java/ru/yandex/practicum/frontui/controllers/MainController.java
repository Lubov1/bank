package ru.yandex.practicum.frontui.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.frontui.dto.Account;
import ru.yandex.practicum.frontui.services.UserService;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {
    private final UserService userService;
    @GetMapping("/main/{login}")
    public String mainPage(@PathVariable String login, Model model, HttpServletResponse servletResponse, HttpSession httpSession) {
        List<Account> accounts = userService.getAccounts(login);
        model.addAttribute("login", login);
        model.addAttribute("accounts", accounts);
        httpSession.setAttribute("login", login);
        return "main";
    }
}
