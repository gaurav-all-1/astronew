package com.social.java.socialapplication.dto;

import com.social.java.socialapplication.model.Product;
import com.social.java.socialapplication.model.Variant;

public interface InventoryResponse {
    Variant getVariant();
    int getPrice();
    int getQuantity();
    Product getProduct();
}
