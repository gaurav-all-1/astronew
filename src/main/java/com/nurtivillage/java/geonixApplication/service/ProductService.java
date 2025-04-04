package com.nurtivillage.java.geonixApplication.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.amazonaws.services.simplesystemsmanagement.model.GetInventoryRequest;
import com.nurtivillage.java.geonixApplication.dao.*;
import com.nurtivillage.java.geonixApplication.dto.ProductUpdateDto;
import com.nurtivillage.java.geonixApplication.model.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	private final static Logger log=LogManager.getLogger(ProductService.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VariantRepository variantRepo;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private InvoiceDataRepository invoiceDataRepository;
    public Page<Product> getAllProduct(Pageable pageable){
        try {
            Page<Product> allProduct = productRepository.
            findByDeletedAtIsNull(pageable);
            allProduct.forEach((var)->{
                var.setDiscription(null);
                var.setAdditional(null);
                // List<Variant> variants = ;
                if(var.getVariants().size() > 0){
                    Inventory variantInventory = inventoryRepository.findByProductIdAndVariantId(var.getId(),var.getVariants().get(0).getId());
                    var.setDefaultPrice(String.valueOf(variantInventory.getPrice()));
                    var.setMrp(String.valueOf(variantInventory.getMrp()));
                }
            });
            return allProduct;
        } catch (Exception e) {
            throw e;
        }
    }

    public InvoiceData insertInvoice(InvoiceData invoiceData){
        try {
           return invoiceDataRepository.save(invoiceData);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<InvoiceData> getInvoices(){
        try {
            return invoiceDataRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Product insertProduct(Product product){
        boolean alreadyExists = false;
        try {
            if(product.getId()!=null){
                Product p = productRepository.findById(product.getId()).get();
                p.setAdditional(product.getAdditional());
                p.setDiscription(product.getDiscription());
                p.setBrand(product.getBrand());
                p.setName(product.getName());
                p.setStatus(product.getStatus());
                product=p;
                alreadyExists = true;
            }

            Category c = product.getCategory();
            if(!categoryRepository.existsById(c.getId())){
                Category createCat = categoryRepository.save(c);
                product.setCategory(createCat);
            }else{
                Category category = categoryRepository.findById(c.getId()).get();
                product.setCategory(category);
            }
            List<Variant> savedVariants = new ArrayList<>();
            List<Variant> variants = product.getVariants();
            variants.forEach((var)->{
                System.out.println(var.getPrice());
                if(var.getId() == 0){
                    Variant v =   variantRepo.save(var);        
                    savedVariants.add(v);
                }else{
                    Optional<Variant> ov = variantRepo.findById(var.getId());
                    if(ov.isPresent()){
                        savedVariants.add(ov.get());
                    }
                }
            });
            
            product.setVariants(savedVariants);
            product.setDeletedAt(null);
            product.setCreatedAt(new Date());
            Product save = productRepository.save(product);
            if(!alreadyExists) {
                save.getVariants().forEach((var) -> {
                    Optional<Variant> ov = variantRepo.findById(var.getId());
                    Variant v = ov.get();
                    int quantity = 0;
                    int price = 0;
                    for (Variant savedVariant : variants) {
                        if (savedVariant.getId() != var.getId() && savedVariant.getName() != var.getName()) {
                            continue;
                        }
                        quantity = savedVariant.getQuantity();
                        price = savedVariant.getPrice();
                    }
                    System.out.println(price);
                    Inventory inventory = new Inventory(save, v, quantity, price);

                    inventoryService.addInventory(inventory);
                });
            }

            return save;
        } catch (Exception e) {
            throw e;
        }
    }

    public Product updateProduct(Product product){
        try {
            List<Variant> savedVariants = new ArrayList<>();
            Variant variant = product.getVariants().get(0);
            Inventory inventory = inventoryRepository.findByProductIdAndVariantId(product.getId(),variant.getId());
            inventory.setPrice(product.getVariants().get(0).getPrice());
            inventory.setMrp(product.getVariants().get(0).getMrp());
            inventoryRepository.save(inventory);

//            product.setVariants(savedVariants);
//            Product save = productRepository.save(product);

            return product;
        } catch (Exception e) {
            throw e;
        }
    }

//    public String DeleteProduct(Long id) throws Exception{
//        try {
//            if(!productRepository.existsById(id)){
//                throw new ExceptionService("product is deleted or not exists");
//            }
//            System.out.println(id);
//            Optional<Product> product = productRepository.findById(id);
//            product.get().setDeletedAt(new Date());
//            productRepository.save(product.get());
//            return "delete product";
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    public Optional<Product> ProductInfo(Long id) throws Exception {
        try {
            if(!productRepository.existsById(id)){
                throw new ExceptionService("product is not exists");
            }
            Optional<Product> productInfo = productRepository.findById(id);
            if(productInfo.get().getDeletedAt() != null){
                throw new ExceptionService("product is deleted");
            }
            List<Variant> variantList = productInfo.get().getVariants();
                variantList.forEach((var)->{
                   Inventory variantInventory = inventoryRepository.findByProductIdAndVariantId(id,var.getId());
                   var.setPrice(variantInventory.getPrice());
                   var.setMrp(variantInventory.getMrp());
                   var.setQuantity( variantInventory.getQuantity());
                });
            return productInfo;            
        } catch (Exception e) {
            throw e;
        }
    }


    public Optional<Product> ProductInfoByName(String name) throws Exception {
        try {
//            if(!productRepository.find(id)){
//                throw new ExceptionService("product is not exists");
//            }
            Optional<Product> productInfo = productRepository.findByName(name);
            if(!productInfo.isPresent()){
                throw new ExceptionService("product is not exists");
            }
            if(productInfo.get().getDeletedAt() != null){
                throw new ExceptionService("product is deleted");
            }
            List<Variant> variantList = productInfo.get().getVariants();
            variantList.forEach((var)->{
                Inventory variantInventory = inventoryRepository.findByProductIdAndVariantId(productInfo.get().getId(),var.getId());
                var.setPrice(variantInventory.getPrice());
                var.setMrp(variantInventory.getMrp());
                var.setQuantity( variantInventory.getQuantity());
            });
            return productInfo;
        } catch (Exception e) {
            throw e;
        }
    }

    public Optional<Product> productInfoByUrl(String url) throws Exception {
        try {
//            if(!productRepository.find(id)){
//                throw new ExceptionService("product is not exists");
//            }
            Optional<Product> productInfo = productRepository.findByProductUrl(url);
            if(!productInfo.isPresent()){
                throw new ExceptionService("product is not exists");
            }
            if(productInfo.get().getDeletedAt() != null){
                throw new ExceptionService("product is deleted");
            }
            List<Variant> variantList = productInfo.get().getVariants();
            variantList.forEach((var)->{
                Inventory variantInventory = inventoryRepository.findByProductIdAndVariantId(productInfo.get().getId(),var.getId());
                var.setPrice(variantInventory.getPrice());
                var.setMrp(variantInventory.getMrp());
                var.setQuantity( variantInventory.getQuantity());
            });
            return productInfo;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Product> highlighterProduct() {
        List<Product> productList = productRepository.findByStatusAndDeletedAtIsNull(1);
        return productList;
    }
  
    public List<Product> categoryProductLIst(Integer catId) throws Exception {
        try {
            if(!categoryRepository.existsById(catId)){
                throw new ExceptionService("Category is not exists");
            }
            List<Product> productList = productRepository.findByCategoryIdAndDeletedAtIsNull(catId);
//            productList.forEach(product->{
//                Integer rating = reviewService.avgRating(product.getId());
//                // Object[] defaultPrice = inventoryService.defaultPrice(product.getId());
//                // System.out.println(defaultPrice);
//                
//                
//                // product.setDefaultPrice(String.valueOf(defaultPrice)+"-"+String.valueOf(defaultPrice[0]));
//                rating = rating ==null?0:rating;
//                product.setRating(rating.intValue());
//                if(product.getVariants().size() > 0){
//                    Inventory variantInventory = inventoryRepository.findByProductIdAndVariantId(product.getId(),product.getVariants().get(0).getId());
//                    product.setDefaultPrice(String.valueOf(variantInventory.getPrice()));
//                }
//            });
            return productList;
        } catch (Exception e) {
            throw e;
        }
    }
// @Cacheable(value="categoryCache",key="#catPage")
    public Page<Product> getCategoryProducts(int categoryId,Pageable pageable,String catPage)throws Exception{
    	try {
    		 if(!categoryRepository.existsById(categoryId)){
                 throw new ExceptionService("Category is not exists");
             }
             Page<Product> productList = productRepository.getByCategoryIdAndDeletedAtIsnull(categoryId,pageable);
            productList.forEach(product->{
                Integer rating = reviewService.avgRating(product.getId());
                // Object[] defaultPrice = inventoryService.defaultPrice(product.getId());
                // System.out.println(defaultPrice);
                
                
                // product.setDefaultPrice(String.valueOf(defaultPrice)+"-"+String.valueOf(defaultPrice[0]));
                rating = rating ==null?0:rating;
                product.setRating(rating.intValue());
                if(product.getVariants().size() > 0){
                    Inventory variantInventory = inventoryRepository.findByProductIdAndVariantId(product.getId(),product.getVariants().get(0).getId());
                    product.setDefaultPrice(String.valueOf(variantInventory.getPrice()));
                    product.setMrp(String.valueOf(variantInventory.getMrp()));
                }
            });
            return productList;
    	}
    	catch(Exception e) {
    		throw e;
    	}
    }

    public List<Product> getProductAll() {
        try {
            List<Product> allProduct = productRepository.findByDeletedAtIsNull();
            allProduct.forEach((var)->{
                // List<Variant> variants = ;
                if(var.getVariants().size() > 0){
                    Inventory variantInventory = inventoryRepository.findByProductIdAndVariantId(var.getId(),var.getVariants().get(0).getId());
                    var.setDefaultPrice(String.valueOf(variantInventory.getPrice()));
                }
            });
            return allProduct;
        } catch (Exception e) {
            throw e;
        }
    }


    public void resetDeletedAtForProducts() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if (product.getId() > 1) {
                product.setDeletedAt(null);
                productRepository.save(product);
            }
        }
    }

    public Page<Product> getAllProductWithFilter(Variant i,Pageable firstPage) {
        try {
            Page<Product> allProduct = productRepository.
            findByVariantsAndDeletedAtIsNull(i,firstPage);
            allProduct.forEach((var)->{
                if(var.getVariants().size() > 0){
                    Inventory variantInventory = inventoryRepository.findByProductIdAndVariantId(var.getId(),var.getVariants().get(0).getId());
                    var.setDefaultPrice(String.valueOf(variantInventory.getPrice()));
                }
            });
            return allProduct;
        } catch (Exception e) {
            throw e;
        }
    }

	public List<Product> searchProduct(String str) {
		try {
			List<Product> products = this.productRepository.findBynameContainsAndDeletedAtIsNull(str);
			if (products.size() == 0) {
//				return products;
				Category category = null;
				List<Category> categories = this.categoryRepository.findBynameContains(str);
				if (categories.size() > 0) {
					category = categories.get(0);
					products = this.productRepository.findByCategoryIdAndDeletedAtIsNull(category.getId());
				}
			} 
			
			products.forEach(product->{
                Integer rating = reviewService.avgRating(product.getId());
                rating = rating ==null?0:rating;
                product.setRating(rating.intValue());
                if(product.getVariants().size() > 0){
                    Inventory variantInventory = inventoryRepository.findByProductIdAndVariantId(product.getId(),product.getVariants().get(0).getId());
                    product.setDefaultPrice(String.valueOf(variantInventory.getPrice()));
                }
            });
			
			return products;
		} catch (Exception e) {
			throw e;
		}
	}
//	@Cacheable(value = "productCache")
    public Map<String, List<?>> getProductListByCategory() {
        List<Category> c = categoryService.getCategories();
        Map<String,List<?>> products = new HashMap<>();
        c.forEach((category)->{
            List<Product> p;
            try {
                p = this.categoryProductLIst(category.getId());
                if(category.getName() != null){
                    products.put(category.getName(),p);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        // Map<String,List> products = this.productRepository.
        return products;
    }

   public Product updateDescription(Long productId,ProductUpdateDto productDto)throws Exception {
	   try {
		  Optional< Product >product=productRepository.findById(productId);
		  if(!product.isPresent()) {
			  log.error("Product doesn't exist with id: "+productId);
			  throw new Exception("Product doesn't exist with id: "+productId);
		  }
		  Product savedProduct=product.get();
		  savedProduct.setDiscription(productDto.getDescription());
		  savedProduct.setAdditional(productDto.getAdditional());
		  return productRepository.save(savedProduct);
		  
	   }
	   catch(Exception e) {
		   throw e;
	   }
   }
    // public List<Product> Filter


    // public List<Product> highlighterProduct(){
        // List<Product> highlighter = productRepository.findByHighlighter(1);
        // return highlighter;
    // }
}
