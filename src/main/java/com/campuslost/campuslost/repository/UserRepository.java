package com.campuslost.campuslost.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campuslost.campuslost.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByRegisterNo(String registerNo);
    
    Optional<User> findByEmail(String email);
    
    default Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        Optional<User> userByRegisterNo = findByRegisterNo(usernameOrEmail);
        if (userByRegisterNo.isPresent()) {
            return userByRegisterNo;
        }
        return findByEmail(usernameOrEmail);
    }
    
    boolean existsByRegisterNo(String registerNo);
    
    boolean existsByEmail(String email);
}