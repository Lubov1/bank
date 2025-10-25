package ru.yandex.practicum.gatewayapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {
    @Bean
    @Order(0)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/actuator/**")
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainOauth2(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/signup", "/login", "/notifications/**",
                        "/exchange/**", "/exchange-generator/**", "/blocker/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(csrf -> csrf.disable())
                .build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChainPasswordAndOauth2(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/accounts/**")
                .securityContext(c->c.securityContextRepository(securityContextRepository()))
                .addFilterBefore(new DebugSessionFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(csrf -> csrf.disable())
                .build();
    }
    @Bean
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

}
