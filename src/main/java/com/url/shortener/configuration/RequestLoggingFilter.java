package com.url.shortener.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
class RequestLoggingFilter extends CommonsRequestLoggingFilter {
    private final List<String> includePatterns;
    private final List<String> excludePatterns;
    private final PathMatcher pathMatcher;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        var path = request.getServletPath();
        var isNotIncluded = includePatterns.stream()
                .noneMatch(urlPattern -> pathMatcher.match(urlPattern, path));
        var isExcluded = excludePatterns.stream()
                .anyMatch(urlPattern -> pathMatcher.match(urlPattern, path));

        return isNotIncluded || isExcluded;
    }
}