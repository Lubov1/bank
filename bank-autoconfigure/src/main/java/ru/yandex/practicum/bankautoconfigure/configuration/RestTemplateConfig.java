package ru.yandex.practicum.bankautoconfigure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@EnableWebSecurity
@ConditionalOnClass(org.springframework.security.oauth2.client.registration.ClientRegistrationRepository.class)
public class RestTemplateConfig {
    private final String appName;

    public RestTemplateConfig(@Value("${spring.application.name}")String appName) {
        this.appName = appName;
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository repo,
                                                                                        OAuth2AuthorizedClientService clientService) {
        var provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(repo, clientService);
        manager.setAuthorizedClientProvider(provider);
        return manager;
    }
    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(AuthorizedClientServiceOAuth2AuthorizedClientManager manager) {
        RestTemplate rt = new RestTemplate();
        rt.getInterceptors().add((request, body, execution) -> {
            var authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(appName)
                    .principal(appName + "-service")
                    .build();

            var client = manager.authorize(authorizeRequest);
            if (client == null || client.getAccessToken() == null) {
                throw new IllegalStateException("Cannot obtain access token for client 'interservice'");
            }

            request.getHeaders().setBearerAuth(client.getAccessToken().getTokenValue());

            return execution.execute(request, body);
        });
        System.out.println("RestTemplate is initialized");
        return rt;
    }
    @Bean
    @ConditionalOnBean(RestTemplate.class)
    public RestExceptionHandlerController restExceptionHandlerController() {
        return new RestExceptionHandlerController();
    }
    @Bean
    @Order(0)
    @ConditionalOnProperty(prefix = "customFilterChain", name = "needed", matchIfMissing=true)
    SecurityFilterChain defaultRsSecurity(HttpSecurity http) throws Exception {
        System.out.println("CustomFilterChain is initialized");
        return http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login(c -> c.disable())
                .build();
    }


    @Bean
    @ConditionalOnProperty(prefix = "blockerService", name = "needed")
    BlockerService blockerService(RestTemplate restTemplate) {
        return new BlockerService(restTemplate);
    }

    @Bean
    @ConditionalOnProperty(prefix = "notificationsService", name = "needed")
    NotificationService notificationService(RestTemplate restTemplate) {
        return new NotificationService(restTemplate);
    }

}