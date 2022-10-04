package com.nurtivillage.java.nutrivillageApplication.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import com.nurtivillage.java.nutrivillageApplication.model.Review;
import com.nurtivillage.java.nutrivillageApplication.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @GetMapping("/list")
    public ResponseEntity<?> getAllReview(){
        try {
            List<Review> review = reviewService.getAllReview();
            return new ResponseEntity<List>(review,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<?> getProductList(@PathVariable Long id){
        try {
            List<Review> review = reviewService.getProductReview(id);
            return new ResponseEntity<List>(review,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/addReview")
    public ResponseEntity<?> addReview(@RequestBody Review review){
        try {
            Review r = reviewService.addReview(review);
            return new ResponseEntity<Review>(r,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
