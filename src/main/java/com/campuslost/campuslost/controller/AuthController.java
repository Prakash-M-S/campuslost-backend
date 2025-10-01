package com.campuslost.campuslost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campuslost.campuslost.dto.RegisterRequest;
import com.campuslost.campuslost.service.AuthService;

@RestController  // This tells Spring: "This class handles HTTP requests"
@RequestMapping("/api/auth")  // All endpoints start with /api/auth
public class AuthController {
    
    @Autowired
    private AuthService authService;  // Our business logic service
    
    /**
     * Register endpoint: POST /api/auth/register
     * 
     * Frontend will send JSON like:
     * {
     *   "registerNo": "22CSE1001",
     *   "name": "Prakash",
     *   "department": "CSE", 
     *   "college": "ABC University",
     *   "email": "prakash@example.com",
     *   "password": "mypassword"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        
        try {
            // Call service to do the actual registration work
            String result = authService.register(request);
            
            // Return success response (HTTP 200)
            return ResponseEntity.ok(result);
            
        } catch (RuntimeException e) {
            // If something goes wrong (duplicate email, etc.)
            // Return error response (HTTP 400 Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            // For any other unexpected errors
            // Return server error (HTTP 500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Server error: " + e.getMessage());
        }
    }
    
    /**
     * Test endpoint to verify server is running
     * GET /api/auth/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("CampusLost Auth API is working!");
    }
}