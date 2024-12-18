package com.learn.spring_security.filter;

import com.learn.spring_security.utils.JWTUtil;
import com.learn.spring_security.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Authorization: Bearer <your-jwt-token>
        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlZHdpbiIsImlhdCI6MTczNDUwNjg5NSwiZXhwIjoxNzM0NTA2OTMxfQ.rAdX1ik5VW8d7Y1b0VRZZOQLlkb8TS-ixqisNQt7op8

        // Steps:
        // 1. Extract the Token from the Authorization header.
        // 2. Validate Token.
        // 3. Authenticate the user in the Spring Security context.

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // Check if the Authorization header contains a Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7); // Extract the token
            username = jwtUtil.extractUsername(jwtToken); // Extract username from the token
        }

        // Validate token and set authentication in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Extract user details from DB
            UserDetails userDetails = applicationContext.getBean(MyUserDetailsService.class).loadUserByUsername(username);
            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
