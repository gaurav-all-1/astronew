package com.social.java.socialapplication.service;

import com.social.java.socialapplication.dao.QuestionRepository;
import com.social.java.socialapplication.dao.QuestionnaireTemplateRepository;
import com.social.java.socialapplication.dto.QuestionnaireTemplateDTO;
import com.social.java.socialapplication.model.Question;
import com.social.java.socialapplication.model.QuestionnaireTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionnaireTemplateService {

    private final QuestionnaireTemplateRepository templateRepo;
    private final QuestionRepository questionRepo;

    public QuestionnaireTemplateService(QuestionnaireTemplateRepository templateRepo, QuestionRepository questionRepo) {
        this.templateRepo = templateRepo;
        this.questionRepo = questionRepo;
    }

    @Transactional
    public QuestionnaireTemplate createTemplate(QuestionnaireTemplateDTO dto) {
        QuestionnaireTemplate template = new QuestionnaireTemplate();
        template.setTitle(dto.getTitle());

        List<Question> questions = dto.getQuestions().stream()
                .map(qText -> {
                    Question q = new Question();
                    q.setText(qText);
                    q.setTemplate(template);
                    return q;
                })
                .collect(Collectors.toList());

        template.setQuestions(questions);
        return templateRepo.save(template);
    }

    @Transactional
    public QuestionnaireTemplate updateTemplate(Long id, QuestionnaireTemplateDTO dto) {
        QuestionnaireTemplate template = templateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        template.setTitle(dto.getTitle());
        template.getQuestions().clear();

        dto.getQuestions().forEach(qText -> {
            Question q = new Question();
            q.setText(qText);
            q.setTemplate(template);
            template.getQuestions().add(q);
        });

        return templateRepo.save(template);
    }

    public List<QuestionnaireTemplate> getAllTemplates() {
        return templateRepo.findAll();
    }

    public QuestionnaireTemplate getTemplate(Long id) {
        return templateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public void deleteTemplate(Long id) {
        templateRepo.deleteById(id);
    }
}
