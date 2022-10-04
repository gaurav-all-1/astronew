package com.nurtivillage.java.geonixApplication.dao;

import java.util.List;

import com.nurtivillage.java.geonixApplication.model.DeviceMetadata;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, Long> {

    List<DeviceMetadata> findByUserId(Long userId);
}
