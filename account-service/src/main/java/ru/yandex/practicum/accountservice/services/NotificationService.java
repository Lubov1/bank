package ru.yandex.practicum.accountservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.accountservice.configuration.KeycloakTokenService;

@Service
public class NotificationService {
    @Value("${emailService}")
    private String emailService;
    @Value("${gateway.prefix}")
    String gatewayPrefix;
    RestTemplate restTemplate;
    @Autowired
    KeycloakTokenService keycloakTokenService;

    public NotificationService(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }




    public boolean sendNotification(String email, String message) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "lubov.88867@gmail.com");
        map.add("message", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(keycloakTokenService.getToken());
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://" + gatewayPrefix + "/"+ emailService +"/sendEmail",
                HttpMethod.POST, entity, Void.class);
        if (!response.getStatusCode().is2xxSuccessful()){
            System.out.println(response.getStatusCode());
            throw new RuntimeException("Error changing password");
        }
        return true;
    }
}
