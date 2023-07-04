package com.kiosk.accessbank.source.api;

import com.kiosk.accessbank.source.model.AccountDetailRequest;
import com.kiosk.accessbank.source.model.CustomerAccount;
import com.kiosk.accessbank.source.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {

    @GET("login")
    Single<ApiResponse<User>> login(Long authenticationNumber);

    @POST("GetAccountDetails")
    Single<CustomerAccountResponse> getAccountDetails(@Body AccountDetailRequest accountDetailRequest);
}
