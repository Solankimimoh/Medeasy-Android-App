package com.example.ldrp.medeeasyapp.model;

public class PrescriptionModel {

    private String patientUUID;
    private String title;
    private String description;
    private String imgUrl;
    private String date;


    public PrescriptionModel(String title, String description, String imgUrl, String date) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public PrescriptionModel() {
    }

    public PrescriptionModel(String patientUUID, String title, String description, String imgUrl, String date) {
        this.patientUUID = patientUUID;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
