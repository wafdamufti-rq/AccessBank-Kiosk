package com.kiosk.accessbank.source.model;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class AccountDetailRequest {

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public AccountDetailRequest setAccountNo(String accountNo) {
        this.accountNo = accountNo;
        return this;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @SerializedName("channel_code")
    private String channelCode = "KIOSKAPP";
    @SerializedName("account_no")
    private String accountNo;
    @SerializedName("bank_code")
    private String bankCode = "044";

}
