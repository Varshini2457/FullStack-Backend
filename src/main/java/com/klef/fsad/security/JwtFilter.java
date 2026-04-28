package com.klef.fsad.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

     
        if (
                path.equals("/login") ||
                path.equals("/register") ||
                path.equals("/send-otp") ||
                path.equals("/verify-otp") ||
                path.equals("/auth/google")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Get Authorization Header
        String authHeader = request.getHeader("Authorization");

        // ❌ Missing Token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write("Missing or invalid Authorization header");

            return;
        }

        // ✅ Extract Token
        String token = authHeader.substring(7);

        try {

            // ✅ Validate Token
            String email = jwtUtil.extractEmail(token);

            System.out.println("✅ Valid user: " + email);

        } catch (Exception e) {

            // ❌ Invalid Token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write("Invalid or expired token");

            return;
        }

        // ✅ Continue Request
        filterChain.doFilter(request, response);
    }
}