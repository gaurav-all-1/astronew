package com.nurtivillage.java.nutrivillageApplication.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nurtivillage.java.nutrivillageApplication.model.Inventory;
import com.nurtivillage.java.nutrivillageApplication.model.Variant;
import com.nurtivillage.java.nutrivillageApplication.service.VariantService;

@RestController
public class VariantController {
@Autowired
VariantService variantService;

@RequestMapping("/addVariant")
public ResponseEntity<?> addVariant(@RequestBody Variant variant) {
	try {
	Variant v = variantService.addVariant(variant);
	 return new ResponseEntity<Variant>(v, HttpStatus.OK);
	}catch (Exception e) {
		// TODO: handle exception
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
@RequestMapping("/getVariant")
public ResponseEntity<?> getVariant(@PathVariable int id) {
	try {
	Optional<Variant>v = variantService.getVariant(id);
	 return new ResponseEntity<>(v, HttpStatus.OK);
	}catch (Exception e) {
		// TODO: handle exception
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
@RequestMapping("/getAllVariants")
public ResponseEntity<?> getAllVariants() {
	try {
	List<Variant> v = variantService.getAllVariants();
	 return new ResponseEntity<>(v, HttpStatus.OK);
	}catch (Exception e) {
		// TODO: handle exception
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
}
