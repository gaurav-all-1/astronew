package com.nurtivillage.java.geonixApplication.dto;

import com.nurtivillage.java.geonixApplication.model.Product;
import com.nurtivillage.java.geonixApplication.model.Variant;

public interface InventoryResponse {
    Variant getVariant();
    int getPrice();
    int getQuantity();
    Product getProduct();
}
