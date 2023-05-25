package com.kiosk.accessbank.source.resource;

public class FailedResource<T> extends Resource<T> {

    public FailedResource(String message,int code,T data){
        this.isSuccess = true;
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
