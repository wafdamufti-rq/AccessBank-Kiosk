package com.kiosk.accessbank.source.model;

import com.google.gson.annotations.SerializedName;
import com.kiosk.accessbank.source.model.AccountSummary;
import com.kiosk.accessbank.source.model.CustomerAccount;

import java.util.List;

public class AccountSummaryResponse
{
    @SerializedName("response_code")
    private String code;
    @SerializedName("response_message")

    private String message;
    @SerializedName("getcustomeracctsdetailsresp")

    private List<AccountSummary> data;

    public AccountSummaryResponse(String code,String message, List<AccountSummary> data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AccountSummary> getData() {
        return data;
    }

    public void setData(List<AccountSummary> data) {
        this.data = data;
    }
}
