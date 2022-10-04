package com.nurtivillage.java.geonixApplication.dao;


import java.util.List;

import com.nurtivillage.java.geonixApplication.model.OrderDetails;
import com.nurtivillage.java.geonixApplication.model.UserOrder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long>{
    List<OrderDetails> findByUesrOrder(UserOrder uesrOrder);
}
