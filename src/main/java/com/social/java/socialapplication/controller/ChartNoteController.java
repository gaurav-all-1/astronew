package com.social.java.socialapplication.controller;

import com.social.java.socialapplication.model.ChartNote;
import com.social.java.socialapplication.service.ChartNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounters")
public class ChartNoteController {

    private final ChartNoteService chartNoteService;

    @Autowired
    public ChartNoteController(ChartNoteService chartNoteService) {
        this.chartNoteService = chartNoteService;
    }

    @PostMapping("/{id}/chartnotes")
    public ResponseEntity<ChartNote> addChartNote(@PathVariable Long id, @RequestBody ChartNote chartNote) {
        return chartNoteService.addChartNote(id, chartNote)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/chartnotes")
    public ResponseEntity<List<ChartNote>> getChartNotes(@PathVariable Long id) {
        List<ChartNote> notes = chartNoteService.getChartNotesByEncounter(id);
        return ResponseEntity.ok(notes);
    }
}
