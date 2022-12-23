package com.nurtivillage.java.geonixApplication.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import com.nurtivillage.java.geonixApplication.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.nurtivillage.java.geonixApplication.dao.UserRepository;
import com.nurtivillage.java.geonixApplication.error.GenericException;


@Service
public class AWSS3ServiceImpl implements AWSS3Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3ServiceImpl.class);

	@Autowired
	private AmazonS3 amazonS3;
	@Value("${aws.s3.bucket}")
	private String bucketName;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	// @Async annotation ensures that the method is executed in a different background thread 
	// but not consume the main thread.
	@Async
	public void uploadFile(final MultipartFile multipartFile,String userId) throws Exception {
		LOGGER.info("File upload in progress.");
		try {
			final File file = convertMultiPartFileToFile(multipartFile);
			uploadFileToS3Bucket(bucketName, file,Integer.parseInt(userId));
			LOGGER.info("File upload is completed.");
			file.delete();	// To remove the file locally created in the project folder.
		} catch (final AmazonServiceException ex) {
			LOGGER.info("File upload is failed.");
			LOGGER.error("Error= {} while uploading file.", ex.getMessage());
		}
	}
	
	@Async
	public void uploadGenericFile(final MultipartFile multipartFile) throws Exception {
		LOGGER.info("File upload in progress.");
		try {
			final File file = convertMultiPartFileToFile(multipartFile);
			uploadGenericFileToS3Bucket(bucketName, file);
			LOGGER.info("File upload is completed.");
			file.delete();	// To remove the file locally created in the project folder.
		} catch (final AmazonServiceException ex) {
			LOGGER.info("File upload is failed.");
			LOGGER.error("Error= {} while uploading file.", ex.getMessage());
		}
	}

	public String uploadProductFile(final MultipartFile multipartFile,Product product) throws Exception{
		final File file = convertMultiPartFileToFile(multipartFile);
		URL imageUrl = uploadProductImageToS3(bucketName,file,product);
		LOGGER.info("File upload is completed.");
		file.delete();
		return imageUrl.toString();
	}

	public String uploadCategoryFile(final MultipartFile multipartFile,Category category) throws Exception{
		final File file = convertMultiPartFileToFile(multipartFile);
		URL imageUrl = uploadCategoryImageToS3(bucketName,file,category);
		LOGGER.info("category image upload is completed.");
		file.delete();
		return imageUrl.toString();
	}

	private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
		final File file = new File(multipartFile.getOriginalFilename());
		try (final FileOutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(multipartFile.getBytes());
		} catch (final IOException ex) {
			LOGGER.error("Error converting the multi-part file to file= ", ex.getMessage());
		}
		return file;
	}

	private void uploadFileToS3Bucket(final String bucketName, final File file,int userId)throws Exception {
		
		
		final String uniqueFileName = userId+"/"+userId+"_"+ file.getName();
		LOGGER.info("Uploading file with name= " + uniqueFileName);
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
		amazonS3.putObject(putObjectRequest);
	
	
}

	private URL uploadProductImageToS3(final String bucketName,final File file,Product product){
		final String fileName = product.getId()+"/"+product.getName()+"/"+file.getName();
		LOGGER.info("Uploading file with name= " + fileName);
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead);
		amazonS3.putObject(putObjectRequest);
		URL imageUrl = amazonS3.getUrl(bucketName, fileName);
		return imageUrl;
	}	

	private URL uploadCategoryImageToS3(final String bucketName,final File file,Category category){
		final String fileName = category.getId()+"/"+category.getName()+"/"+file.getName();
		LOGGER.info("Uploading file with name= " + fileName);
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead);
		amazonS3.putObject(putObjectRequest);
		URL imageUrl = amazonS3.getUrl(bucketName, fileName);
		return imageUrl;
	}	

	private void uploadGenericFileToS3Bucket(final String bucketName, final File file)throws Exception {
		
			final String uniqueFileName = file.getName();
			LOGGER.info("Uploading file with name= " + uniqueFileName);
			final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
			amazonS3.putObject(putObjectRequest);
		
	}
	
	@Override
	public ByteArrayOutputStream downloadFile(String keyName,UserProfile profile) {
		try {
            S3Object s3object = amazonS3.getObject(new GetObjectRequest(bucketName,profile.getId()+"/"+profile.getId()+"_"+ keyName));
            
            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos;
		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException ase) {
        	System.out.println("sCaught an AmazonServiceException from GET requests, rejected reasons:");
        	System.out.println("Error Message:    " + ase.getMessage());
        	System.out.println("HTTP Status Code: " + ase.getStatusCode());
        	System.out.println("AWS Error Code:   " + ase.getErrorCode());
        	System.out.println("Error Type:       " + ase.getErrorType());
			
			throw ase;
        } catch (AmazonClientException ace) {
        	
            throw ace;
        }
		
		return null;
	}
	
	@Override
	public ByteArrayOutputStream downloadFile(String keyName) {
		try {
            S3Object s3object = amazonS3.getObject(new GetObjectRequest(bucketName, keyName));
            
            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos;
		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException ase) {
        	System.out.println("sCaught an AmazonServiceException from GET requests, rejected reasons:");
        	System.out.println("Error Message:    " + ase.getMessage());
        	System.out.println("HTTP Status Code: " + ase.getStatusCode());
        	System.out.println("AWS Error Code:   " + ase.getErrorCode());
        	System.out.println("Error Type:       " + ase.getErrorType());
			
			throw ase;
        } catch (AmazonClientException ace) {
        	
            throw ace;
        }
		
		return null;
	}

	public URL uploadinvoicetos3(final String bucketName, final File file, UserOrder order){
		final String fileName = "orders"+"/"+order.getId()+"/"+file.getName();
		LOGGER.info("Uploading file with name= " + fileName);
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead);
		amazonS3.putObject(putObjectRequest);
		URL imageUrl = amazonS3.getUrl(bucketName, fileName);
		return imageUrl;
	}
}
