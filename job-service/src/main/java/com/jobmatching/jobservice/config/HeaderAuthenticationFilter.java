package com.jobmatching.jobservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Gateway đã verify JWT và inject 2 header này
        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role"); // ví dụ: EMPLOYER

        if (userId != null && role != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var auth = new AbstractAuthenticationToken(
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))) {
                @Override public Object getCredentials() { return null; }
                @Override public Object getPrincipal() { return userId; }
                @Override public boolean isAuthenticated() { return true; }
            };
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
