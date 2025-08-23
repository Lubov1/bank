package ru.yandex.practicum.gatewayapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebConfig implements WebMvcConfigurer {
    @Value("${accounts.prefix}")
    private String prefix;
    private final RouteLoggingInterceptor interceptor;
    WebConfig(RouteLoggingInterceptor interceptor) { this.interceptor = interceptor; }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("prefix: " + prefix);
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }
}