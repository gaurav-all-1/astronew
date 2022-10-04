package com.nurtivillage.java.geonixApplication.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
	@JoinColumn(name = "order_id")
    private UserOrder order;

    public Payment(){};
    public Payment(String razorpayPaymentId,String razopayOrderId,String razorpaySignature,UserOrder order){
        this.razorpayPaymentId = razorpayPaymentId;
        this.razorpayOrderId = razopayOrderId;
        this.razorpaySignature = razorpaySignature;
        this.order = order;

    }

    public UserOrder getOrder() {
        return order;
    }

    public String getRazopayOrderId() {
        return razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}
	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}
	public void setRazorpaySignature(String razorpaySignature) {
		this.razorpaySignature = razorpaySignature;
	}
	public void setOrder(UserOrder order) {
		this.order = order;
	}

}
