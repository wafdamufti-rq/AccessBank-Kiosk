package com.kiosk.accessbank.source.repository;

import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.api.UserService;
import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.AccountDetailRequest;
import com.kiosk.accessbank.source.model.AccountSummaryRequest;
import com.kiosk.accessbank.source.model.AccountSummaryResponse;
import com.kiosk.accessbank.source.model.CustomerAccountResponse;
import com.kiosk.accessbank.source.model.UpdateInfoRequest;
import com.kiosk.accessbank.source.model.User;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepositoryImpl extends BaseRepository implements UserRepository {

    private UserService loginService;

    public UserRepositoryImpl(UserService userService) {
        this.loginService = userService;
    }

    @Override
    public Single<ApiResponse<User>> login(Long authenticationNumber) {
        //TOD-O: dummy response
        return Single.just(new ApiResponse<User>("00", "success", new User("1", 1389173, "michele")));
    }

    @Override
    public Single<ApiResponse<ArrayList<Account>>> getAllAccounts(long authenticationNumber) {
        ArrayList<Account> list = new ArrayList<>();
        list.add(new Account(1, "michele", "11212121", "24th May 2023", "michele@gmail.com", "0913789173"));
        list.add(new Account(2, "backed", "123123123", "24th May 2023", "backed@gmail.com", "323321231321"));
        list.add(new Account(3, "sherly", "123156456", "24th May 2023", "sherly@gmail.com", "13123131321"));
        return Single.just(new ApiResponse<>("00", "success", list));
    }

    @Override
    public Single<CustomerAccountResponse> getAccountDetails(String accountNo) {
        return loginService.getAccountDetails(new AccountDetailRequest().setAccountNo(accountNo)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<AccountSummaryResponse> getAccountSummaryByCustomerID(String customerId) {
        return loginService.getAccountSummaryByCustomerId(new AccountSummaryRequest().setCustomerId(customerId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<ApiResponse<Object>> changeEmail(String accountNo, String email) {
        return loginService.changeEmail(new UpdateInfoRequest().setAccountNo(accountNo).setEmail(email)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<ApiResponse<Object>> changePhoneNumber(String accountNo, String phoneNumber) {
        return loginService.changePhoneNumber(new UpdateInfoRequest().setAccountNo(accountNo).setPhoneNumber(phoneNumber)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
