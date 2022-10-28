package com.nurtivillage.java.geonixApplication.dto;

import com.nurtivillage.java.geonixApplication.model.Inventory;
import com.nurtivillage.java.geonixApplication.model.Product;
import com.nurtivillage.java.geonixApplication.model.Variant;

public interface CartResponseDto {
    Long getQuantity();
    // Product getProduct();
    Inventory getInventory();
    Long getId();
}
