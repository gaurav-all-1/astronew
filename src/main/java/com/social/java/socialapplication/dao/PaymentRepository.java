package com.social.java.socialapplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.java.socialapplication.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
Payment findByRazorpayOrderId(String id);
Payment findByOrderId(Long id);

}
