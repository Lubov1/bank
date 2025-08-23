package ru.yandex.practicum.gatewayapi.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
@Component
public class GatewayDebugFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("📡 Incoming request to Gateway: " + request.getRequestURL());

        // В Gateway MVC после маршрутизации адрес назначения можно вытащить
        // из атрибута FORWARD_REQUEST_URI_ATTRIBUTE
        Object targetUri = request.getAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping");
        if (targetUri != null) {
            System.out.println("➡️ Will forward to: " + targetUri);
        }

        filterChain.doFilter(request, response);
    }
}

