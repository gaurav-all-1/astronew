package com.social.java.socialapplication.dao;

import com.social.java.socialapplication.model.DoctorProfile;
import com.social.java.socialapplication.model.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface DoctorProfileRepository extends CrudRepository<DoctorProfile, Integer> {

}
