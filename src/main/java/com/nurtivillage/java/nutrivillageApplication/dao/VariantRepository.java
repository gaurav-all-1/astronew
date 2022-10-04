package com.nurtivillage.java.nutrivillageApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurtivillage.java.nutrivillageApplication.model.Variant;

public interface VariantRepository extends JpaRepository<Variant,Integer> {

    Variant findByName(String name);

}
