package com.nurtivillage.java.nutrivillageApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurtivillage.java.nutrivillageApplication.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
Payment findByRazorpayOrderId(String id);
Payment findByOrderId(Long id);

}
