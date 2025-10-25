package ru.yandex.practicum.gatewayapi.configuration;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@AllArgsConstructor
public class OAuth2GlobalFilter {
    RestTemplate restTemplate;
    @Scheduled(fixedRate = 60000)
    public void sendCurrencies() {
        System.out.println("Sending exchange");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> response = restTemplate.exchange("http://gateway/anything",
                HttpMethod.GET, entity, Map.class);
        System.out.println("response: " + response.getBody());
    }
}