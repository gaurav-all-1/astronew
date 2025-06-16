package com.social.java.socialapplication.dao;

import java.util.List;

import com.social.java.socialapplication.dto.CartResponseDto;
import com.social.java.socialapplication.model.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long>{
    
    List<CartResponseDto> findByUserId(Long id);

    // Long deleteByUser(User user);

    void deleteByUserId(Long id);
}
