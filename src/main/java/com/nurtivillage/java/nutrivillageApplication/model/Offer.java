package com.nurtivillage.java.nutrivillageApplication.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Offer {
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private Long id;
@ManyToOne
private Product product;
@ManyToOne
private Category category;

private String amount;

private String discountType;

private Date deletedAt;

@UpdateTimestamp
@Temporal(TemporalType.TIMESTAMP)
@Column(nullable = false)
private Date updatedAt;

@CreationTimestamp
@Temporal(TemporalType.TIMESTAMP)
@Column(nullable = false)
private Date createdAt;

public Date getDeletedAt() {
	return deletedAt;
}

public void setDeletedAt(Date deletedAt) {
	this.deletedAt = deletedAt;
}

public Date getUpdatedAt() {
	return updatedAt;
}

public void setUpdatedAt(Date updatedAt) {
	this.updatedAt = updatedAt;
}

public Date getCreatedAt() {
	return createdAt;
}

public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Product getProduct() {
	return product;
}

public void setProduct(Product product) {
	this.product = product;
}

public Category getCategory() {
	return category;
}

public void setCategory(Category category) {
	this.category = category;
}

public String getAmount() {
	return amount;
}

public void setAmount(String amount) {
	this.amount = amount;
}

public String getDiscountType() {
	return discountType;
}

public void setDiscountType(String discountType) {
	this.discountType = discountType;
}


}
