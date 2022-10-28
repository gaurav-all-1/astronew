package com.nurtivillage.java.geonixApplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nurtivillage.java.geonixApplication.dao.VariantRepository;
import com.nurtivillage.java.geonixApplication.model.Variant;

@Service
public class VariantService {
@Autowired
VariantRepository variantRepository;

public Variant addVariant(Variant variant) {
	try {
		Variant v=variantRepository.save(variant);
		return v;
		
	}
	catch(Exception e) {
		throw e;
	}
}
	public List<Variant> getAllVariants() {
		try {
			return variantRepository.findAll();
		}
		catch(Exception e) {
			throw e;
		}
	}

	public Optional<Variant> getVariant(int id) {
		try {
			Optional<Variant> v=variantRepository.findById(id);
			return v;
		}
		catch(Exception e) {
			throw e;
		}
	}

	public Variant getVariantBYName(String name){
		try {
			Variant variant = variantRepository.findByName(name);
			return variant;			
		} catch (Exception e) {
			throw e;
		}
	}
}
