package com.nurtivillage.java.nutrivillageApplication.dao;

import java.util.List;

import com.nurtivillage.java.nutrivillageApplication.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Override
    void delete(User user);
    
    User findByForgotPasswordKey(String key);
    
    User findByPhoneNo(String phoneNo);
}
