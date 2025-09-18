package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.ChatMessage;
import com.social.java.socialapplication.model.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {
    List<SupportMessage> findByEncounterIdOrderByCreatedDateAsc(Long encounterId);
}
