package com.kiosk.accessbank.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.Api;
import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.api.ServiceService;
import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.Service;
import com.kiosk.accessbank.source.model.User;
import com.kiosk.accessbank.source.repository.ServiceRepository;
import com.kiosk.accessbank.source.repository.UserRepository;
import com.kiosk.accessbank.util.UpdateType;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class MainViewModel extends BaseViewModel {


    private UserRepository userRepository;

    private ServiceRepository serviceRepository;
    private SavedStateHandle handle;


    @Inject
    public MainViewModel(UserRepository userRepository, ServiceRepository serviceRepository, SavedStateHandle handle) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.handle = handle;
    }

    private final MutableLiveData<ApiResponse<User>> _loginLiveData = new MutableLiveData<>();
    public LiveData<ApiResponse<User>> loginLiveData = _loginLiveData;

    private final MutableLiveData<ApiResponse<ArrayList<Service>>> _servicesLiveData = new MutableLiveData<>();

    public LiveData<ApiResponse<ArrayList<Service>>> servicesLiveData = _servicesLiveData;

    private final MutableLiveData<ApiResponse<ArrayList<Account>>> _accountsLiveData = new MutableLiveData<>();

    public LiveData<ApiResponse<ArrayList<Account>>> accountsLiveData = _accountsLiveData;


    private final MutableLiveData<Account> _selectedAccountLiveData = new MutableLiveData<>();

    public LiveData<Account> selectedAccountLiveData = _selectedAccountLiveData;


    private final MutableLiveData<UpdateType> _updateTypeLiveData = new MutableLiveData<>();

    public LiveData<UpdateType> updateTypeLiveData = _updateTypeLiveData;



    private final MutableLiveData<Boolean> _submittedLiveData = new MutableLiveData<>();

    public LiveData<Boolean> submittedLiveData = _submittedLiveData;

    private final MutableLiveData<Boolean> _delayLiveData = new MutableLiveData<>();

    public LiveData<Boolean> delayLiveData = _delayLiveData;
    // clear those after destroyed
    private User loggedUser = null;
    private Account selectedAccount = null;
    private Service selectedService = null;



    public void login(long authNumber){
        userRepository.login(authNumber).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull ApiResponse<User> userApiResponse) {
                loggedUser = userApiResponse.getData();
                _loginLiveData.postValue(userApiResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    public void setSelectedAccount(Account account){
        selectedAccount = account;
    }

    public void getAccount(){
        userRepository.getAllAccounts(loggedUser.getAuthNumber()).subscribe(new SingleObserver<ApiResponse<ArrayList<Account>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull ApiResponse<ArrayList<Account>> arrayListApiResponse) {

                _accountsLiveData.postValue(arrayListApiResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    public void setSelectedService(Service service){
        selectedService = service;
    }

    public void loadServices(){
        serviceRepository.getAllServices("1").subscribe(new SingleObserver<ApiResponse<ArrayList<Service>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull ApiResponse<ArrayList<Service>> arrayListApiResponse) {
                _servicesLiveData.postValue(arrayListApiResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    public void clearState() {
        loggedUser = null;
        selectedService = null;
        selectedAccount = null;
        _loginLiveData.postValue(null);
        _accountsLiveData.postValue(null);
        _servicesLiveData.postValue(null);
        _selectedAccountLiveData.postValue(null);
        _updateTypeLiveData.postValue(null);
        _submittedLiveData.postValue(null);

    }

    public void loadAccount() {
        _selectedAccountLiveData.postValue(selectedAccount);
    }
    
    public void getUpdateType(){
        _updateTypeLiveData.postValue(handle.get("updateType"));
    }

    public void submitUpdate(String toString) {
       _submittedLiveData.postValue(true);
    }

    public void clearEverything() {

        Single.timer(3, TimeUnit.SECONDS).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                    _delayLiveData.postValue(true);

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });
    }
}

