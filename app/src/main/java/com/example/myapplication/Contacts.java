package com.example.myapplication;

public class Contacts {
    private String name;
    private String email;
    private int picId;

    public Contacts(String name, String email, int picId) {
        this.name = name;
        this.email = email;
        this.picId = picId;
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

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }
}
