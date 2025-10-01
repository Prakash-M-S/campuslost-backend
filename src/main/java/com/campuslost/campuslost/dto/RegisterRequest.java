package com.campuslost.campuslost.dto;

// DTO = Data Transfer Object
// This class receives JSON data from frontend during registration
public class RegisterRequest {
    
    // These fields match exactly what frontend sends in JSON:
    // {
    //   "registerNo": "22CSE1001",
    //   "name": "Prakash", 
    //   "department": "CSE",
    //   "college": "ABC University",
    //   "email": "prakash@example.com",
    //   "password": "mypassword"
    // }
    
    private String registerNo;      // Student register number
    private String name;            // Student name
    private String department;      // Department like "CSE"
    private String college;         // College name
    private String email;           // Email address
    private String password;        // Plain text password (will be hashed later)
    
    // Default constructor (required for JSON deserialization)
    public RegisterRequest() {
    }
    
    // Constructor with all fields
    public RegisterRequest(String registerNo, String name, String department, String college, String email, String password) {
        this.registerNo = registerNo;
        this.name = name;
        this.department = department;
        this.college = college;
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters (required for JSON processing)
    public String getRegisterNo() {
        return registerNo;
    }
    
    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getCollege() {
        return college;
    }
    
    public void setCollege(String college) {
        this.college = college;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
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
        return "RegisterRequest{" +
                "registerNo='" + registerNo + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", college='" + college + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}