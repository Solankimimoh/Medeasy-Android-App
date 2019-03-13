package com.example.laboratory;

public class ReportModel {

    private String patientUUID;
    private String title;
    private String description;
    private String imgUrl;

    public ReportModel(String patientUUID, String title, String description, String imgUrl) {
        this.patientUUID = patientUUID;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public ReportModel(String title, String description, String imgUrl) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public ReportModel() {
    }

    public String getPatientUUID() {
        return patientUUID;
    }

    public void setPatientUUID(String patientUUID) {
        this.patientUUID = patientUUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
