package com.nurtivillage.java.nutrivillageApplication.controller;

import java.util.Arrays;
import java.util.List;

import com.nurtivillage.java.nutrivillageApplication.dao.UserRepository;
import com.nurtivillage.java.nutrivillageApplication.dto.CartResponseDto;
import com.nurtivillage.java.nutrivillageApplication.model.Cart;
import com.nurtivillage.java.nutrivillageApplication.model.Inventory;
import com.nurtivillage.java.nutrivillageApplication.model.User;
import com.nurtivillage.java.nutrivillageApplication.service.ApiResponseService;
import com.nurtivillage.java.nutrivillageApplication.service.CartService;
import com.nurtivillage.java.nutrivillageApplication.service.InventoryService;
import com.nurtivillage.java.nutrivillageApplication.service.LoggedInUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
            private UserRepository userRepository;
    @Autowired
        private LoggedInUserService userService;
    @Autowired
        private InventoryService inventoryService;
    @GetMapping("/list")
    public ResponseEntity<ApiResponseService> getAllCartItem(){
        try {
            User user = userService.userDetails();
            List<CartResponseDto> cartItem = cartService.getCartItem(user.getId());
            ApiResponseService res = new ApiResponseService("Cart item",true,cartItem);
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
        } catch (Exception e) {
            ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("data"));
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<ApiResponseService> insertCart(@RequestBody Cart cart){
        try {
            User user = userService.userDetails();
            cart.setUser(user);
            System.out.println(cart.getVariant().getId());
            int variantId = cart.getVariant().getId();
            Inventory inventory = inventoryService.getProductVariantInventory(cart.getProduct().getId(),variantId);
            cart.setInventory(inventory);
            if(inventory.getQuantity() < cart.getQuantity()){
                throw new Exception("out of stock");
            }
            cartService.insertCart(cart);
            ApiResponseService res = new ApiResponseService("Cart item insert",true,Arrays.asList(cart));
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
        } catch (Exception e) {
            ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("data"));
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<ApiResponseService> editCart(@RequestBody Cart cart){
        try {
            Cart getCartItem = cartService.cartItemById(cart.getId());
            getCartItem.setQuantity(cart.getQuantity());
            cartService.insertCart(getCartItem);
            ApiResponseService res = new ApiResponseService("Cart item insert",true,Arrays.asList(cart));
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
        } catch (Exception e) {
            ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("data"));
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponseService> removeFromCart(@PathVariable Long id){
        try {
            cartService.DeleteCartItem(id);
            ApiResponseService res = new ApiResponseService("Delete item from cart",true,Arrays.asList(id));
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
        } catch (Exception e) {
            ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("data"));
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clear")
    public ResponseEntity<ApiResponseService> cartClear(){
        try {
            User user = userService.userDetails();
            String cart = cartService.cartClear();
            System.out.println(cart);
            List<CartResponseDto> cartItem = cartService.getCartItem(user.getId());
            ApiResponseService res = new ApiResponseService("cart clear",true,cartItem);
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
        } catch (Exception e) {
            ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("data"));
            return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
