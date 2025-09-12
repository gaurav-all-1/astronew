package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {

    // Search by status
    List<Encounter> findByStatus(String status);

    List<Encounter> findByStatusIn(List<String> statuses);

    // Search by createdDate range
    List<Encounter> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}