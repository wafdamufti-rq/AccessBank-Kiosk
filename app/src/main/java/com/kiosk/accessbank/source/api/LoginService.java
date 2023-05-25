package com.kiosk.accessbank.source.api;

import com.kiosk.accessbank.source.model.User;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface LoginService {

    @GET("login")
    Single<ApiResponse<User>> login(Long authenticationNumber);
}
