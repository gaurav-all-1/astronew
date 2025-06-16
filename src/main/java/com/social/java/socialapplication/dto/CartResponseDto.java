package com.social.java.socialapplication.dto;

import com.social.java.socialapplication.model.Inventory;

public interface CartResponseDto {
    Long getQuantity();
    // Product getProduct();
    Inventory getInventory();
    Long getId();
}
