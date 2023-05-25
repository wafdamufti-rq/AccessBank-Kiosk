package com.kiosk.accessbank.source.model;

public class Account {
    private int id;
    private String name;
    private String number;
    private String date;
    private String email;
    private String phoneNumber;

    public Account(int id, String name, String number, String date, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.date = date;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public Account(int id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
