package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByEncounterIdOrderByCreatedDateAsc(Long encounterId);
}
