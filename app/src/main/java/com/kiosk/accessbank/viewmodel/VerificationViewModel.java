package com.kiosk.accessbank.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.kiosk.accessbank.source.model.CustomerAccount;
import com.kiosk.accessbank.source.model.CustomerAccountResponse;
import com.kiosk.accessbank.source.repository.UserRepository;
import com.kiosk.accessbank.util.Constants;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class VerificationViewModel extends  BaseViewModel{

    private static final String TAG = VerificationViewModel.class.getSimpleName();
    private SavedStateHandle savedStateHandle;

    private UserRepository userRepository;


    @Inject
    public VerificationViewModel(SavedStateHandle savedStateHandle,UserRepository userRepository){
        this.savedStateHandle = savedStateHandle;
        this.userRepository = userRepository;
        check();

    }

    private void check(){
        String data = savedStateHandle.get("account_no");
        Log.d(TAG, data);

    }




    public void login(CustomerAccountLoginListener customerAccountLoginListener) {
        userRepository.getAccountDetails(savedStateHandle.get(Constants.ACCOUNT_NO_EXTRA)).subscribe(new SingleObserver<CustomerAccountResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull CustomerAccountResponse customerAccountResponse) {
                if (customerAccountResponse.getCode().equals("00")){
                    customerAccountLoginListener.onLogin(customerAccountResponse.getData().get(0));
                }else{
                    customerAccountLoginListener.onFail(customerAccountResponse.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                customerAccountLoginListener.onFail(e.getMessage());
            }
        });
    }

    public interface CustomerAccountLoginListener {
        void onLogin(CustomerAccount customerAccount);

        void onFail(String message);
    }
}
