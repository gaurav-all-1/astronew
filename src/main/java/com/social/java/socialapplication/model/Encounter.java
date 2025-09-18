package com.social.java.socialapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Encounter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;
    private String height;
    private String weight;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<MediaAttachments> mediaAttachements;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ChartNote> chartNotes;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PrescriptionTreatment> treatments;



    private String status;
    private String bloodPressure;

    private String allergies;
    private String conditions;
    private String reportedMeds;
    private String department;
    private String complaints;
    private String advice;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private String consultationType;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<DiagnosisHistory> diagnosisHistoryList;

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void addChatMessage(ChatMessage message) {
        chatMessages.add(message);
        message.setEncounter(this);
    }

    public void removeChatMessage(ChatMessage message) {
        chatMessages.remove(message);
        message.setEncounter(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getReportedMeds() {
        return reportedMeds;
    }

    public void setReportedMeds(String reportedMeds) {
        this.reportedMeds = reportedMeds;
    }

    public List<DiagnosisHistory> getDiagnosisHistoryList() {
        return diagnosisHistoryList;
    }

    public void setDiagnosisHistoryList(List<DiagnosisHistory> diagnosisHistoryList) {
        this.diagnosisHistoryList = diagnosisHistoryList;
    }

    public List<MediaAttachments> getMediaAttachements() {
        return mediaAttachements;
    }

    public void setMediaAttachements(List<MediaAttachments> mediaAttachements) {
        this.mediaAttachements = mediaAttachements;
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

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<ChartNote> getChartNotes() {
        return chartNotes;
    }

    public void setChartNotes(List<ChartNote> chartNotes) {
        this.chartNotes = chartNotes;
    }

    public List<PrescriptionTreatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<PrescriptionTreatment> treatments) {
        this.treatments = treatments;
    }




}
