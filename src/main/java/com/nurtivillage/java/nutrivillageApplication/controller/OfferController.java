package com.nurtivillage.java.geonixApplication.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nurtivillage.java.geonixApplication.model.Offer;
import com.nurtivillage.java.geonixApplication.service.OfferService;

@RestController
@RequestMapping("/offer")
public class OfferController {
@Autowired
private OfferService offerService;

@PostMapping("/add")
public ResponseEntity<?> addOffer(@RequestBody Offer offer) {
	try {
		Offer savedOffer=offerService.addOffer(offer);
		return new ResponseEntity<>(savedOffer,HttpStatus.OK);
	}
	catch(Exception e) {
		throw e;
	}
}

@GetMapping("/list")
public ResponseEntity<?> addOffer() throws Exception{
	try {
		List<Offer> offerList=offerService.getAllOffer();
		return new ResponseEntity<>(offerList,HttpStatus.OK);
	}
	catch(Exception e) {
		throw e;
	}
}

@GetMapping("/{offerId}")
public ResponseEntity<?> getOfferById(@PathVariable Long offerId) throws Exception{
	try {
		Offer savedOffer=offerService.getOfferById(offerId);
		return new ResponseEntity<>(savedOffer,HttpStatus.OK);
	}
	catch(Exception e) {
		throw e;
	}
}

@GetMapping("/product/{productId}")
public ResponseEntity<?> getOffersByProduct(@PathVariable Long productId) {
	try {
	List<Offer> savedOffers=offerService.getOffersByProduct(productId);
		return new ResponseEntity<>(savedOffers,HttpStatus.OK);
	}
	catch(Exception e) {
		throw e;
	}
}
@GetMapping("/category/{categoryId}")
public ResponseEntity<?> getOffersByCategory(@PathVariable int categoryId) {
	try {
	List<Offer> savedOffers=offerService.getOffersByCategory(categoryId);
		return new ResponseEntity<>(savedOffers,HttpStatus.OK);
	}
	catch(Exception e) {
		throw e;
	}
}
@GetMapping("/delete/{offerId}")
public ResponseEntity<?> deleteOffer(@PathVariable Long offerId) throws Exception{
	try {
		Offer savedOffer=offerService.deleteOffer(offerId);
		return new ResponseEntity<>(savedOffer,HttpStatus.OK);
	}
	catch(Exception e) {
		throw e;
	}
}
	@PutMapping("/update")
	public ResponseEntity<?> updateOffer(@RequestBody Offer offer) throws Exception{
		try {
			Offer savedOffer=offerService.updateOffer(offer);
			return new ResponseEntity<>(savedOffer,HttpStatus.OK);
		}
		catch(Exception e) {
			throw e;
		}
	}


}
