package ru.yandex.practicum.gatewayapi.configuration;

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
public class DebugSessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String jsessionId = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> "JSESSIONID".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        HttpSession session = request.getSession(false); // не создаём новую

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("======== DebugSessionFilter ========");
        System.out.println("JSESSIONID (cookie): " + jsessionId);
        System.out.println("HttpSession ID: " + (session != null ? session.getId() : "null"));
        System.out.println("Authenticated: " + (authentication != null));
        if (authentication != null) {
            System.out.println("User: " + authentication.getName());
            System.out.println("Authorities: " + authentication.getAuthorities());
        }
        System.out.println("====================================");

        filterChain.doFilter(request, response);
    }
}

