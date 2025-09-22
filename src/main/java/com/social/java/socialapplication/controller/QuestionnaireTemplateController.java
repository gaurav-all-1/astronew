package com.social.java.socialapplication.controller;

import com.social.java.socialapplication.dto.QuestionnaireTemplateDTO;
import com.social.java.socialapplication.model.Question;
import com.social.java.socialapplication.model.QuestionnaireTemplate;
import com.social.java.socialapplication.service.QuestionnaireTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/templates")
public class QuestionnaireTemplateController {

    private final QuestionnaireTemplateService templateService;

    public QuestionnaireTemplateController(QuestionnaireTemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    public ResponseEntity<QuestionnaireTemplateDTO> createTemplate(@RequestBody QuestionnaireTemplateDTO dto) {
        QuestionnaireTemplate saved = templateService.createTemplate(dto);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionnaireTemplateDTO> updateTemplate(
            @PathVariable Long id,
            @RequestBody QuestionnaireTemplateDTO dto) {
        QuestionnaireTemplate updated = templateService.updateTemplate(id, dto);
        return ResponseEntity.ok(toDto(updated));
    }

    @GetMapping
    public ResponseEntity<List<QuestionnaireTemplateDTO>> getAllTemplates() {
        List<QuestionnaireTemplateDTO> list = templateService.getAllTemplates()
                .stream().map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionnaireTemplateDTO> getTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(templateService.getTemplate(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    // helper mapping
    private QuestionnaireTemplateDTO toDto(QuestionnaireTemplate entity) {
        QuestionnaireTemplateDTO dto = new QuestionnaireTemplateDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setQuestions(entity.getQuestions()
                .stream().map(
                        Question::getText).collect(Collectors.toList()));
        return dto;
    }
}
