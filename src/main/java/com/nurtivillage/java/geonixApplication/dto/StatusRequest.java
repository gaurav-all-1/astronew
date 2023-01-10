package com.nurtivillage.java.geonixApplication.dto;

public class StatusRequest {
    private Long id;
    private String status;
    private String comment;

    private String trackingUrl;

    private String trackingno;

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public String getTrackingno() {
        return trackingno;
    }

    public void setTrackingno(String trackingno) {
        this.trackingno = trackingno;
    }

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
