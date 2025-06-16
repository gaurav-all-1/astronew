package com.social.java.socialapplication.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.java.socialapplication.model.Offer;

public interface OfferRepository extends JpaRepository<Offer,Long>{
	List<Offer> findByProductIdAndDeletedAtIsNull(Long productId);
	
	List<Offer> findByCategoryIdAndDeletedAtIsNull(int categoryId);
	
	Offer findByIdAndDeletedAtIsNull(Long offerId);

}
