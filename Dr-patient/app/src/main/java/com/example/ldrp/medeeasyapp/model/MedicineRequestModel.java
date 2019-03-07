package com.example.ldrp.medeeasyapp.model;

public class MedicineRequestModel {

    private String title;
    private String description;
    private String imgurl;
    private PatientModel patientModel;

    public MedicineRequestModel() {
    }

    public MedicineRequestModel(String title, String description, String imgurl, PatientModel patientModel) {
        this.title = title;
        this.description = description;
        this.imgurl = imgurl;
        this.patientModel = patientModel;
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public PatientModel getPatientModel() {
        return patientModel;
    }

    public void setPatientModel(PatientModel patientModel) {
        this.patientModel = patientModel;
    }
}
