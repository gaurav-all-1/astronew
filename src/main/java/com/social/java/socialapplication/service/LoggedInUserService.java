package com.social.java.socialapplication.service;

import com.social.java.socialapplication.dao.UserRepository;
import com.social.java.socialapplication.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Service
public class LoggedInUserService {
    @Autowired
    UserRepository userRepository;
    public User userDetails(){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();
            User user = userRepository.findByEmail(login);
            return user;
    }
}
