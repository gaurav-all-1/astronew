package com.nurtivillage.java.nutrivillageApplication.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
    private int quantity;

    // @OneToOne(cascade = CascadeType.MERGE)
    // @JoinColumn(name = "variant_id")
    @Transient
    private Variant variant;
    public Cart(){}

    public Cart(User user,Product product,int quantity,Variant variant){
        this.product = product;
        this.quantity =  quantity;
        this.user = user;
        // this.inventory = inventory;
        this.variant = variant;
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
    
    public User getUser() {
        return user;
    }

    public Variant getVariant() {
        return variant;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public void setVariant(Variant variant) {
        this.variant = variant;
    }

}
