package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Override
    void delete(User user);
    
    User findByForgotPasswordKey(String key);
    
    User findByPhoneNo(String phoneNo);
}
