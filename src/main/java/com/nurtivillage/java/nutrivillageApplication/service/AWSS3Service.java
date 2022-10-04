package com.nurtivillage.java.nutrivillageApplication.service;

import java.io.ByteArrayOutputStream;

import com.nurtivillage.java.nutrivillageApplication.model.Category;
import com.nurtivillage.java.nutrivillageApplication.model.Product;
import com.nurtivillage.java.nutrivillageApplication.model.UserProfile;

import org.springframework.web.multipart.MultipartFile;

public interface AWSS3Service {

	void uploadFile(MultipartFile multipartFile,String userId)  throws Exception;
	
	void uploadGenericFile(MultipartFile multipartFile)  throws Exception;

	String uploadProductFile(MultipartFile multipartFile,Product product) throws Exception;
	
	String uploadCategoryFile(MultipartFile multipartFile,Category category) throws Exception;

	public ByteArrayOutputStream downloadFile(String keyName,UserProfile profile);
	
	public ByteArrayOutputStream downloadFile(String keyName);
	
	
}
