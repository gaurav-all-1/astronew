package com.nurtivillage.java.nutrivillageApplication.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nurtivillage.java.nutrivillageApplication.dao.InventoryRepository;
import com.nurtivillage.java.nutrivillageApplication.dto.InventoryResponse;
import com.nurtivillage.java.nutrivillageApplication.model.Inventory;
import com.nurtivillage.java.nutrivillageApplication.model.Product;
import com.nurtivillage.java.nutrivillageApplication.model.Variant;


@Service
public class InventoryService {
	@Autowired 
	InventoryRepository inventoryRepo;
	public List<Inventory> getAll(){
		try {
			return inventoryRepo.findAll();
			
		}
		catch(Exception e) {
			throw e;
		}
	}
	public Inventory addInventory(Inventory inventory) {
		try {
			Inventory inv=inventoryRepo.save(inventory);
			return inv;
		}
		catch(Exception e) {
			throw e;
		}
	}

	public List<Inventory> getProductInventory(Product product) {
		try {
			List<Inventory> inv=inventoryRepo.findByProduct(product);

	return inv;

		}
		catch(Exception e) {
			throw e;
		}
	}

	public Inventory getProductVariantInventory(Long  productId,int  variantId) {
		try {
			Inventory inv=inventoryRepo.findByProductIdAndVariantId(productId,variantId);
	return inv;}
		catch(Exception e) {
			throw e;}
		}


	public Inventory updateInventory(Long productId,int variantId,int quantity) throws Exception {
		try {
			Inventory inv=inventoryRepo.findByProductIdAndVariantId(productId,variantId);
			if(inv.getQuantity()>=quantity) {
			inv.setQuantity(inv.getQuantity()-quantity);
			inventoryRepo.save(inv);}
			return inv;
		}
		catch(Exception e) {


			throw e;
		}}

	public List<InventoryResponse> detailsInventory(Long id){
		List<InventoryResponse> inv = inventoryRepo.findByProductId(id);
		return inv;

	}
    public List<?> findbyname(String string) {
		List<InventoryResponse> products = inventoryRepo.findDistinctByProductNameContains(string);
        return products;
    }
    
    public List<Inventory> getInventoryFilteredByPrice(int maxPrice,int minPrice){
    	try {
    		return inventoryRepo.getInventoryFilteredByPrice(maxPrice, minPrice);
    	}
    	catch(Exception e) {
    		throw e;
    	}
    }
    public Object[] defaultPrice(Long id) {
		Object[] defaultprice = inventoryRepo.getdefaultPrice(id);
        return defaultprice;
    }
}
