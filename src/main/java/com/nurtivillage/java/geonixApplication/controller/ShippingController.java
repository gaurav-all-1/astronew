package com.nurtivillage.java.geonixApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nurtivillage.java.geonixApplication.model.SearchAddress;
import com.nurtivillage.java.geonixApplication.model.ShippingAddress;
import com.nurtivillage.java.geonixApplication.model.User;
import com.nurtivillage.java.geonixApplication.service.ShippingAddressService;

@RestController
public class ShippingController {
@Autowired
ShippingAddressService shippingService;

@RequestMapping("/AddShippingAddress")
public ResponseEntity<?> addAddress(@RequestBody ShippingAddress address) {
	try {
		ShippingAddress sa = shippingService.addAddress(address);
	 	return new ResponseEntity<>(sa, HttpStatus.OK);
	}catch (Exception e) {
		// TODO: handle exception
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

@RequestMapping("/getAddress")
public ResponseEntity<?> getAddress(@RequestBody SearchAddress searchAddress) {
	try {
		List<ShippingAddress>sa = shippingService.getAddress(searchAddress);
	 	return new ResponseEntity<>(sa, HttpStatus.OK);
	}catch (Exception e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
}
