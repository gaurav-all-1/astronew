package com.nurtivillage.java.nutrivillageApplication.dto;

import com.nurtivillage.java.nutrivillageApplication.model.Product;
import com.nurtivillage.java.nutrivillageApplication.model.Variant;

public interface InventoryResponse {
    Variant getVariant();
    int getPrice();
    int getQuantity();
    Product getProduct();
}
