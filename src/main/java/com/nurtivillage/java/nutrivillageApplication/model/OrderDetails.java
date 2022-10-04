package com.nurtivillage.java.geonixApplication.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id")
    private UserOrder uesrOrder;
    private int quantity;
    private int price;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "variant_id")
    private Variant variant;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "offer_id")
    private Offer offer;
    //private Inventory

    public OrderDetails(){}

    public OrderDetails(Product product,UserOrder userOrder,int quantity,Variant variant,Offer offer,int price){
        this.product = product;
        this.uesrOrder = userOrder;
        this.quantity = quantity;
        this.variant = variant;
        this.offer = offer;
        this.price=price;
     
    }

    public Long getId() {
        return id;
    }
     


	public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Variant getVariant() {
        return variant;
    }

    public Offer getOffer(){
        return offer;
    }

    public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@JsonIgnore
    public UserOrder getUesrOrder() {
        return uesrOrder;
    }
    
}
