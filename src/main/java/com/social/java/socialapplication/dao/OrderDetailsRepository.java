package com.social.java.socialapplication.dao;


import java.util.List;

import com.social.java.socialapplication.model.OrderDetails;
import com.social.java.socialapplication.model.UserOrder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long>{
    List<OrderDetails> findByUesrOrder(UserOrder uesrOrder);
}
