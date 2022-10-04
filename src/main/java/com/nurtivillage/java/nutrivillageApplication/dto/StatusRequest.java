package com.nurtivillage.java.geonixApplication.dto;

public class StatusRequest {
    private Long id;
    private String status;
    private String comment;

    public String getComment() {
        return comment;
    }

    public Long getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
