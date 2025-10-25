package ru.yandex.practicum.bankautoconfigure.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class NotificationService {
    @Value("${notifications.prefix}")
    private String emailService;
    @Value("${gateway.prefix}")
    String gatewayPrefix;
    RestTemplate restTemplate;

    public NotificationService(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean sendNotification(String email, String message) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "lubov.88867@gmail.com");
        map.add("message", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map, headers);

        restTemplate.exchange("http://" + gatewayPrefix + "/"+ emailService +"/sendEmail",
                HttpMethod.POST, entity, Void.class);
        return true;
    }
}
