package com.kiosk.accessbank.source.resource;

public class SuccessResource<T> extends Resource<T> {

    public SuccessResource(String message,int code,T data){
        this.isSuccess = true;
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
