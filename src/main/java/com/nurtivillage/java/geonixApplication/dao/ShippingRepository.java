package com.nurtivillage.java.geonixApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurtivillage.java.geonixApplication.model.ShippingAddress;
import com.nurtivillage.java.geonixApplication.model.User;

public interface ShippingRepository extends JpaRepository<ShippingAddress,Integer> {
ShippingAddress findByUser(User user);
}
