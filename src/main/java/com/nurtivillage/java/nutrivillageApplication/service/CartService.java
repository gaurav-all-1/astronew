package com.nurtivillage.java.nutrivillageApplication.service;

import java.util.List;
import java.util.Optional;

import com.nurtivillage.java.nutrivillageApplication.dao.CartRepository;
import com.nurtivillage.java.nutrivillageApplication.dao.UserRepository;
import com.nurtivillage.java.nutrivillageApplication.dto.CartResponseDto;
import com.nurtivillage.java.nutrivillageApplication.model.Cart;
import com.nurtivillage.java.nutrivillageApplication.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public LoggedInUserService userService;

    public List<CartResponseDto>getCartItem(Long id){
        try {
            List<CartResponseDto> cartItem = cartRepository.findByUserId(id);
            return cartItem;
            
        } catch (Exception e) {
            throw e;
        }
    }

    public Cart cartItemById(Long id){
        try {
            Optional<Cart> cartItem = cartRepository.findById(id);
            return cartItem.get();
        } catch (Exception e) {
            throw e;
        }
    }


    @Async
    public void insertCart(Cart cart){
        try {
            cartRepository.save(cart);
        } catch (Exception e) {
            throw e;
        }
    }

    public String DeleteCartItem(Long id){
        try {
            cartRepository.deleteById(id);
            return "delete item form cart";
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public String cartClear(){
        try {
            User user = userService.userDetails();
            cartRepository.deleteByUserId(user.getId());
            return "cart clear";
        } catch (Exception e) {
            throw e;
        }
    }

}
