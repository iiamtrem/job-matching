package com.jobmatching.searchservice.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${search.sync.apiKey:}")
    private String apiKey;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!request.getRequestURI().startsWith("/search/jobs")) return true;
        return "GET".equalsIgnoreCase(request.getMethod()); // chỉ chặn POST/DELETE
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        if (apiKey == null || apiKey.isBlank()) { chain.doFilter(req, res); return; } // dev mode
        String k = req.getHeader("X-API-Key");
        if (!Objects.equals(k, apiKey)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"Unauthorized: invalid X-API-Key\"}");
            return;
        }
        chain.doFilter(req, res);
    }
}
