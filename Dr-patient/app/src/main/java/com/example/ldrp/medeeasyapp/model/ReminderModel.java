package com.example.ldrp.medeeasyapp.model;

public class ReminderModel {

    private String uuid;
    private String title;
    private String description;


    public ReminderModel() {
    }

    public ReminderModel(String uuid, String title, String description) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
    }

    public ReminderModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
