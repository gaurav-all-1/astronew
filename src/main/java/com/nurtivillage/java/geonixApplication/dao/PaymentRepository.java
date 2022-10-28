package com.nurtivillage.java.geonixApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nurtivillage.java.geonixApplication.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
Payment findByRazorpayOrderId(String id);
Payment findByOrderId(Long id);

}
