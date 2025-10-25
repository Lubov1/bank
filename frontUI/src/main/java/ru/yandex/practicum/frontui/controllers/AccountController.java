package ru.yandex.practicum.frontui.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.dto.UserData;
import ru.yandex.practicum.frontui.services.UserService;

import java.time.LocalDate;

@Controller
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    public record ChangePasswordForm(String password, String secondPassword) {}
    @PostMapping("/{login}/editPassword")
    public String changePassword(@PathVariable String login,
                                 @ModelAttribute("form") ChangePasswordForm form,
                                 RedirectAttributes ra, HttpServletResponse servletResponse,
                                 HttpServletRequest servletRequest) {
        if (!form.password.equals(form.secondPassword)) {
            ra.addFlashAttribute("error", "Passwords don't match");
            return "redirect:/main/"+login;
        }
        userService.changePassword(login, form.password);
        ra.addFlashAttribute("success","Password changed");
        return "redirect:/main/"+login;
    }

    @PostMapping("/{login}/editUserAccounts")
    public String changeUserAccounts(@PathVariable String login, @RequestParam String name
            , @RequestParam LocalDate birthdate) {
        userService.changeUserAccounts(login, name, birthdate);
        return "redirect:/main/"+login;
    }

    @PostMapping("/{login}/addAccount")
    public String addAccount(@PathVariable String login, @RequestParam Currencies currency
            ) {
        userService.addAccount(login, currency);
        return "redirect:/main/"+login;
    }

    @PostMapping("/{login}/deleteAccount")
    public String deleteAccount(@PathVariable String login, @RequestParam Currencies currency
            ) {
        userService.deleteAccount(login, currency);
        return "redirect:/main/"+login;
    }


}
