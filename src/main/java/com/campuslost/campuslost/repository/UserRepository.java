package com.campuslost.campuslost.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campuslost.campuslost.entity.User;

@Repository  // This tells Spring: "This is a database access class"
public interface UserRepository extends JpaRepository<User, Long> {
    
    // JpaRepository gives us FREE methods like:
    // - save(user)           → Save user to database
    // - findById(id)         → Find user by ID
    // - findAll()            → Get all users
    // - deleteById(id)       → Delete user by ID
    // - count()              → Count total users
    
    // Custom methods we need for registration:
    
    // Find user by register number (like "22CSE1001")
    Optional<User> findByRegisterNo(String registerNo);
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Find user by either register number OR email (for login)
    default Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        // First try to find by register number
        Optional<User> userByRegisterNo = findByRegisterNo(usernameOrEmail);
        if (userByRegisterNo.isPresent()) {
            return userByRegisterNo;
        }
        // If not found, try to find by email
        return findByEmail(usernameOrEmail);
    }
    
    // Check if register number already exists (for validation)
    boolean existsByRegisterNo(String registerNo);
    
    // Check if email already exists (for validation)
    boolean existsByEmail(String email);
}