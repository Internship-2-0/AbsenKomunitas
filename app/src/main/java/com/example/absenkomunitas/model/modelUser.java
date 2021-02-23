package com.example.absenkomunitas.model;

public class modelUser {
    private String nama;
    private String role;
    private String Uid;
    private String timeStamp;

    public modelUser() {
    }

    public modelUser(String nama, String timeStamp) {
        this.nama = nama;
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
