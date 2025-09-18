package com.social.java.socialapplication.service;


import com.social.java.socialapplication.dao.ChatMessageRepository;
import com.social.java.socialapplication.dao.SupportMessageRepository;
import com.social.java.socialapplication.model.ChatMessage;
import com.social.java.socialapplication.model.SupportMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportMessageService {

    private final SupportMessageRepository supportMessageRepository;

    public SupportMessageService(SupportMessageRepository supportMessageRepository) {
        this.supportMessageRepository = supportMessageRepository;
    }

    public SupportMessage saveMessage(SupportMessage message) {
        return supportMessageRepository.save(message);
    }

    public List<SupportMessage> getMessagesForEncounter(Long encounterId) {
        return supportMessageRepository.findByEncounterIdOrderByCreatedDateAsc(encounterId);
    }
}
