package com.social.java.socialapplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.java.socialapplication.model.UserSocialLoginType;



public interface UserSocialLoginTypeRepository extends JpaRepository<UserSocialLoginType,Long> {

}
