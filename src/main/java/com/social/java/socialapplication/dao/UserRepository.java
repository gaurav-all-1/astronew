package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.dto.UserDoctorProjection;
import com.social.java.socialapplication.dto.UserSummaryDTO;
import com.social.java.socialapplication.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByEmail(String email);
    @Override
    void delete(User user);
    
    User findByForgotPasswordKey(String key);
    
    User findByPhoneNo(String phoneNo);

    // Automatic projection
    List<UserDoctorProjection> findAllBy();

    // OR custom query if you want to control joins
    @Query("SELECT u FROM User u JOIN u.doctorProfile d")
    Page<UserDoctorProjection> findAllUsersWithDoctor(Pageable pageable);

    // All users + doctor info + roles
    @Query("SELECT u FROM User u LEFT JOIN u.doctorProfile d JOIN u.roles r ")
    Page<UserDoctorProjection> findAllUsersWithDoctorAndRoles(Pageable pageable);

    // Filter by role name
    @Query("SELECT new com.social.java.socialapplication.dto.UserSummaryDTO(" +
            "u.id, u.firstName, u.lastName, u.email, u.phoneNo, d.dob, d.designation) " +
            "FROM User u " +
            "LEFT JOIN u.doctorProfile d " +
            "JOIN u.roles r " +
            "WHERE r.name = :roleName")
    Page<UserSummaryDTO> findUsersByRoleName(@Param("roleName") String roleName, Pageable pageable);

}
