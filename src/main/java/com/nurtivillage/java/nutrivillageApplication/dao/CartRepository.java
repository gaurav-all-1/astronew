package com.nurtivillage.java.geonixApplication.dao;

import java.util.List;
import java.util.Optional;

import com.nurtivillage.java.geonixApplication.dto.CartResponseDto;
import com.nurtivillage.java.geonixApplication.model.Cart;
import com.nurtivillage.java.geonixApplication.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long>{
    
    List<CartResponseDto> findByUserId(Long id);

    // Long deleteByUser(User user);

    void deleteByUserId(Long id);
}
