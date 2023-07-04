package com.kiosk.accessbank.source.repository;

import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.api.CustomerAccountResponse;
import com.kiosk.accessbank.source.api.LoginService;
import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.AccountDetailRequest;
import com.kiosk.accessbank.source.model.CustomerAccount;
import com.kiosk.accessbank.source.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepositoryImpl extends BaseRepository implements UserRepository{

    private LoginService loginService;
    public UserRepositoryImpl(LoginService loginService){
        this.loginService = loginService;
    }
    @Override
    public Single<ApiResponse<User>> login(Long authenticationNumber) {
        //TOD-O: dummy response
        return Single.just(new ApiResponse<User>(200,"success",new User("1",1389173,"michele")));
    }

    @Override
    public Single<ApiResponse<ArrayList<Account>>> getAllAccounts(long authenticationNumber) {
        ArrayList<Account> list =  new ArrayList<>();
        list.add(new Account(1,"michele","11212121", "24th May 2023", "michele@gmail.com","0913789173"));
        list.add(new Account(2,"backed","123123123", "24th May 2023", "backed@gmail.com","323321231321"));
        list.add(new Account(3,"sherly","123156456", "24th May 2023", "sherly@gmail.com","13123131321"));
        return Single.just(new ApiResponse<>(200,"success",list));
    }

    @Override
    public Single<CustomerAccountResponse> getAccountDetails(String accountNo) {
        return loginService.getAccountDetails(new AccountDetailRequest().setAccountNo(accountNo)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
