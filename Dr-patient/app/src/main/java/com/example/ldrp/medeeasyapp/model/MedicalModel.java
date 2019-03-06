package com.example.ldrp.medeeasyapp.model;

public class MedicalModel {
    private String uid;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String address;
    private String types;
    private String license;


    public MedicalModel(String name, String email, String password, String mobile, String address, String types, String license) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
        this.types = types;
        this.license = license;
    }

    public MedicalModel() {
    }

    public MedicalModel(String uid, String name, String email, String password, String mobile, String address, String types, String license) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
        this.types = types;
        this.license = license;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
