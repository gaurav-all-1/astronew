package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.dto.EncounterSummaryDTO;
import com.social.java.socialapplication.model.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {

    // Search by status
    List<Encounter> findByStatus(String status);

    List<Encounter> findByStatusIn(List<String> statuses);

    List<Encounter> findByUserId(Long userId);

    // Search by createdDate range
    List<Encounter> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT new com.social.java.socialapplication.dto.EncounterSummaryDTO( e.id, e.status, e.department, e.consultationType, e.createdDate, u.firstName)FROM Encounter e JOIN e.user u")
    List<EncounterSummaryDTO> findAllSummaries();

    @Query("SELECT new com.social.java.socialapplication.dto.EncounterSummaryDTO(" +
            "e.id, e.status, e.department, e.consultationType, e.createdDate, u.firstName) " +
            "FROM Encounter e JOIN e.user u " +
            "WHERE e.status = :status")
    List<EncounterSummaryDTO> findSummariesByStatus(@Param("status") String status);
}