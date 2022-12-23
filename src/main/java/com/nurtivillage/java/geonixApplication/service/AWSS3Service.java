package com.nurtivillage.java.geonixApplication.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import com.nurtivillage.java.geonixApplication.model.Category;
import com.nurtivillage.java.geonixApplication.model.Product;
import com.nurtivillage.java.geonixApplication.model.UserOrder;
import com.nurtivillage.java.geonixApplication.model.UserProfile;

import org.springframework.web.multipart.MultipartFile;

public interface AWSS3Service {

	void uploadFile(MultipartFile multipartFile,String userId)  throws Exception;
	
	void uploadGenericFile(MultipartFile multipartFile)  throws Exception;

	String uploadProductFile(MultipartFile multipartFile,Product product) throws Exception;
	
	String uploadCategoryFile(MultipartFile multipartFile,Category category) throws Exception;

	public ByteArrayOutputStream downloadFile(String keyName,UserProfile profile);
	
	public ByteArrayOutputStream downloadFile(String keyName);

	public URL uploadinvoicetos3(final String bucketName, final File file, UserOrder order);
}
