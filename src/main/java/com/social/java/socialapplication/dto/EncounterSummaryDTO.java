package com.social.java.socialapplication.dto;

import java.time.LocalDateTime;

public class EncounterSummaryDTO {
    private Long id;
    private String status;
    private String department;
    private String consultationType;
    private LocalDateTime createdDate;

    public EncounterSummaryDTO(Long id, String status, String department, String consultationType, LocalDateTime createdDate, String patientName) {
        this.id = id;
        this.status = status;
        this.department = department;
        this.consultationType = consultationType;
        this.createdDate = createdDate;
        this.patientName = patientName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    private String patientName; // from user
}
