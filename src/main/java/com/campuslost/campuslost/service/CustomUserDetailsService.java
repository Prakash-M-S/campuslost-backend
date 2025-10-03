package com.campuslost.campuslost.service;

import com.campuslost.campuslost.entity.User;
import com.campuslost.campuslost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetailsService Implementation
 * 
 * This service bridges the gap between our User entity and Spring Security.
 * It tells Spring Security how to load user information from our database.
 * 
 * What it does:
 * 1. Loads user from database by email/username
 * 2. Converts our User entity to Spring Security's UserDetails
 * 3. Provides user roles and permissions to Spring Security
 * 4. Used during login and JWT validation
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Load user by username (email in our case)
     * This method is called by Spring Security during authentication
     * 
     * @param usernameOrEmail - can be either email or register number
     * @return UserDetails object for Spring Security
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        
        // Try to find user by email or register number using our custom method
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "User not found with email or register number: " + usernameOrEmail
                ));

        // Convert our User entity to Spring Security's UserDetails
        return new CustomUserPrincipal(user);
    }

    /**
     * Custom UserPrincipal class
     * This class implements UserDetails and wraps our User entity
     * It tells Spring Security about user's credentials and authorities
     */
    public static class CustomUserPrincipal implements UserDetails {
        
        private User user;

        public CustomUserPrincipal(User user) {
            this.user = user;
        }

        /**
         * Get user authorities/roles
         * For now, all users have "USER" role
         * Later you can add admin roles, etc.
         */
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // For now, give all users a "USER" role
            // You can extend this to have different roles like ADMIN, STUDENT, etc.
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        /**
         * Get user password for authentication
         */
        @Override
        public String getPassword() {
            return user.getPassword();
        }

        /**
         * Get username for authentication
         * We use email as username
         */
        @Override
        public String getUsername() {
            return user.getEmail();
        }

        /**
         * Check if account is not expired
         * For now, accounts never expire
         */
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        /**
         * Check if account is not locked
         * For now, accounts are never locked
         */
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        /**
         * Check if credentials are not expired
         * For now, credentials never expire
         */
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        /**
         * Check if account is enabled
         * For now, all accounts are enabled
         */
        @Override
        public boolean isEnabled() {
            return true;
        }

        /**
         * Get the underlying User entity
         * Useful when you need access to user details like name, department, etc.
         */
        public User getUser() {
            return user;
        }
    }
}