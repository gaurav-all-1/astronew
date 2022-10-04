package com.nurtivillage.java.nutrivillageApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

import com.nurtivillage.java.nutrivillageApplication.dto.InventoryResponse;
import com.nurtivillage.java.nutrivillageApplication.model.Inventory;
import com.nurtivillage.java.nutrivillageApplication.model.Product;
import com.nurtivillage.java.nutrivillageApplication.model.Variant;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

	// Inventory findByProduct(Product product);
	@Query(value="select * from inventory where price <= :maxPrice and price >= :minPrice",nativeQuery=true)
	List<Inventory> getInventoryFilteredByPrice(@Param("maxPrice") int maxPrice,@Param("minPrice") int minPrice);

	@Query(value="select max(price),min(price)from inventory where product_id ='?1'",nativeQuery = true)
	Object[] getdefaultPrice(Long productId);

	Inventory findByProductIdAndVariantId(Long product, int variant);

    List<Inventory> findByProduct(Product product);

	List<InventoryResponse> findByProductId(Long id);

	List<InventoryResponse> findDistinctByProductNameContains(String string);
    
}
