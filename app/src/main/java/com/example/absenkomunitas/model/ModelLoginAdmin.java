package com.example.absenkomunitas.model;

public class ModelLoginAdmin {
    private String email;
    private String password;
    private String nama;

    public ModelLoginAdmin() {
    }

    public ModelLoginAdmin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNama() {
        return nama;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
