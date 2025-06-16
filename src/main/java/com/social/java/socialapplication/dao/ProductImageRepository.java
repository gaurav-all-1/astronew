package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.ProductImage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
        
}
