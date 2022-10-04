package com.nurtivillage.java.nutrivillageApplication.dao;


import java.util.List;

import com.nurtivillage.java.nutrivillageApplication.model.OrderDetails;
import com.nurtivillage.java.nutrivillageApplication.model.UserOrder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long>{
    List<OrderDetails> findByUesrOrder(UserOrder uesrOrder);
}
