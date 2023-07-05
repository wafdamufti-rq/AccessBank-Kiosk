package com.kiosk.accessbank.source.model;

public class UpdateInfoRequest {
    private String channelCode = "KIOSKAPP";
    private String accountNo;
    private String bankCode = "044";
    private String email;
    private String phoneNumber;

    public String getChannelCode() {
        return channelCode;
    }

    public UpdateInfoRequest setChannelCode(String value) {
        this.channelCode = value;
        return this;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public UpdateInfoRequest setAccountNo(String value) {
        this.accountNo = value;
        return this;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String value) {
        this.bankCode = value;
    }

    public String getEmail() {
        return email;
    }

    public UpdateInfoRequest setEmail(String value) {
        this.email = value;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UpdateInfoRequest setPhoneNumber(String value) {
        this.phoneNumber = value;
        return this;
    }
}
