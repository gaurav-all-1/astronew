package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.NewLocationToken;
import com.social.java.socialapplication.model.UserLocation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewLocationTokenRepository extends JpaRepository<NewLocationToken, Long> {

    NewLocationToken findByToken(String token);

    NewLocationToken findByUserLocation(UserLocation userLocation);

}
