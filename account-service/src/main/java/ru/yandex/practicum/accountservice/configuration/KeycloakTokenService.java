package ru.yandex.practicum.accountservice.configuration;

import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Service;
@Service
public class KeycloakTokenService {

    private final OAuth2AuthorizedClientManager clientManager;

    public KeycloakTokenService(ClientRegistrationRepository repo,
                                OAuth2AuthorizedClientRepository clientService) {
        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        DefaultOAuth2AuthorizedClientManager manager =
                new DefaultOAuth2AuthorizedClientManager(repo, clientService);
        manager.setAuthorizedClientProvider(provider);

        this.clientManager = manager;
    }

    public String getToken() {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                .withClientRegistrationId("accounts")
                .principal("accounts-service")
                .build();

        OAuth2AuthorizedClient client = clientManager.authorize(request);
        if (client == null) {
            throw new IllegalStateException("Cannot get access token from Keycloak");
        }

        return client.getAccessToken().getTokenValue();
    }
}

