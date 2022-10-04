package com.nurtivillage.java.geonixApplication.dao;

import com.nurtivillage.java.geonixApplication.model.NewLocationToken;
import com.nurtivillage.java.geonixApplication.model.UserLocation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewLocationTokenRepository extends JpaRepository<NewLocationToken, Long> {

    NewLocationToken findByToken(String token);

    NewLocationToken findByUserLocation(UserLocation userLocation);

}
