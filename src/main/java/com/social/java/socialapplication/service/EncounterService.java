package com.social.java.socialapplication.service;

import com.social.java.socialapplication.dao.EncounterRepository;
import com.social.java.socialapplication.dto.EncounterSummaryDTO;
import com.social.java.socialapplication.model.Encounter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EncounterService {

    private final EncounterRepository encounterRepository;

    public EncounterService(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    public List<Encounter> getAllEncounters() {
        return encounterRepository.findAll();
    }

    public Optional<Encounter> getEncounterById(Long id) {
        return encounterRepository.findById(id);
    }

    public Encounter createEncounter(Encounter encounter) {
        encounter.setStatus("Assigned");
        return encounterRepository.save(encounter);
    }

    public Encounter updateEncounter(Long id, Encounter updatedEncounter) {
        return encounterRepository.findById(id).map(encounter -> {
            encounter.setUser(updatedEncounter.getUser());
            encounter.setHeight(updatedEncounter.getHeight());
            encounter.setWeight(updatedEncounter.getWeight());
            encounter.setStatus(updatedEncounter.getStatus());
            encounter.setBloodPressure(updatedEncounter.getBloodPressure());
            encounter.setAllergies(updatedEncounter.getAllergies());
            encounter.setConditions(updatedEncounter.getConditions());
            encounter.setReportedMeds(updatedEncounter.getReportedMeds());
            encounter.setDepartment(updatedEncounter.getDepartment());
            encounter.setComplaints(updatedEncounter.getComplaints());
            encounter.setAdvice(updatedEncounter.getAdvice());
            encounter.setConsultationType(updatedEncounter.getConsultationType());
            encounter.setMediaAttachements(updatedEncounter.getMediaAttachements());
            encounter.setDiagnosisHistoryList(updatedEncounter.getDiagnosisHistoryList());
            return encounterRepository.save(encounter);
        }).orElseThrow(() -> new RuntimeException("Encounter not found with ID: " + id));
    }

    public void deleteEncounter(Long id) {
        encounterRepository.deleteById(id);
    }

    public Encounter updateStatus(Long id, String status) {
        return encounterRepository.findById(id).map(encounter -> {
            encounter.setStatus(status);
            return encounterRepository.save(encounter);
        }).orElseThrow(() -> new RuntimeException("Encounter not found with ID: " + id));
    }

    public List<Encounter> bulkUpdateStatus(List<Long> ids, String status) {
        List<Encounter> encounters = encounterRepository.findAllById(ids);
        encounters.forEach(encounter -> encounter.setStatus(status));
        return encounterRepository.saveAll(encounters);
    }

    public List<Encounter> getEncountersByStatus(String status) {
        return encounterRepository.findByStatus(status);
    }

    public List<Encounter> getEncountersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return encounterRepository.findByCreatedDateBetween(startDate, endDate);
    }

    public List<Encounter> getEncountersByStatuses(List<String> statuses) {
        return encounterRepository.findByStatusIn(statuses);
    }

    public List<Encounter> getEncountersByUserId(Long userId) {
        return encounterRepository.findByUserId(userId);
    }

    public List<EncounterSummaryDTO> getEncounterSummary(){
        return encounterRepository.findAllSummaries();
    }

    public List<EncounterSummaryDTO> getEncounterSummaryByStatus(String status){
        return encounterRepository.findSummariesByStatus(status);
    }
}
