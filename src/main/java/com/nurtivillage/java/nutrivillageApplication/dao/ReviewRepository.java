package com.nurtivillage.java.nutrivillageApplication.dao;

import java.util.List;

import com.nurtivillage.java.nutrivillageApplication.model.Product;
import com.nurtivillage.java.nutrivillageApplication.model.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByProduct(Product product);

    List<Review> findByProductId(Long productId);

    @Query(value="SELECT AVG(rating) FROM review WHERE product_id  = ?1",nativeQuery=true)
    Integer findAvgratingByProduct(Long productId);
}
