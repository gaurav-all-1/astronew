package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.User;
import com.social.java.socialapplication.model.UserLocation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    UserLocation findByCountryAndUser(String country, User user);

}
