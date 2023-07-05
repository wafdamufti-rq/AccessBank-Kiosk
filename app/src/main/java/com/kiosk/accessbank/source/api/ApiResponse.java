package com.kiosk.accessbank.source.api;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    @SerializedName("response_code")
    private String code;
    @SerializedName("response_message")
    private String message;
    private T data;

    public ApiResponse(String code,String message, T data){
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
