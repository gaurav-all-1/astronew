package com.social.java.socialapplication.dao;

import java.util.List;

import com.social.java.socialapplication.model.DeviceMetadata;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, Long> {

    List<DeviceMetadata> findByUserId(Long userId);
}
