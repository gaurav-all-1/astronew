package com.nurtivillage.java.geonixApplication.service;

import java.util.List;
import java.util.Optional;

import com.nurtivillage.java.geonixApplication.dao.ReviewRepository;
import com.nurtivillage.java.geonixApplication.model.Product;
import com.nurtivillage.java.geonixApplication.model.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;


    public List<Review>getAllReview(){
    	try {
        return reviewRepository.findAll();
        }
    	catch(Exception e) {
    	throw e;	
    	}
    }

    public List<Review>getProductReview(Long productId){
    	try {
        return reviewRepository.findByProductId(productId);
        }
    	catch(Exception e) {
    	throw e;	
    	}
    }    
  
    public Review addReview(Review review) {
    	try {
            System.out.println(review);
    		Review r=reviewRepository.save(review);
    return r;
    	}
    	catch(Exception e) {
    		throw e;
    	}
    }
    
    public List<Review>getReview(Product product){
        List<Review> review = reviewRepository.findByProduct(product);
        return review;
    }

    public Integer avgRating(Long productId){
        Integer avg = reviewRepository.findAvgratingByProduct(productId);
        return avg;
    }

}
