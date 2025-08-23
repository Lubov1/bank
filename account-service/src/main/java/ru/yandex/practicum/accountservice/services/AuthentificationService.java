package ru.yandex.practicum.accountservice.services;

import com.sun.net.httpserver.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.accountservice.dto.UserDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthentificationService {
    RestTemplate template = new RestTemplate();
    @Value("${keycloak.token-endpoint}")
    private String tokenEndpoint;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public String registerAndLogin(UserDto user) {
        String adminToken = getAdminAccessToken();
        createUserInKeycloak(user, adminToken);
        return getJwtToken(user);
    }

    public String getJwtToken(UserDto user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", user.getLogin());
        body.add("password", user.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        System.out.println(request.getBody());

        ResponseEntity<Map> response = template.postForEntity(tokenEndpoint, request, Map.class);
        System.out.println(response.getBody());
        return (String) response.getBody().get("access_token");
    }

    public void createUserInKeycloak(UserDto user, String adminToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", user.getLogin());
        userPayload.put("enabled", true);

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", user.getPassword());
        credential.put("temporary", false);

        userPayload.put("credentials", List.of(credential));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);

        template.postForEntity(
                "http://localhost:8090/admin/realms/master/users",
                request,
                Void.class
        );
    }

    public String getAdminAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "admin-cli"); // или client_id клиента с правами
        body.add("username", "admin"); // admin Keycloak
        body.add("password", "admin"); // пароль администратора

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = template.postForEntity(
                "http://localhost:8090/realms/master/protocol/openid-connect/token",
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

}
