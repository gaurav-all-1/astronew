package com.nurtivillage.java.geonixApplication.dao;

import java.util.Date;
import java.util.List;

import com.nurtivillage.java.geonixApplication.model.Status;
import com.nurtivillage.java.geonixApplication.model.User;
import com.nurtivillage.java.geonixApplication.model.UserOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<UserOrder,Long>{
    List<UserOrder> findByUser(User user);

    List<UserOrder> findByStatusNotOrderByCreatedAtAsc(Status status);
    
    List<UserOrder> findByStatusOrderByCreatedAtAsc(Status status);
    // void getOneOrderBYAsc();

    List<UserOrder> findByStatusAndUserIdOrderByCreatedAtAsc(Status canceled, Long userId);

    @Query(value = "from UserOrder t where createdAt BETWEEN :startDate AND :endDate")
    public List<UserOrder> getAllBetweenDates(@Param("startDate") Date startDate, @Param("endDate")Date endDate);

}
