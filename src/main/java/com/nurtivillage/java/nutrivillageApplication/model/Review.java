package com.nurtivillage.java.nutrivillageApplication.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@ManyToOne
	private Product product;

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	private String comment;
	private String rating;

	public Review() {

	}

	public Review(String comment, String rating, User user) {
		// this.product = product;
		this.user = user;
		this.rating = rating;
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public Long getId() {
		return id;
	}

//    public Product getProduct() {
//        return product;
//    }
	public String getRating() {
		return rating;
	}

	public User getUser() {
		return user;
	}

	public Product getProduct() {
		return product;
	}
}
