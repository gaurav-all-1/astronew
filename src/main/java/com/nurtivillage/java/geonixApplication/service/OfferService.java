package com.nurtivillage.java.geonixApplication.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.nurtivillage.java.geonixApplication.dao.OfferRepository;
import com.nurtivillage.java.geonixApplication.model.Offer;

@Service
public class OfferService {
@Autowired
private OfferRepository offerRepository;
// to add offer 
public Offer addOffer(Offer offer) {
	try {
		return offerRepository.save(offer);
	}
	catch(Exception e) {
		throw e;
	}
}
//to get all offer
public List<Offer> getAllOffer() throws Exception{
	try{
		List<Offer> allOffer = offerRepository.findAll(); 
		return allOffer;
	}catch(Exception e){
		throw e;
	}
}
// to get offer by offer id

public Offer getOfferById(Long offerId) throws Exception {
	try {
		 Optional<Offer> offerTemp=offerRepository.findById(offerId);
		 if(offerTemp.isPresent()) {
			 return offerTemp.get();
		 }
		 else {
			 throw new Exception("Offer doesn't exists in DB with this provided Id");
		 }
	}
	catch(Exception e) {
		throw e;
	}
}

//to get List of offer by product id
public List<Offer> getOffersByProduct(Long productId){
	try {
		return offerRepository.findByProductIdAndDeletedAtIsNull(productId);
	}
	catch(Exception e) {
		throw e;
	}
}

public List<Offer> getOffersByCategory(int categoryId){
	try {
		return offerRepository.findByCategoryIdAndDeletedAtIsNull(categoryId);
	}
	catch(Exception e) {
		throw e;
	}
}
//public Offer deleteOffer(Long offerId) {
//	try {
//		Offer offer=offerRepository.findByIdAndDeletedAtIsNull(offerId);
//	   offer.setDeletedAt(new Date());
//	   return offerRepository.save(offer);
//	}
//	catch(Exception e) {
//		throw e;
//	}
//}
public Offer updateOffer(Offer offer) {
	try {
		return offerRepository.save(offer);
	}
	catch(Exception e) {
		throw e;
	}
}


}
