package com.kiosk.accessbank.source.repository;

import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.api.CustomerAccountResponse;
import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.User;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Single;

public interface UserRepository {

    Single<ApiResponse<User>> login(Long authenticationNumber);

    Single<ApiResponse<ArrayList<Account>>> getAllAccounts(long authenticationNumber);

    Single<CustomerAccountResponse> getAccountDetails(String accountNo);
}
