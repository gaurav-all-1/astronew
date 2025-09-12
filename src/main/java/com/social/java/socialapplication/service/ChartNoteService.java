package com.social.java.socialapplication.service;


import com.social.java.socialapplication.dao.EncounterRepository;
import com.social.java.socialapplication.model.ChartNote;
import com.social.java.socialapplication.model.Encounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ChartNoteService {

    private final EncounterRepository encounterRepository;

    @Autowired
    public ChartNoteService(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    public Optional<ChartNote> addChartNote(Long encounterId, ChartNote chartNote) {
        return encounterRepository.findById(encounterId).map(encounter -> {
            encounter.getChartNotes().add(chartNote);
            encounterRepository.save(encounter); // cascade saves chartNote
            return chartNote;
        });
    }

    public List<ChartNote> getChartNotesByEncounter(Long encounterId) {
        return encounterRepository.findById(encounterId)
                .map(Encounter::getChartNotes)
                .orElse(Collections.emptyList());
    }
}
