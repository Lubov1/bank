package ru.yandex.practicum.bankautoconfigure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class BlockerService {
    @Value("${gateway.prefix}")
    private String gatewayApiPrefix;
    @Value("${blocker.prefix}")
    private String blockerPrefix;
    private RestTemplate restTemplate;

    public BlockerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void check(String login, BigDecimal amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("amount", amount.toString());
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map, headers);

//        restTemplate.exchange("http://" + gatewayApiPrefix + "/"+ blockerPrefix + "/" + login +"/check",
//                HttpMethod.POST, entity, Void.class);
        restTemplate.exchange("http://" + blockerPrefix + ":8080/" + login +"/check",
                HttpMethod.POST, entity, Void.class);
    }
}
