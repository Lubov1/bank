package ru.yandex.practicum.gatewayapi.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;

@Component
class RouteLoggingInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
        var target = (java.net.URI) req.getAttribute(
                org.springframework.cloud.gateway.server.mvc.common.MvcUtils.GATEWAY_REQUEST_URL_ATTR);
        var routeId = req.getAttribute(
                org.springframework.cloud.gateway.server.mvc.common.MvcUtils.GATEWAY_ROUTE_ID_ATTR);
        var original = req.getAttribute(
                org.springframework.cloud.gateway.server.mvc.common.MvcUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

        System.out.printf("GW routeId2=%s, original=%s, target=%s%n", routeId, original, target);
        if (target != null) res.setHeader("X-Target-URL", target.toString());
        if (routeId != null) res.setHeader("X-Route-Id", routeId.toString());
        if (routeId != null) System.out.println("oauth2"+res.getHeader("Authorization"));


    }
}

