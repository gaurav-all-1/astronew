package com.nurtivillage.java.nutrivillageApplication.dao;

import com.nurtivillage.java.nutrivillageApplication.model.ProductImage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
        
}
