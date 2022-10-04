package com.nurtivillage.java.nutrivillageApplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.simpleworkflow.flow.core.TryCatch;
import com.nurtivillage.java.nutrivillageApplication.dao.CategoryRepository;
import com.nurtivillage.java.nutrivillageApplication.model.Category;

@Service
public class CategoryService {
@Autowired
CategoryRepository categoryRepository;
@Autowired 
private AWSS3Service awsS3Service;

public Category addCategory(Category category) {
try {
	Category c=categoryRepository.save(category);
	return c;
}
catch(Exception e) {
	throw e;
}
}
public List<Category> getCategories() {
try {
	return categoryRepository.findAll();
	
}
catch(Exception e) {
	throw e;
}
}

public Category getCategory(String name) {
	try {
		Category c=categoryRepository.findByName(name);
		return c;
	}
	catch(Exception e) {
		throw e;
	}
}

public Category getCategoryById(int id){
	try {
		Optional<Category> c = categoryRepository.findById(id);
		return c.get();
	} catch (Exception e) {
		throw e;
	}
}

public void uploadImage(final MultipartFile multipartFile,Category category) throws Exception{
	try {
		String res = awsS3Service.uploadCategoryFile(multipartFile, category);
		category.setImage(res);
		categoryRepository.save(category);
		// uploadCategoryFile(MultipartFile multipartFile,Product product)
	} catch (Exception e) {
		throw e;
	}
}

public void uploadCoverImage(MultipartFile multipartFile, Category category) throws Exception {
	try {
		String res = awsS3Service.uploadCategoryFile(multipartFile, category);
		category.setCoverImage(res);
		categoryRepository.save(category);
	} catch (Exception e) {
		throw e;
	}
}

}
