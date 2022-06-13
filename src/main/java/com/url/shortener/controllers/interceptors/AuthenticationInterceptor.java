package com.url.shortener.controllers.interceptors;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.url.shortener.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JWTService<?> jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (RequestMethod.OPTIONS == RequestMethod.valueOf(request.getMethod())) return true;

        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        var token = authorizationHeader.substring(7);

        try {
            var decodedJWT = jwtService.verify(token);
            request.setAttribute("userId", UUID.fromString(decodedJWT.getClaim("user_id").asString()));
        } catch (TokenExpiredException ignored) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } catch (JWTVerificationException ignored) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        return true;
    }
}