package com.example.ldrp.medeeasyapp.model;

public class AppoinmentModel {


    private PatientModel patientModel;
    private String purpose;
    private String date;
    private String time;
    private String remarks;
    private boolean status;
    private PrescriptionModel prescriptionModel;
    private PatientReviewModel patientReviewModel;

    public AppoinmentModel() {
    }


    public AppoinmentModel(String purpose, String date, String time, String remarks, boolean status, PrescriptionModel prescriptionModel, PatientReviewModel patientReviewModel) {
        this.purpose = purpose;
        this.date = date;
        this.time = time;
        this.remarks = remarks;
        this.status = status;
        this.prescriptionModel = prescriptionModel;
        this.patientReviewModel = patientReviewModel;
    }

    public AppoinmentModel(PatientModel patientModel, String purpose, String date, String time, String remarks, boolean status, PrescriptionModel prescriptionModel, PatientReviewModel patientReviewModel) {
        this.patientModel = patientModel;
        this.purpose = purpose;
        this.date = date;
        this.time = time;
        this.remarks = remarks;
        this.status = status;
        this.prescriptionModel = prescriptionModel;
        this.patientReviewModel = patientReviewModel;
    }

    public PatientModel getPatientModel() {
        return patientModel;
    }

    public void setPatientModel(PatientModel patientModel) {
        this.patientModel = patientModel;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PrescriptionModel getPrescriptionModel() {
        return prescriptionModel;
    }

    public void setPrescriptionModel(PrescriptionModel prescriptionModel) {
        this.prescriptionModel = prescriptionModel;
    }

    public PatientReviewModel getPatientReviewModel() {
        return patientReviewModel;
    }

    public void setPatientReviewModel(PatientReviewModel patientReviewModel) {
        this.patientReviewModel = patientReviewModel;
    }
}
