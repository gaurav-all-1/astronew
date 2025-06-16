package com.social.java.socialapplication.dao;

import java.util.List;
import java.util.Optional;

import com.social.java.socialapplication.model.Product;
import com.social.java.socialapplication.model.ProductImage;
import com.social.java.socialapplication.model.Variant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product,Long>{

    
    Page<Product> findByDeletedAtIsNull(Pageable pageable);
    List<Product> findByDeletedAtIsNull();

    List<Product> findByStatusAndDeletedAtIsNull(int status);

    List<Product> findByCategoryIdAndDeletedAtIsNull(Integer categoryId);
    
    @Query(value="select * from product where category_id =:categoryId and deleted_at is null order by id desc",nativeQuery=true)
    Page<Product> getByCategoryIdAndDeletedAtIsnull(int categoryId,Pageable pageable);
    
    Product findByProductImage(ProductImage productImage);
    Page<Product> findByVariantsAndDeletedAtIsNull(Variant variantId, Pageable firstPage);
    List<Product> findBynameContainsAndDeletedAtIsNull(String str);

    Optional<Product> findByName(String name);

    Optional<Product> findByProductUrl(String productUrl);
}
