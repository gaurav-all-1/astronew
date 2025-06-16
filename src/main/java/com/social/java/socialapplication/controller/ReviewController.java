package com.social.java.socialapplication.controller;

import java.util.List;

import com.social.java.socialapplication.model.Review;
import com.social.java.socialapplication.service.ReviewService;

import com.social.java.socialapplication.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    SMSService smsService;
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

    @GetMapping("/testsms")
    public String testsms(){
        String s = "sent";
        try {
            smsService.sendSms("MAR03222023/3331","9873567279");
        } catch (Exception e) {
            s=e.getMessage();
            e.printStackTrace();
        }
        return s;
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
