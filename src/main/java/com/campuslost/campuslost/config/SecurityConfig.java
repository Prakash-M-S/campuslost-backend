package com.campuslost.campuslost.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.campuslost.campuslost.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtFilter jwtFilter;
    
    /**
     * Password Encoder Bean
     * BCrypt is a strong hashing algorithm for passwords
     * It automatically adds "salt" and is very secure
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Security Configuration - JWT Authentication
     * 1. Allow registration and login without token
     * 2. Require JWT token for all other endpoints
     * 3. Add JWT filter to check tokens
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for API
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())  // Allow H2 console
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // No sessions, use JWT
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/test").permitAll()  // Allow registration/login/test
                .requestMatchers("/h2-console/**").permitAll()  // Allow H2 console
                .anyRequest().authenticated()   // All other endpoints need JWT token
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter
        
        return http.build();
    }
}