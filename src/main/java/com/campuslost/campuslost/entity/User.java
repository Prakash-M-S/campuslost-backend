package com.campuslost.campuslost.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity                    
@Table(name = "users")     // The table will be called "users" in database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String registerNo;                    // Student register number like "22CSE1001"
    
    @Column(nullable = false)                     // Cannot be null
    private String name;                          // Student name like "Prakash"
    
    @Column(nullable = false)
    private String department;                    // Department like "CSE"
    
    @Column(nullable = false)
    private String college;                       // College name like "ABC University"
    
    @Column(unique = true, nullable = false)      // Email must be unique
    private String email;                         // Email like "prakash@example.com"
    
    @Column(nullable = false)
    private String password;                      // Encrypted password
    
    
    public User() {
    }
    
    // Constructor for creating new users (without ID - it will be auto-generated)
    public User(String registerNo, String name, String department, String college, String email, String password) {
        this.registerNo = registerNo;
        this.name = name;
        this.department = department;
        this.college = college;
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    // toString method for debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", registerNo='" + registerNo + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", college='" + college + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}