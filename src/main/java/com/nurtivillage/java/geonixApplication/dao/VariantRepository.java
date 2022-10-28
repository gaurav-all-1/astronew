package com.nurtivillage.java.geonixApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurtivillage.java.geonixApplication.model.Variant;

public interface VariantRepository extends JpaRepository<Variant,Integer> {

    Variant findByName(String name);

}
