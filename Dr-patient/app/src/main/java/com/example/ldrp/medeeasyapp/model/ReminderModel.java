package com.example.ldrp.medeeasyapp.model;

public class ReminderModel {

    private String uuid;
    private String title;
    private String description;
    private String date;
    private String time;

    public ReminderModel() {
    }


    public ReminderModel(String title, String description, String date, String time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public ReminderModel(String uuid, String title, String description, String date, String time) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
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
}
