package com.nurtivillage.java.nutrivillageApplication.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurtivillage.java.nutrivillageApplication.model.Category;
import com.nurtivillage.java.nutrivillageApplication.model.Product;



public interface CategoryRepository extends JpaRepository<Category,Integer> {
Category findByCode(String code);
Category findByName(String name);
List<Category> findBynameContains(String str);
}
