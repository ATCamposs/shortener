package com.url.shortener.configuration;

import com.url.shortener.controllers.interceptors.AuthenticationInterceptor;
import com.url.shortener.models.User;
import com.url.shortener.repositories.UserRepository;
import com.url.shortener.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserRepository userRepository;
    private final ConfigurationProperties configurationProperties;

    @Value("${shortener.app.host}")
    private List<String> frontendHost;

    @Bean
    public JWTService<User> jwtUserService() {
        return new JWTService<>(userRepository);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        configurationProperties.allowedCors.forEach((pathPattern, allowedMethods) ->
                registry.addMapping(pathPattern)
                        .allowedMethods(allowedMethods.toArray(String[]::new))
                        .allowCredentials(true)
                        .allowedOrigins(frontendHost.toArray(String[]::new))
        );

        registry.addMapping("/api/v1/mailing_lists/**")
                .allowedMethods("POST")
                .allowedOrigins("http://localhost:3000");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(jwtUserService()))
                .addPathPatterns("/api/v1/users/**")
                .excludePathPatterns("/api/v1/users/login")
                .excludePathPatterns("/api/v1/users/create");
    }

    @Bean
    public RequestLoggingFilter logFilter(ConfigurationProperties configurationProperties,
                                          PathMatcher pathMatcher) {
        var requestLoggingFilter = new RequestLoggingFilter(
                configurationProperties.requestResponseLogging.includePatterns,
                configurationProperties.requestResponseLogging.excludePatterns,
                pathMatcher
        );

        requestLoggingFilter.setIncludeHeaders(true);
        requestLoggingFilter.setIncludePayload(true);
        requestLoggingFilter.setIncludeClientInfo(true);
        requestLoggingFilter.setIncludeQueryString(true);
        requestLoggingFilter.setMaxPayloadLength(10000);
        requestLoggingFilter.setAfterMessagePrefix("REQUEST DATA : ");

        return requestLoggingFilter;
    }

}