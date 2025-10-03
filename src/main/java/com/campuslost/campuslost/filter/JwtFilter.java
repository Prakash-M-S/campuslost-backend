package com.campuslost.campuslost.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.campuslost.campuslost.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Simple JWT Filter
 * This runs on EVERY request and checks for JWT token
 * If valid token found → User is authenticated
 * If no token or invalid → User is not authenticated
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Step 1: Get Authorization header from request
        String authHeader = request.getHeader("Authorization");
        
        // Step 2: Check if header exists and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            
            // Step 3: Extract token (remove "Bearer " prefix)
            String token = authHeader.substring(7);
            
            try {
                // Step 4: Extract username from token
                String username = jwtUtil.extractUsername(token);
                
                // Step 5: Validate token
                if (jwtUtil.validateToken(token, username)) {
                    
                    // Step 6: Create authentication object (tells Spring Security user is logged in)
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(username, null, null);
                    
                    // Step 7: Set authentication in Spring Security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                
            } catch (Exception e) {
                // Token is invalid, do nothing (user stays unauthenticated)
            }
        }
        
        // Step 8: Continue to next filter/controller
        filterChain.doFilter(request, response);
    }
}