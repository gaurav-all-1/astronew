package com.nurtivillage.java.nutrivillageApplication.dao;

import java.util.List;

import com.nurtivillage.java.nutrivillageApplication.model.Category;
import com.nurtivillage.java.nutrivillageApplication.model.Product;
import com.nurtivillage.java.nutrivillageApplication.model.ProductImage;
import com.nurtivillage.java.nutrivillageApplication.model.Variant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
}
