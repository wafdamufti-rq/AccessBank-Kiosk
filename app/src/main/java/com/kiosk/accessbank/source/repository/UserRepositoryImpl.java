package com.kiosk.accessbank.source.repository;

import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.api.LoginService;
import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.User;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Single;

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
}
