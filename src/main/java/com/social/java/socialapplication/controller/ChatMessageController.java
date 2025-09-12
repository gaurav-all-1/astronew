package com.social.java.socialapplication.controller;


import com.social.java.socialapplication.model.ChatMessage;
import com.social.java.socialapplication.model.Encounter;
import com.social.java.socialapplication.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounters/{encounterId}/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    @Autowired
    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    // Get all messages for an encounter
    @GetMapping
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long encounterId) {
        return ResponseEntity.ok(chatMessageService.getMessagesForEncounter(encounterId));
    }

    // Send a message
    @PostMapping
    public ResponseEntity<ChatMessage> sendMessage(
            @PathVariable Long encounterId,
            @RequestBody ChatMessage message
    ) {
        // Ensure encounter id is set
        Encounter encounter = new Encounter();
        encounter.setId(encounterId);
        message.setEncounter(encounter);

        return ResponseEntity.ok(chatMessageService.saveMessage(message));
    }
}
