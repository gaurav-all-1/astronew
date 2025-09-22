package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {}