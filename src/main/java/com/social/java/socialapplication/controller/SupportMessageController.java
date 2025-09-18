package com.social.java.socialapplication.controller;


import com.social.java.socialapplication.model.Encounter;
import com.social.java.socialapplication.model.SupportMessage;
import com.social.java.socialapplication.service.SupportMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounters/{encounterId}/supportChat")
public class SupportMessageController {

    private final SupportMessageService supportMessageService;
    @Autowired
    public SupportMessageController(SupportMessageService supportMessageService) {
        this.supportMessageService = supportMessageService;
    }

    // Get all messages for an encounter
    @GetMapping
    public ResponseEntity<List<SupportMessage>> getMessages(@PathVariable Long encounterId) {
        return ResponseEntity.ok(supportMessageService.getMessagesForEncounter(encounterId));
    }

    // Send a message
    @PostMapping
    public ResponseEntity<SupportMessage> sendMessage(
            @PathVariable Long encounterId,
            @RequestBody SupportMessage message
    ) {
        // Ensure encounter id is set
        Encounter encounter = new Encounter();
        encounter.setId(encounterId);
        message.setEncounter(encounter);

        return ResponseEntity.ok(supportMessageService.saveMessage(message));
    }
}
