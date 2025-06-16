package com.social.java.socialapplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.java.socialapplication.model.ShippingAddress;
import com.social.java.socialapplication.model.User;

public interface ShippingRepository extends JpaRepository<ShippingAddress,Integer> {
ShippingAddress findByUser(User user);
}
