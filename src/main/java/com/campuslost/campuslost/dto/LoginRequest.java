package com.campuslost.campuslost.dto;

// DTO to receive login data from frontend
public class LoginRequest {
    
    // User can login with either register number OR email
    // Frontend will send JSON like:
    // {
    //   "usernameOrEmail": "22CSE1001",     // or "prakash@example.com"
    //   "password": "mypassword"
    // }
    
    private String usernameOrEmail;    // Can be register number or email
    private String password;           // Plain text password (will be verified)
    
    // Default constructor (required for JSON deserialization)
    public LoginRequest() {
    }
    
    // Constructor with all fields
    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
    
    // Getters and Setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }
    
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    // toString for debugging (excluding password for security)
    @Override
    public String toString() {
        return "LoginRequest{" +
                "usernameOrEmail='" + usernameOrEmail + '\'' +
                '}';
    }
}