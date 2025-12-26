package com.web.bookstore.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws ServletException, IOException {

        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                var claims = jwtTokenProvider.parse(token);

                String userId = String.valueOf(claims.getSubject());
                String role = String.valueOf(claims.get("role"));

                String authority = role != null && role.startsWith("ROLE_")
                        ? role
                        : "ROLE_" + role;

                var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(authority))
                );

                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ignored) {
            }
        }
        chain.doFilter(req, res);
    }
}  