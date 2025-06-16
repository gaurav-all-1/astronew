package com.social.java.socialapplication.dao;

import java.util.List;

import com.social.java.socialapplication.model.Product;
import com.social.java.socialapplication.model.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByProduct(Product product);

    List<Review> findByProductId(Long productId);

    @Query(value="SELECT AVG(rating) FROM review WHERE product_id  = ?1",nativeQuery=true)
    Integer findAvgratingByProduct(Long productId);
}
