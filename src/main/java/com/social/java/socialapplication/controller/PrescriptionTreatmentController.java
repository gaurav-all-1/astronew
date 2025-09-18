package com.social.java.socialapplication.controller;

import com.social.java.socialapplication.model.Encounter;
import com.social.java.socialapplication.model.PrescriptionTreatment;
import com.social.java.socialapplication.dao.EncounterRepository;
import com.social.java.socialapplication.dao.PrescriptionTreatmentRepository;
import com.social.java.socialapplication.response.ApiResultFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/encounters")
public class PrescriptionTreatmentController {

    private final EncounterRepository encounterRepository;
    private final PrescriptionTreatmentRepository prescriptionRepository;

    @Autowired
    public PrescriptionTreatmentController(EncounterRepository encounterRepository,
                                           PrescriptionTreatmentRepository prescriptionRepository) {
        this.encounterRepository = encounterRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    // ✅ Add Prescription to Encounter
    @PostMapping("/{encounterId}/prescriptions")
    public ResponseEntity<?> addPrescription(@PathVariable Long encounterId,
                                             @RequestBody PrescriptionTreatment prescription) {
        Optional<Encounter> encounterOpt = encounterRepository.findById(encounterId);
        if (!encounterOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Encounter encounter = encounterOpt.get();


        encounter.getTreatments().add(prescription);
        encounterRepository.save(encounter);

        return ResponseEntity.ok(prescription);
    }

    // ✅ Get Prescriptions for Encounter
    @GetMapping("/{encounterId}/prescriptions")
    public ResponseEntity<List<PrescriptionTreatment>> getPrescriptions(@PathVariable Long encounterId) {
        Optional<Encounter> encounterOpt = encounterRepository.findById(encounterId);
        return encounterOpt.map(encounter -> ResponseEntity.ok(encounter.getTreatments())).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/prescriptions")
    public ResponseEntity<ApiResultFormat<PrescriptionTreatment>> getAllPrescriptions() {
        List<PrescriptionTreatment> treatments= prescriptionRepository.findAll();
        return ResponseEntity.ok(new ApiResultFormat<>(treatments, treatments.size()));

    }

    @PutMapping("/{encounterId}/prescriptions/{prescriptionId}")
    public ResponseEntity<PrescriptionTreatment> updatePrescriptionApi(
            @PathVariable Long encounterId,
            @PathVariable Long prescriptionId,
            @RequestBody PrescriptionTreatment prescription) {

        PrescriptionTreatment updated = updatePrescription(encounterId, prescriptionId, prescription);
        return ResponseEntity.ok(updated);
    }

    public PrescriptionTreatment updatePrescription(Long encounterId, Long prescriptionId, PrescriptionTreatment updatedPrescription) {
        Encounter encounter = encounterRepository.findById(encounterId)
                .orElseThrow(() -> new RuntimeException("Encounter not found with id " + encounterId));

        PrescriptionTreatment existingPrescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id " + prescriptionId));

        // ✅ Update fields
        existingPrescription.setTitle(updatedPrescription.getTitle());
        existingPrescription.setPharmacy(updatedPrescription.getPharmacy());
        existingPrescription.setSupply(updatedPrescription.getSupply());
        existingPrescription.setRefills(updatedPrescription.getRefills());
        existingPrescription.setCompound(updatedPrescription.getCompound());
        existingPrescription.setDirections(updatedPrescription.getDirections());
        existingPrescription.setPharmacyNotes(updatedPrescription.getPharmacyNotes());
        existingPrescription.setAddWithNote(updatedPrescription.getAddWithNote());
        existingPrescription.setNoteForPhysician(updatedPrescription.getNoteForPhysician());
        existingPrescription.setMessageForPatient(updatedPrescription.getMessageForPatient());
        existingPrescription.setSpecialInstructions(updatedPrescription.getSpecialInstructions());
        existingPrescription.setVideoFile(updatedPrescription.getVideoFile());

        return prescriptionRepository.save(existingPrescription);
    }
}
