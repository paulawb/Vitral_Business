package com.vitral.auth.infraestructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 120;
    private final Map<String, Counter> counters = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String key = request.getRemoteAddr() + ":" + request.getRequestURI();
        Counter counter = counters.computeIfAbsent(key, ignored -> new Counter());
        synchronized (counter) {
            long now = Instant.now().getEpochSecond();
            if (now - counter.windowStart >= 60) {
                counter.windowStart = now;
                counter.requests = 0;
            }
            counter.requests++;
            if (counter.requests > MAX_REQUESTS_PER_MINUTE) {
                response.sendError(429, "Rate limit exceeded");
                return;
            }
        }

        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        filterChain.doFilter(request, response);
    }

    private static class Counter {
        private long windowStart = Instant.now().getEpochSecond();
        private int requests;
    }
}
