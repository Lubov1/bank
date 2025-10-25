package ru.yandex.practicum.gatewayapi.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
public class CookieLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Преобразуем в HTTP-запрос и ответ
        if (request instanceof HttpServletRequest httpRequest) {
            // Полный URI (без домена)
            String requestURI = httpRequest.getRequestURI();

            // Только путь без query params
            String servletPath = httpRequest.getServletPath();
            System.out.println("requesst" + request.getLocalAddr() + ":" + request.getLocalPort()+ servletPath);

        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // === Логируем входящие куки ===
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            System.out.println("== Incoming Cookies ==");
            Arrays.stream(cookies).forEach(cookie ->
                    System.out.printf("Cookie: %s=%s; Path=%s; Domain=%s\n",
                            cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain())
            );
        } else {
            System.out.println("== No incoming cookies ==");
        }

        // Продолжаем выполнение цепочки
        chain.doFilter(request, response);

        // === Логируем Set-Cookie заголовки из ответа ===
        String setCookie = httpResponse.getHeader("Set-Cookie");
        if (setCookie != null) {
            System.out.println("== Outgoing Set-Cookie header ==");
            System.out.println(setCookie);
        } else {
            System.out.println("== No Set-Cookie in response ==");
        }
        String auth = httpResponse.getHeader("Authorization");
        System.out.println("== outgoing Authorization == "+ auth );
    }
}
