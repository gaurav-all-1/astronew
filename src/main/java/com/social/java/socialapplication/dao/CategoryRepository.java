package com.social.java.socialapplication.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.java.socialapplication.model.Category;


public interface CategoryRepository extends JpaRepository<Category,Integer> {
Category findByCode(String code);
Category findByName(String name);
List<Category> findBynameContains(String str);
}
