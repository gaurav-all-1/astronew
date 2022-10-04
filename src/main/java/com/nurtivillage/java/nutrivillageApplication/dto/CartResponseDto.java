package com.nurtivillage.java.nutrivillageApplication.dto;

import com.nurtivillage.java.nutrivillageApplication.model.Inventory;
import com.nurtivillage.java.nutrivillageApplication.model.Product;
import com.nurtivillage.java.nutrivillageApplication.model.Variant;

public interface CartResponseDto {
    Long getQuantity();
    // Product getProduct();
    Inventory getInventory();
    Long getId();
}
