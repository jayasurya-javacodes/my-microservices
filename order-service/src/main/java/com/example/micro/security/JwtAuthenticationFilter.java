package com.example.micro.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        // Get JWT from Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            try {
                username = jwtUtil.extractUsername(token);

            } catch (Exception e) {
                System.out.println("Invalid JWT token");
            }
        }

        // Authenticate user
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            try {

                if (jwtUtil.validateToken(token)) {

                    String role = jwtUtil.extractRole(token);

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(authority)
                            );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println("========== JWT FILTER ==========");

                    System.out.println("Token is VALID");

                    System.out.println("Username: " + username);

                    System.out.println("Role: " + role);

                    System.out.println("Authenticated: " + authentication.isAuthenticated());

                    System.out.println("Authorities: " + authentication.getAuthorities()
                    );

                    System.out.println("================================");
                }

            } catch (Exception e) {

                System.out.println(
                        "JWT authentication failed: "
                                + e.getMessage()
                );
            }
        }

        filterChain.doFilter(request, response);
    }
}