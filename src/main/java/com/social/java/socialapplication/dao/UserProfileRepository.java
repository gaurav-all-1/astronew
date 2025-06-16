package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.UserProfile;

import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {

}
