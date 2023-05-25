package com.kiosk.accessbank.source.model;

public class BvnAccount {

    private String id;
    private long authNumber;
    private String Name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAuthNumber() {
        return authNumber;
    }

    public void setAuthNumber(long authNumber) {
        this.authNumber = authNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public BvnAccount(String id, long authNumber, String name) {
        this.id = id;
        this.authNumber = authNumber;
        Name = name;
    }
}
