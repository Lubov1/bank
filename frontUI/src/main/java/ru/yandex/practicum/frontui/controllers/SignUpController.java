package ru.yandex.practicum.frontui.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.frontui.dto.User;
import ru.yandex.practicum.frontui.dto.UserData;
import ru.yandex.practicum.frontui.exceptions.LoginException;
import ru.yandex.practicum.frontui.services.UserService;

@Controller
@AllArgsConstructor
public class SignUpController {
    private UserService userService;

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @ModelAttribute("user")
    public UserData user() {
        return new UserData();
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("user") UserData user, BindingResult br, Model model, HttpServletResponse servletResponse, HttpSession httpSession) {
        if (!user.getPassword().equals(user.getSecondPassword())) {
            br.rejectValue("secondPassword", "Mismatch", "Passwords don't match");
        }
        if (user.getBirthdate().isAfter(user.getBirthdate().minusYears(18))) {
            br.rejectValue("birthDate", "unacceptable", "User should be at least 18 years old");
        }
        if (br.hasErrors()) return "signup";
        try {
            userService.createUser(user);
        } catch (LoginException e) {
            br.addError(new ObjectError("login error", e.getMessage()));
            return "signup";
        }
        model.addAttribute("login", user.getLogin());
        httpSession.setAttribute("login", user.getLogin());
        return "redirect:/main/"+user.getLogin();
    }


    @ModelAttribute("creds")
    User creds() {
        return new User();
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("creds") User user, BindingResult br, Model model, HttpServletResponse servletResponse, HttpSession httpSession) {
        if (br.hasErrors()) return "signup";
        try {
            userService.login(user);
        } catch (LoginException e) {
            br.addError(new ObjectError("login error", e.getMessage()));
            return "signup";
        }
        model.addAttribute("login", user.getLogin());
        httpSession.setAttribute("login", user.getLogin());
        return "redirect:/main/"+user.getLogin();
    }

    @GetMapping("/{login}/logout")
    public String logout(@PathVariable String login) {
        userService.logout();
        return "signup";
    }
}
