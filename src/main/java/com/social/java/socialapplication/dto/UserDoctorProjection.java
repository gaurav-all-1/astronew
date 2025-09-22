package com.social.java.socialapplication.dto;

public interface UserDoctorProjection {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getPhoneNo();

    DoctorProfileInfo getDoctorProfile();

    // roles collection
    java.util.List<RoleInfo> getRoles();

    interface DoctorProfileInfo {
        String getDob();
        String getDesignation();
    }

    interface RoleInfo {
        String getName();
    }
}
