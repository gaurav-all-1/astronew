package com.nurtivillage.java.nutrivillageApplication.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nurtivillage.java.nutrivillageApplication.dao.ShippingRepository;
import com.nurtivillage.java.nutrivillageApplication.model.SearchAddress;
import com.nurtivillage.java.nutrivillageApplication.model.ShippingAddress;
import com.nurtivillage.java.nutrivillageApplication.model.User;

@Service
@Transactional
public class ShippingAddressService {
	@PersistenceContext
	EntityManager em;
	@Autowired
	ShippingRepository shippingRepository;
public ShippingAddress addAddress(ShippingAddress sa) {
	try {
		ShippingAddress s= shippingRepository.save(sa);
		return s;
	}
	catch(Exception e) {
		throw e;
	}}

	public List<ShippingAddress> getAddress(SearchAddress searchAddress) {
		 CriteriaBuilder cb = em.getCriteriaBuilder();
		    CriteriaQuery<ShippingAddress> cq = cb.createQuery(ShippingAddress.class);
		    
		    Root<ShippingAddress> address = cq.from(ShippingAddress.class);
		    List<Predicate> predicates = new ArrayList<>();
		  
		    if(searchAddress.getName()!=null) {
		    	try{predicates.add(cb.equal(address.get("name"), searchAddress.getName()));
		    	
		    }
		  catch(Exception e) {
			  e.printStackTrace();
			  
		  }}
		    if(searchAddress.getCountry()!=null) {
		    	try{predicates.add(cb.equal(address.get("country"), searchAddress.getCountry()));
		    	
		    }
		  catch(Exception e) {
			  e.printStackTrace();
			  
		  }}
		    if(searchAddress.getStreet()!=null) {
		    	try{predicates.add(cb.equal(address.get("street"), searchAddress.getStreet()));
		    	
		    }
		  catch(Exception e) {
			  e.printStackTrace();
			  
		  }}
		    if(searchAddress.getState()!=null) {
		    	try{predicates.add(cb.equal(address.get("state"), searchAddress.getState()));
		    	
		    }
		  catch(Exception e) {
			  e.printStackTrace();
			  
		  }}
		    if(searchAddress.getCity()!=null) {
		    	try{predicates.add(cb.equal(address.get("city"), searchAddress.getCity()));
		    	
		    }
		  catch(Exception e) {
			  e.printStackTrace();
			  
		  }}
		    if(searchAddress.getPincode()!=null) {
		    	try{predicates.add(cb.equal(address.get("pincode"), searchAddress.getPincode()));
		    	
		    }
		  catch(Exception e) {
			  e.printStackTrace();
			  
		  }}
		    if(searchAddress.getMobile()!=null) {
		    	try{predicates.add(cb.equal(address.get("mobile"), searchAddress.getMobile()));
		    	
		    }
		  catch(Exception e) {
			  e.printStackTrace();
			  
		  }}    
		    if(searchAddress.getUser()!=null) {
		    	try{predicates.add(cb.equal(address.get("user"), searchAddress.getUser()));
		    	
		    }
		  catch(Exception e) {
			  e.printStackTrace();
			  
		  }} 
		    cq.where(predicates.toArray(new Predicate[0]));

		    return em.createQuery(cq).getResultList();
	}
	
}
