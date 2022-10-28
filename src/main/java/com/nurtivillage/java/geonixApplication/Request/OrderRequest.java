package com.nurtivillage.java.geonixApplication.Request;

import java.util.List;

import com.nurtivillage.java.geonixApplication.model.Cart;
import com.nurtivillage.java.geonixApplication.model.Inventory;
import com.nurtivillage.java.geonixApplication.model.Product;
import com.nurtivillage.java.geonixApplication.model.ShippingAddress;
import com.nurtivillage.java.geonixApplication.model.User;

import org.springframework.stereotype.Service;

@Service
public class OrderRequest {
    private double amount;
    private List<Cart> cartItem;
    private ShippingAddress shippingAddress;
    private String paymentMethod;
    private Long productId;
    private int variantId;
    private int quantity;

    public OrderRequest(){}

    public OrderRequest(List<Cart> cartItem,double amount,ShippingAddress shippingAddress,String paymentMethod){
        this.amount = amount;
        this.cartItem = cartItem;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
    }

    public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
        return amount;
    }
    public List<Cart> getCartItem() {
        return cartItem;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getVariantId() {
		return variantId;
	}

	public void setVariantId(int variantId) {
		this.variantId = variantId;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
    
}
