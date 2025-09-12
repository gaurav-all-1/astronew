package com.social.java.socialapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.java.socialapplication.model.Encounter;
import com.social.java.socialapplication.model.MediaAttachments;
import com.social.java.socialapplication.response.ApiResultFormat;
import com.social.java.socialapplication.service.AWSS3Service;
import com.social.java.socialapplication.service.EncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/encounters")
public class EncounterController {

    private final AWSS3Service awss3Service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final EncounterService encounterService;
    @Autowired
    public EncounterController(AWSS3Service awss3Service, EncounterService encounterService) {
        this.awss3Service = awss3Service;
        this.encounterService = encounterService;
    }

    @GetMapping
    public ResponseEntity<ApiResultFormat<Encounter>> getAllEncounters() {
        List<Encounter> encounters = encounterService.getAllEncounters();
        ApiResultFormat<Encounter> result = new ApiResultFormat<>(encounters, encounters.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResultFormat<Encounter>> getEncounterById(@PathVariable Long id) {
        return encounterService.getEncounterById(id)
                .map(encounter -> {
                    ApiResultFormat<Encounter> result = new ApiResultFormat<>(
                            Collections.singletonList(encounter),
                            1
                    );
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Encounter> createEncounter(
            @RequestPart("encounter") String encounter,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws Exception {

        Encounter encounterObj = objectMapper.readValue(encounter, Encounter.class);

        List<MediaAttachments> attachments = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String url = awss3Service.uploadingMediaAttachments(file);
                MediaAttachments attachment = new MediaAttachments();
                attachment.setName(file.getOriginalFilename());
                attachment.setUrl(url);
                attachment.setFileType(file.getContentType());
                attachments.add(attachment);
            }
        }

        encounterObj.setMediaAttachements(attachments);
        Encounter savedEncounter = encounterService.createEncounter(encounterObj);

        return ResponseEntity.ok(savedEncounter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Encounter> updateEncounter(@PathVariable Long id, @RequestBody Encounter updatedEncounter) {
        return ResponseEntity.ok(encounterService.updateEncounter(id, updatedEncounter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEncounter(@PathVariable Long id) {
        encounterService.deleteEncounter(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Encounter> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(encounterService.updateStatus(id, status));
    }

    @PatchMapping("/bulk-status")
    public ResponseEntity<List<Encounter>> bulkUpdateStatus(@RequestParam List<Long> ids, @RequestParam String status) {
        return ResponseEntity.ok(encounterService.bulkUpdateStatus(ids, status));
    }

    // 🔹 Search by Status
    @GetMapping("/search/status")
    public ResponseEntity<ApiResultFormat<Encounter>> getEncountersByStatus(
            @RequestParam String status
    ) {
        List<String> statuses = Arrays.stream(status.split(","))
                .map(String::trim).collect(Collectors.toList());

        List<Encounter> encounters = encounterService.getEncountersByStatuses(statuses);
        return ResponseEntity.ok(new ApiResultFormat<>(encounters, encounters.size()));
    }

    // 🔹 Search by Date Range
    @GetMapping("/search/dates")
    public ResponseEntity<ApiResultFormat<Encounter>> getEncountersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<Encounter> encounters = encounterService.getEncountersBetweenDates(startDate, endDate);
        return ResponseEntity.ok(new ApiResultFormat<>(encounters, encounters.size()));
    }
}
