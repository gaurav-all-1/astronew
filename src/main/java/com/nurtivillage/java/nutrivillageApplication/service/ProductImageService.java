package com.nurtivillage.java.geonixApplication.service;

import java.util.List;
import java.util.Optional;

import com.nurtivillage.java.geonixApplication.dao.ProductImageRepository;
import com.nurtivillage.java.geonixApplication.dao.ProductRepository;
import com.nurtivillage.java.geonixApplication.model.Product;
import com.nurtivillage.java.geonixApplication.model.ProductImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired 
    private ProductRepository productRepository;

    public void addImage(Product product,String imageUrl){
        if(product.getProductImage().size() < 1){
            product.setImage(imageUrl);
        }
        
        ProductImage productImage = new ProductImage(product.getName(),imageUrl);
        ProductImage getProductImage = productImageRepository.save(productImage);
        List<ProductImage> imageList = product.getProductImage();
        imageList.add(getProductImage); 
        product.setProductImage(imageList);
        productRepository.save(product);
    }

    public ProductImage deleteImage(Long id){
        try {
            Optional<ProductImage> productImage = productImageRepository.findById(id);
            productImageRepository.deleteById(id);
            return productImage.get();
        } catch (Exception e) {
            throw e;
        }
    }
}
