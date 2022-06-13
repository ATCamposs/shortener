package com.url.shortener.configuration;

import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@Setter
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "shortener")
public class ConfigurationProperties {

    Map<String, Set<String>> allowedCors;
    RequestResponseLogging requestResponseLogging;

    @Setter
    static class RequestResponseLogging {
        List<String> includePatterns = new ArrayList<>();
        List<String> excludePatterns = new ArrayList<>();
    }
}