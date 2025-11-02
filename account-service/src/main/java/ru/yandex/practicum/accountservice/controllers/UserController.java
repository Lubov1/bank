package ru.yandex.practicum.accountservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
import ru.yandex.practicum.accountservice.dto.CredentialsDto;
import ru.yandex.practicum.accountservice.dto.PersonalInformationDto;
import ru.yandex.practicum.accountservice.dto.UserDto;
import ru.yandex.practicum.accountservice.services.UserService;

import java.util.List;


@RestController
@RequestMapping
public class UserController {
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    SecurityContextRepository securityContextRepository;
    public UserController(UserService userService, SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto user, HttpServletRequest req,
                                    HttpServletResponse res) {
        logger.info("creating user");
        userService.createUser(user);
        createContext(user.getLogin(), req, res);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDto credentials, HttpServletRequest req,
                                   HttpServletResponse res) {
        logger.info("login user");
        userService.loginUser(credentials);
        createContext(credentials.getLogin(), req, res);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res) {
        var s = req.getSession(false);
        if (s != null) s.invalidate();

        var cookie = ResponseCookie.from("JSESSIONID", "")
                .maxAge(0).path("/")
                .httpOnly(true).secure(true)
                .sameSite("Lax")
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
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
