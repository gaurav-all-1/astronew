package com.social.java.socialapplication.model;

import javax.persistence.*;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EncounterQuestionnaire encounterQuestionnaire;

    @ManyToOne
    private Question question;

    @Lob
    private String response; // patient text answer

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EncounterQuestionnaire getEncounterQuestionnaire() {
        return encounterQuestionnaire;
    }

    public void setEncounterQuestionnaire(EncounterQuestionnaire encounterQuestionnaire) {
        this.encounterQuestionnaire = encounterQuestionnaire;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
