package com.social.java.socialapplication.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PrescriptionTreatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;              // Medicine / Treatment title
    private String pharmacy;           // Pharmacy name
    private String supply;             // Supply duration/amount
    private String refills;            // Number of refills
    private String compound;           // Compound details
    private String directions;         // Dosage / Usage directions
    private String pharmacyNotes;      // Notes for pharmacy

    private Boolean addWithNote;       // Checkbox true/false
    private String noteForPhysician;   // Additional note for physician
    private String messageForPatient;  // Message visible to patient
    private String specialInstructions;// Extra instructions

    private String videoFile;          // You can store file URL/path (instead of blob)

    @CreationTimestamp
    private LocalDateTime createdDate;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPharmacy() { return pharmacy; }
    public void setPharmacy(String pharmacy) { this.pharmacy = pharmacy; }

    public String getSupply() { return supply; }
    public void setSupply(String supply) { this.supply = supply; }

    public String getRefills() { return refills; }
    public void setRefills(String refills) { this.refills = refills; }

    public String getCompound() { return compound; }
    public void setCompound(String compound) { this.compound = compound; }

    public String getDirections() { return directions; }
    public void setDirections(String directions) { this.directions = directions; }

    public String getPharmacyNotes() { return pharmacyNotes; }
    public void setPharmacyNotes(String pharmacyNotes) { this.pharmacyNotes = pharmacyNotes; }

    public Boolean getAddWithNote() { return addWithNote; }
    public void setAddWithNote(Boolean addWithNote) { this.addWithNote = addWithNote; }

    public String getNoteForPhysician() { return noteForPhysician; }
    public void setNoteForPhysician(String noteForPhysician) { this.noteForPhysician = noteForPhysician; }

    public String getMessageForPatient() { return messageForPatient; }
    public void setMessageForPatient(String messageForPatient) { this.messageForPatient = messageForPatient; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public String getVideoFile() { return videoFile; }
    public void setVideoFile(String videoFile) { this.videoFile = videoFile; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
