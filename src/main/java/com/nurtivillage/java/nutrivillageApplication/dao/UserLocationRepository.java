package com.nurtivillage.java.geonixApplication.dao;

import com.nurtivillage.java.geonixApplication.model.User;
import com.nurtivillage.java.geonixApplication.model.UserLocation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    UserLocation findByCountryAndUser(String country, User user);

}
