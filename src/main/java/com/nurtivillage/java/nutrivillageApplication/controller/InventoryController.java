package com.nurtivillage.java.nutrivillageApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nurtivillage.java.nutrivillageApplication.dao.InventoryRepository;
import com.nurtivillage.java.nutrivillageApplication.dto.InventoryResponse;
import com.nurtivillage.java.nutrivillageApplication.model.Inventory;
import com.nurtivillage.java.nutrivillageApplication.model.Product;
import com.nurtivillage.java.nutrivillageApplication.model.Variant;
import com.nurtivillage.java.nutrivillageApplication.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController

public class InventoryController {
@Autowired
InventoryService inventoryService;

@RequestMapping("/getAllInventory")
public ResponseEntity<?> allInventory() {
	try {
	List<Inventory> inv= inventoryService.getAll();
	 return new ResponseEntity<>(inv, HttpStatus.OK);
	}catch (Exception e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

@RequestMapping("/AddInventory")
public ResponseEntity<?> addInventory(@RequestBody Inventory inventory) {
	try {
	Inventory inv = inventoryService.addInventory(inventory);
	 return new ResponseEntity<Inventory>(inv, HttpStatus.OK);
	}catch (Exception e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
@RequestMapping("/getProductInventory")
public ResponseEntity<?> getProductInventory(@RequestBody Product product) {
	try {

			List<Inventory> inv= inventoryService.getProductInventory(product);
	 		return new ResponseEntity<>(inv, HttpStatus.OK);

	}catch (Exception e) {
		// TODO: handle exception
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
@RequestMapping("/getProductVariantInventory")
public ResponseEntity<?> getProductVariantInventory(@RequestParam Long productId,@RequestParam int variantId) {
	try {

Inventory inv= inventoryService.getProductVariantInventory(productId,variantId);
	 return new ResponseEntity<>(inv, HttpStatus.OK);
	}catch (Exception e) {
		// TODO: handle exception
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
@RequestMapping("/updateInventory/{quantity}")
public ResponseEntity<?> updateInventory(@RequestParam Long productId,@RequestParam int variantId,@PathVariable int quantity) {
	try {
Inventory inv= inventoryService.updateInventory(productId,variantId,quantity);

	 return new ResponseEntity<>(inv, HttpStatus.OK);
	}catch (Exception e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

@PostMapping(value="/product/{id}")
public ResponseEntity<?> productDetails(@PathVariable Long id) {
	List<InventoryResponse> inv = inventoryService.detailsInventory(id);	
	return new ResponseEntity<>(inv, HttpStatus.OK);
}
@GetMapping("/getInventoryByPrice")
public List<Inventory> getInventoryByPrice(@RequestParam int maxPrice,@RequestParam int minPrice){
	try {
		return inventoryService.getInventoryFilteredByPrice(maxPrice, minPrice);
	}
	catch(Exception e) {
		throw e;
	}
}

}
