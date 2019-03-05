package com.example.ldrp.medeeasyapp.model;

public class PatientReviewModel {

    private String description;

    public PatientReviewModel(String description) {
        this.description = description;
    }

    public PatientReviewModel() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
