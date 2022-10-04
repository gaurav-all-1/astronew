package com.nurtivillage.java.nutrivillageApplication.dao;

import java.util.List;
import java.util.Optional;

import com.nurtivillage.java.nutrivillageApplication.dto.CartResponseDto;
import com.nurtivillage.java.nutrivillageApplication.model.Cart;
import com.nurtivillage.java.nutrivillageApplication.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long>{
    
    List<CartResponseDto> findByUserId(Long id);

    // Long deleteByUser(User user);

    void deleteByUserId(Long id);
}
