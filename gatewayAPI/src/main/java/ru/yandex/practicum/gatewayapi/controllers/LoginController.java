package ru.yandex.practicum.gatewayapi.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.gatewayapi.dto.Credentials;
import ru.yandex.practicum.gatewayapi.dto.UserDto;

import java.util.List;

@RestController
public class LoginController {
    @Value("${accounts.prefix}")
    private String accountPrefix;


    public LoginController(RestTemplate template) {
        this.template = template;
    }

    RestTemplate template;
    @Autowired
    SecurityContextRepository securityContextRepository;

    @PostMapping("/login")
    public void login(@RequestBody Credentials user, HttpServletRequest req,
                      HttpServletResponse res) {
        ResponseEntity<Void> response = template.exchange("http://" + accountPrefix + "/login"
                , HttpMethod.POST, new HttpEntity<>(user), Void.class);
        createContext(user.getLogin(), req, res);
    }
    @PostMapping("/signup")
    public void signup(@RequestBody UserDto user, HttpServletRequest req,
                       HttpServletResponse res) {
        System.out.println("Signup");

        ResponseEntity<Void> response = template.exchange("http://" + accountPrefix + "/signup"
                , HttpMethod.POST, new HttpEntity<>(user), Void.class);
        createContext(user.getLogin(), req, res);
    }

    private void createContext(String login, HttpServletRequest req, HttpServletResponse res) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication auth = new UsernamePasswordAuthenticationToken(login, null, authorities);
        context.setAuthentication(auth);
        securityContextRepository.saveContext(context, req, res);
        HttpSession session = req.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        res.setStatus(HttpServletResponse.SC_CREATED);
    }
}
