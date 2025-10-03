package com.campuslost.campuslost.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campuslost.campuslost.dto.LoginRequest;
import com.campuslost.campuslost.dto.RegisterRequest;
import com.campuslost.campuslost.entity.User;
import com.campuslost.campuslost.repository.UserRepository;
import com.campuslost.campuslost.util.JwtUtil;

@Service  // This tells Spring: "This is a business logic class"
public class AuthService {
    
    // Dependencies we need:
    @Autowired
    private UserRepository userRepository;  // For database operations
    
    @Autowired 
    private PasswordEncoder passwordEncoder;  // For hashing passwords
    
    @Autowired
    private JwtUtil jwtUtil;  // For creating JWT tokens
    
    /**
     * Register a new user
     * This method does the complete registration process:
     * 1. Validate data (check duplicates)
     * 2. Hash password
     * 3. Create User entity
     * 4. Save to database
     */
    public String register(RegisterRequest request) {
        
        // Step 1: Validation - Check if register number already exists
        if (userRepository.existsByRegisterNo(request.getRegisterNo())) {
            throw new RuntimeException("Register number already exists: " + request.getRegisterNo());
        }
        
        // Step 2: Validation - Check if email already exists  
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        // Step 3: Hash the password (NEVER store plain text passwords!)
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        
        // Step 4: Convert RegisterRequest (DTO) → User (Entity)
        User newUser = new User(
            request.getRegisterNo(),    // "22CSE1001"
            request.getName(),          // "Prakash"  
            request.getDepartment(),    // "CSE"
            request.getCollege(),       // "ABC University"
            request.getEmail(),         // "prakash@example.com"
            hashedPassword             // Hashed version of "mypassword"
        );
        
        // Step 5: Save to database
        User savedUser = userRepository.save(newUser);
        
        // Step 6: Return success message
        return "User registered successfully with ID: " + savedUser.getId();
    }
    
    /**
     * Login user
     * This method does the complete login process:
     * 1. Find user by register number or email
     * 2. Check if user exists
     * 3. Verify password
     * 4. Generate JWT token and return it
     */
    public String login(LoginRequest request) {
        
        // Step 1: Find user by register number or email
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail());
        
        // Step 2: Check if user exists
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found: " + request.getUsernameOrEmail());
        }
        
        User user = optionalUser.get();
        
        // Step 3: Verify password
        // Compare plain text password with hashed password in database
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        
        if (!passwordMatches) {
            throw new RuntimeException("Invalid password for user: " + request.getUsernameOrEmail());
        }
        
        // Step 4: Generate JWT token for the user
        String jwtToken = jwtUtil.generateToken(user.getEmail());
        
        // Step 5: Return JWT token
        return jwtToken;
    }
}