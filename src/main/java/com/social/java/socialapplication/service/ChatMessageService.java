package com.social.java.socialapplication.service;


import com.social.java.socialapplication.dao.ChatMessageRepository;
import com.social.java.socialapplication.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesForEncounter(Long encounterId) {
        return chatMessageRepository.findByEncounterIdOrderByCreatedDateAsc(encounterId);
    }
}
