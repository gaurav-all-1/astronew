package com.nurtivillage.java.geonixApplication.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import com.nurtivillage.java.geonixApplication.dto.ProductInsert;
import com.nurtivillage.java.geonixApplication.dto.ProductUpdateDto;
import com.nurtivillage.java.geonixApplication.events.TestedEventPublisher;
import com.nurtivillage.java.geonixApplication.model.Inventory;
import com.nurtivillage.java.geonixApplication.model.Product;
import com.nurtivillage.java.geonixApplication.model.ProductImage;
import com.nurtivillage.java.geonixApplication.model.Review;
import com.nurtivillage.java.geonixApplication.model.Variant;
import com.nurtivillage.java.geonixApplication.service.AWSS3Service;
import com.nurtivillage.java.geonixApplication.service.ApiResponseService;
import com.nurtivillage.java.geonixApplication.service.InventoryService;
import com.nurtivillage.java.geonixApplication.service.ProductImageService;
import com.nurtivillage.java.geonixApplication.service.ProductService;
import com.nurtivillage.java.geonixApplication.service.ReviewService;
import com.nurtivillage.java.geonixApplication.service.VariantService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping(path = "/product")
@Validated
public class ProductController {
	private final static Logger log=LogManager.getLogger(ProductController.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private AWSS3Service awsService;
	@Autowired
	private ProductImageService productImageService;

	@Autowired
	private TestedEventPublisher testedEventPublisher;

	@Autowired
	private VariantService variantService;

	@GetMapping("/list")
	public ResponseEntity<ApiResponseService> getAllProductByPage(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "varient", defaultValue = "null", required = false) String variant) {
		try {
			Pageable firstPage = PageRequest.of(pageNo, 100, Direction.ASC, sortBy);

			// if(variant != "null"){
			// Variant variantData = variantService.getVariantBYName(variant);
			// System.out.println(variant);
			// Page<Product> product =
			// productService.getAllProductWithFilter(variantData,firstPage);
			// ApiResponseService res = new ApiResponseService("Product
			// List",true,product.toList(),product.getTotalPages());
			// return new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
			// }

			Page<Product> product = productService.getAllProduct(firstPage);
			ApiResponseService res = new ApiResponseService("Product List", true, product.toList(),
					product.getTotalPages());
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/list-all")
	public ResponseEntity<ApiResponseService> getAllProduct() {
		try {
			List<Product> product = productService.getProductAll();
			ApiResponseService res = new ApiResponseService("Product List", true, product);
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/info/{id}")
	public ResponseEntity<ApiResponseService> ProductInfo(@PathVariable Long id) {
		try {
			Optional<Product> product = productService.ProductInfo(id);
			// List<Review> reviews = reviewService.getReview(product.get());
			List<Inventory> inventory = inventoryService.getProductInventory(product.get());
			// product.get().setReview(reviews);

			// product.get().setVariant(inventory);

			ApiResponseService res = new ApiResponseService("product info", true, Arrays.asList(product.get()));

			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/productinfo/{name}")
	public ResponseEntity<ApiResponseService> ProductInfoByName(@PathVariable String name) {
		try {
			Optional<Product> product = productService.ProductInfoByName(name);
			// List<Review> reviews = reviewService.getReview(product.get());
			List<Inventory> inventory = inventoryService.getProductInventory(product.get());
			// product.get().setReview(reviews);

			// product.get().setVariant(inventory);

			ApiResponseService res = new ApiResponseService("product info", true, Arrays.asList(product.get()));

			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/producturl/{name}")
	public ResponseEntity<ApiResponseService> ProductInfoByUrl(@PathVariable String name) {
		try {
			Optional<Product> product = productService.productInfoByUrl(name);
			// List<Review> reviews = reviewService.getReview(product.get());
			List<Inventory> inventory = inventoryService.getProductInventory(product.get());
			// product.get().setReview(reviews);

			// product.get().setVariant(inventory);

			ApiResponseService res = new ApiResponseService("product info", true, Arrays.asList(product.get()));

			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<ApiResponseService> insertProduct(@Valid @RequestBody Product product) {
		try {
			Product insetProduct = productService.insertProduct(product);
			ApiResponseService res = new ApiResponseService("product create", true, Arrays.asList(insetProduct));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@PutMapping("/edit")
	public ResponseEntity<ApiResponseService> editProduct(@RequestBody Product product) {
		try {
			Product data = productService.insertProduct(product);
			ApiResponseService res = new ApiResponseService("update product", true, Arrays.asList(data));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/updateproduct")
	public ResponseEntity<ApiResponseService> updateProduct(@RequestBody Product product) {
		try {
			Product data = productService.updateProduct(product);
			ApiResponseService res = new ApiResponseService("update product", true, Arrays.asList(data));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponseService> deleteProduct(@PathVariable Long id) {
		try {
			String msg = productService.DeleteProduct(id);
			ApiResponseService res = new ApiResponseService(msg, true, Arrays.asList(id));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/highlighter")
	public ResponseEntity<ApiResponseService> highlighterProduct() {
		try {
			List<Product> data = productService.highlighterProduct();
			ApiResponseService res = new ApiResponseService("List of highlighter", true, data);
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/list/{categoryId}")
	
	public ResponseEntity<?> categoryProductLIst(@PathVariable Integer categoryId,Pageable pageable) {
		try {
            String catPage=pageable.getPageNumber()+"_"+categoryId+"_"+pageable.getPageSize();
			Page<Product> data = productService.getCategoryProducts(categoryId,pageable,catPage);
			ApiResponseService res = new ApiResponseService("Product List", true, data.toList(),
					data.getTotalPages());
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/uploadimage/{id}")
	public ResponseEntity<ApiResponseService> updateProductImage(
			@RequestPart(value = "file", required = true) final MultipartFile multipartFile, @PathVariable Long id)
			throws Exception {
		Optional<Product> product = null;
		File file = null;
		try {
			product = productService.ProductInfo(id);
			String url = awsService.uploadProductFile(multipartFile, product.get());
			productImageService.addImage(product.get(), url);
			ApiResponseService res = new ApiResponseService("file upload successfully", true, Arrays.asList(url));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService("file not uploaded. something went worng", true,
					Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping(value = "/deleteimage/{id}")
	public ResponseEntity<ApiResponseService> deleteProductImage(@PathVariable Long id) throws Exception {
		try {
			ProductImage productimage = productImageService.deleteImage(id);
			ApiResponseService res = new ApiResponseService("file upload successfully", true, Arrays.asList(id));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponseService res = new ApiResponseService("file not uploaded. something went worng", false,
					Arrays.asList(e.getMessage()));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// @PostMapping()
	// public ResponseEntity<ApiResponseService> filterProductList(@RequestBody
	// Map<String,String> request){
	// try {
	// // request.forEach(i->{
	// // System.out.println(i);
	// // });
	// ApiResponseService res = new ApiResponseService("file upload
	// successfully",true,Arrays.asList());
	// return new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
	// } catch (Exception e) {
	// ApiResponseService res = new ApiResponseService("file not uploaded. something
	// went worng",false,Arrays.asList(e.getMessage()));
	// return new
	// ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// }

	@PostMapping("/search")
	public List<?> search(@RequestBody Map<String, String> request) {
		try {
			List<Product> products = this.productService.searchProduct(request.get("key"));
			return products;
		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping("/menu")
	public Map<String, List<?>> categoryByProduct() {
		Map<String, List<?>> map = this.productService.getProductListByCategory();
		return map;
	}

	@PostMapping(value = "/test")
	public List<?> test(@RequestBody Map<String, String> request) {
		try {
			List<?> products = this.inventoryService.findbyname(request.get("key"));
			return products;
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/getProductsByVariant")
	public ResponseEntity<ApiResponseService> getProductsByVariant(@RequestParam String variant,
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy) {
		try {
			Pageable firstPage = PageRequest.of(pageNo, 10, Direction.ASC, sortBy);
			Variant variantData = variantService.getVariantBYName(variant);
			System.out.println(variant);
			Page<Product> product = productService.getAllProductWithFilter(variantData, firstPage);
			ApiResponseService res = new ApiResponseService("Product List", true, product.toList(),
					product.getTotalPages());
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			ApiResponseService res = new ApiResponseService(e.getMessage(), false, Arrays.asList("error"));
			return new ResponseEntity<ApiResponseService>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/{productId}/updateDescription")
	public ResponseEntity<?> updateDescription(@PathVariable Long productId,@RequestBody ProductUpdateDto productDto)throws Exception{
	try {log.info("Updating Product description --Start");
		Product savedProduct=productService.updateDescription(productId,productDto);
		log.info("Updating Product description --End");
		return new ResponseEntity<Product>(savedProduct,HttpStatus.OK);
	}	
	catch(Exception e) {
		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
}
