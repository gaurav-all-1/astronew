package com.social.java.socialapplication.dto;

import java.util.List;

public class QuestionnaireTemplateDTO {
    private Long id;
    private String title;
    private List<String> questions; // just texts

    // getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
