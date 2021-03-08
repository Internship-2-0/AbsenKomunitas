package com.example.absenkomunitas.model;

import com.google.android.gms.common.data.SingleRefDataBufferIterator;

public class ModelUser {
    private String nama;
    private String role;
    private String Uid;
    private String komunitas;

    public String getKomunitas() {
        return komunitas;
    }

    public void setKomunitas(String komunitas) {
        this.komunitas = komunitas;
    }

    private String timeStamp;

    public ModelUser() {
    }

    public ModelUser(String nama, String timeStamp, String komunitas) {
        this.nama = nama;
        this.timeStamp = timeStamp;
        this.komunitas = komunitas;
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
