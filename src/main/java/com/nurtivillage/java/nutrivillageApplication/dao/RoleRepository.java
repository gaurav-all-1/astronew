package com.nurtivillage.java.geonixApplication.dao;

import com.nurtivillage.java.geonixApplication.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}
