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

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        if (userRepository.existsByRegisterNo(request.getRegisterNo())) {
            throw new RuntimeException("Register number already exists: " + request.getRegisterNo());
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        
        User user = new User(
            request.getRegisterNo(),
            request.getName(),
            request.getDepartment(),
            request.getCollege(),
            request.getEmail(),
            hashedPassword
        );

        userRepository.save(user);

        return "User registered successfully with email: " + request.getEmail();
    }

    public String login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getUsernameOrEmail());

        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByRegisterNo(request.getUsernameOrEmail());
        }

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with identifier: " + request.getUsernameOrEmail());
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
