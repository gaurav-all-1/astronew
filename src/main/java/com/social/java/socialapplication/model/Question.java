package com.social.java.socialapplication.model;

import javax.persistence.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text; // e.g. "Describe your symptoms"

    @ManyToOne
    @JoinColumn(name = "template_id")
    private QuestionnaireTemplate template;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionnaireTemplate getTemplate() {
        return template;
    }

    public void setTemplate(QuestionnaireTemplate template) {
        this.template = template;
    }
}
