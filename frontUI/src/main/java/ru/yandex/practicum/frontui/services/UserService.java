package ru.yandex.practicum.frontui.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.frontui.dto.UserData;
import ru.yandex.practicum.frontui.dto.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    @Value("${spring.application.name}")
    String applicationName;
    @Value("${accounts.prefix}")
    String accountPrefix;
    @Value("${gateway.prefix}")
    String gatewayPrefix;
    RestTemplate restTemplate;

    public UserService(KeycloakTokenService tokenService, RestTemplate restTemplate) {
        this.tokenService = tokenService;
        this.restTemplate = restTemplate;
    }

    KeycloakTokenService tokenService;
    public void createUser(UserData user, HttpServletResponse servletResponse) {
        System.out.println( "user is created");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(tokenService.getToken());
        ResponseEntity<Void> response = restTemplate.exchange("http://"+gatewayPrefix + "/"+ accountPrefix + "/users"
                , HttpMethod.POST, new HttpEntity<>(user), Void.class);
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.CREATED)){
            System.out.println("Error creating user");
        }
        System.out.println(response.getBody()+ "user is created");
        List<String> list = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (list!=null && list.size()>0){
            list.forEach(c->{
                servletResponse.addHeader(HttpHeaders.SET_COOKIE,c);
                System.out.println("header"+c);
            });
        }

    }

    public void changePassword(String login, String password, String newPassword
            , HttpServletResponse servletResponse,
                               HttpServletRequest servletRequest) {
        if (password==null || password.isEmpty() || !password.equals(newPassword)) {
            throw new RuntimeException("Password does not match");
        }
        String sessionCookie = null;
        if (servletRequest.getCookies() != null) {
            for (Cookie cookie : servletRequest.getCookies()) {
                if ("JSESSIONID".equals(cookie.getName())) { // или GSESSIONID
                    sessionCookie = "JSESSIONID=" + cookie.getValue(); // или GSESSIONID=...
                    break;
                }
            }
        }

        // 2. Устанавливаем заголовок Cookie
        HttpHeaders headers = new HttpHeaders();
        if (sessionCookie != null) {
            headers.add(HttpHeaders.COOKIE, sessionCookie);
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("password", password);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://"+gatewayPrefix + "/" + accountPrefix + "/users/" + login + "/editPassword",
                HttpMethod.POST, entity, Void.class);
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.CREATED)){
            System.out.println(response.getStatusCode());
            throw new RuntimeException("Error changing password");
        }
        List<String> list = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (list!=null && list.size()>0){
            list.forEach(c->{
                servletResponse.addHeader(HttpHeaders.SET_COOKIE,c);
                System.out.println("header"+c);
            });
        }
    }
}
