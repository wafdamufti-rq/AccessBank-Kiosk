package com.kiosk.accessbank.source.api;

import com.kiosk.accessbank.source.model.AccountDetailRequest;
import com.kiosk.accessbank.source.model.AccountSummaryRequest;
import com.kiosk.accessbank.source.model.AccountSummaryResponse;
import com.kiosk.accessbank.source.model.CustomerAccountResponse;
import com.kiosk.accessbank.source.model.UpdateInfoRequest;
import com.kiosk.accessbank.source.model.User;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

    @GET("login")
    Single<ApiResponse<User>> login(Long authenticationNumber);

    @POST("GetAccountDetails")
    Single<CustomerAccountResponse> getAccountDetails(@Body AccountDetailRequest accountDetailRequest);

    @POST("GetAccountSummaryByCustomerID")
    Single<AccountSummaryResponse> getAccountSummaryByCustomerId(@Body AccountSummaryRequest accountSummaryRequest);

    @POST("ChangeEmail")
    Single<ApiResponse<Object>> changeEmail(@Body UpdateInfoRequest updateInfoRequest);

    @POST("ChangePhoneNumber")
    Single<ApiResponse<Object>> changePhoneNumber(@Body UpdateInfoRequest updateInfoRequest);
}
