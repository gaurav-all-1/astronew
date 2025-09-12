package com.social.java.socialapplication.model;

import javax.persistence.*;

@Entity
@Table(name = "diagnosis_history")
public class DiagnosisHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diagnosisType;

    @Column(length = 1000) // in case complaint history is long
    private String complaintHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiagnosisType() {
        return diagnosisType;
    }

    public void setDiagnosisType(String diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    public String getComplaintHistory() {
        return complaintHistory;
    }

    public void setComplaintHistory(String complaintHistory) {
        this.complaintHistory = complaintHistory;
    }
}