package ru.yandex.practicum.frontui.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bankautoconfigure.configuration.LoggerHelper;
import ru.yandex.practicum.bankautoconfigure.currency.Currencies;
import ru.yandex.practicum.frontui.dto.Account;
import ru.yandex.practicum.frontui.dto.PersonalInformation;
import ru.yandex.practicum.frontui.dto.User;
import ru.yandex.practicum.frontui.dto.UserData;
import ru.yandex.practicum.frontui.exceptions.AccountServiceResponseException;
import ru.yandex.practicum.frontui.exceptions.LoginException;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);
    LoggerHelper loggerHelper;
    @Value("${accounts.prefix}")
    String accountPrefix;
    RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate, LoggerHelper loggerHelper) {
        this.restTemplate = restTemplate;
        this.loggerHelper = loggerHelper;
    }

    public void createUser(UserData user) {
        try {
            loggerHelper.logWithUser(user.getLogin(), ()->logger.info("user {} is creating", user.getLogin()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.ALL));

            HttpEntity<UserData> entity = new HttpEntity<>(user, headers);
            restTemplate.exchange(String.join("/", accountPrefix, "signup")
                        , HttpMethod.POST, entity, Void.class);

            loggerHelper.logWithUser(user.getLogin(), ()-> {
                logger.info(String.join("/", "http:/", accountPrefix+":8080", "signup"));

                logger.info("user {} is created", user.getLogin());});
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new LoginException(ex.getMessage(), user.getLogin());
        }
    }
    public void login(User user) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.ALL));

            HttpEntity<User> entity = new HttpEntity<>(user, headers);
            restTemplate.exchange(String.join("/", accountPrefix, "login")
                        , HttpMethod.POST, entity, Void.class);

        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new LoginException(ex.getMessage(), user.getLogin());
        }
    }

    public List<Account> getAccounts(String login) {
        loggerHelper.logWithUser(login, ()->
            logger.info("getting accounts"));
        try {
            ResponseEntity<List<Account>> response;
            response = restTemplate.exchange(String.join("/", accountPrefix, login, "getAccounts")
                    , HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });

            return response.getBody();
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new AccountServiceResponseException(ex.getMessage(), login);
        }
    }

    public void changePassword(String login, String password) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("password", password);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map, headers);
        try {
            restTemplate.exchange(String.join("/", accountPrefix, login, "editPassword"),
                    HttpMethod.POST, entity, Void.class);

        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new AccountServiceResponseException(ex.getMessage(), login);
        }
    }

    public void changeUserAccounts(String login, String name, LocalDate birthdate) {
        try {
            restTemplate.exchange(String.join("/",  accountPrefix, login, "editUserAccounts"),
                        HttpMethod.POST, new HttpEntity<>(new PersonalInformation(name, birthdate)), Void.class);
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new AccountServiceResponseException(ex.getMessage(), login);
        }
    }

    public void addAccount(String login, Currencies currency) {

        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("currency", currency.name());

        HttpEntity<MultiValueMap<String,Object>> entity = new HttpEntity<>(map, headers);

        try {
            restTemplate.exchange(String.join("/", accountPrefix, login, "addAccount"),
                        HttpMethod.POST, entity, Void.class);


        } catch (RestClientResponseException ex) {
            loggerHelper.logWithUser(login, ()->
                    logger.info("RestClientResponseException"));
            throw new AccountServiceResponseException(ex.getMessage(), login);

        }
    }

    public void deleteAccount(String login, Currencies currency) {
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("currency", currency.name());

        HttpEntity<MultiValueMap<String,Object>> entity = new HttpEntity<>(map, headers);

        try {
                restTemplate.exchange(String.join("/", accountPrefix, login, "deleteAccount"),
                        HttpMethod.POST, entity, Void.class);


        } catch (RestClientResponseException ex) {
            loggerHelper.logWithUser(login, ()->
                logger.info("RestClientResponseException"));

            throw new AccountServiceResponseException(ex.getMessage(), login);

        }
    }

    public void logout() {
        restTemplate.exchange(String.join("/", accountPrefix, "/logout")
                , HttpMethod.POST, null, new ParameterizedTypeReference<>() {
                });
    }
}
