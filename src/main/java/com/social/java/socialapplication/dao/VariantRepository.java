package com.social.java.socialapplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.java.socialapplication.model.Variant;

public interface VariantRepository extends JpaRepository<Variant,Integer> {

    Variant findByName(String name);

}
