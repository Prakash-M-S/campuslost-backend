package com.campuslost.campuslost.dto;

public class RegisterRequest {
    
    private String registerNo;
    private String name;
    private String department;
    private String college;
    private String email;
    private String password;

    public RegisterRequest() {
    }
    
    public RegisterRequest(String registerNo, String name, String department, String college, String email, String password) {
        this.registerNo = registerNo;
        this.name = name;
        this.department = department;
        this.college = college;
        this.email = email;
        this.password = password;
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
}
