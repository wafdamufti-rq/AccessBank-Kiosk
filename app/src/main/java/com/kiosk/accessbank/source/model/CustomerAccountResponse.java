package com.kiosk.accessbank.source.model;

import com.google.gson.annotations.SerializedName;
import com.kiosk.accessbank.source.model.CustomerAccount;

import java.util.List;

public class CustomerAccountResponse {
    @SerializedName("response_code")
    private String code;
    @SerializedName("response_message")

    private String message;
    @SerializedName("getcustomeracctsdetailsresp")

    private List<CustomerAccount> data;

    public CustomerAccountResponse(String code,String message, List<CustomerAccount> data){
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

    public List<CustomerAccount> getData() {
        return data;
    }

    public void setData(List<CustomerAccount> data) {
        this.data = data;
    }
}
