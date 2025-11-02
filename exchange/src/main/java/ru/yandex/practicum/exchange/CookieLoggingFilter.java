package ru.yandex.practicum.exchange;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CookieLoggingFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(CookieLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String servletPath = httpRequest.getServletPath();
            logger.info("requesst" + request.getLocalAddr() + ":" + request.getLocalPort()+ servletPath);

        }
        chain.doFilter(request, response);
    }
}
