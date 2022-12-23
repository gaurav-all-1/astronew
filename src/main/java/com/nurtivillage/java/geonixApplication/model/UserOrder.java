package com.nurtivillage.java.geonixApplication.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private double amount;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_Ad_id")
    private ShippingAddress shippingAddress;

    @Column(nullable = false)
    private Long orderNo;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    private int itemNO;

    @Column(nullable = false)
    private String paymentMethod;

    @Lob
    private String comment;
    private String paymentStatus;

    private String invoiceURL;

    public String getInvoiceURL() {
        return invoiceURL;
    }

    public void setInvoiceURL(String invoiceURL) {
        this.invoiceURL = invoiceURL;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
    
    public UserOrder(){}

    public UserOrder(double amount,User user,int itemNO,Status status,ShippingAddress shippingAddress,String paymentMethod){
        this.amount = amount;
        long leftLimit = 1L;
        long rightLimit = 10000L;
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        this.orderNo = generatedLong;
        this.user = user;
        this.itemNO = itemNO;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public double getAmount() {
        return amount;
    }
    public Long getId() {
        return id;
    }
    public Long getOrderNo() {
        return orderNo;
    }

    public String getComment() {
        return comment;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public User getUser() {
        return user;
    }
    public int getItemNO(){
        return itemNO;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public void setItemNO(int itemNO) {
		this.itemNO = itemNO;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

    public Date getCreatedAt(){
        return createdAt;
    }
}

