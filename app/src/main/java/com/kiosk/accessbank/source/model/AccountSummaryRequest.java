package com.kiosk.accessbank.source.model;

import com.google.gson.annotations.SerializedName;

public class AccountSummaryRequest {
    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getCustomerID() {
        return customerID;
    }

    public AccountSummaryRequest setCustomerId(String customerID) {
        this.customerID = customerID;
        return this;
    }

    @SerializedName("channel_code")
    private String channelCode = "KIOSKAPP";
    @SerializedName("account_no")
    private String customerID;
}
