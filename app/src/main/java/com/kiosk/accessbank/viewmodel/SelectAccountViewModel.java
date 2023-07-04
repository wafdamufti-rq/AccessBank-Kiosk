package com.kiosk.accessbank.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.api.CustomerAccountResponse;
import com.kiosk.accessbank.source.model.CustomerAccount;
import com.kiosk.accessbank.source.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.assisted.Assisted;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class SelectAccountViewModel extends BaseViewModel{

    private SavedStateHandle savedStateHandle;

    private UserRepository userRepository;

    public SelectAccountViewModel(){

    }
    @Inject
    public SelectAccountViewModel( SavedStateHandle savedStateHandle, UserRepository userRepository) {
        this.savedStateHandle = savedStateHandle;
        this.userRepository = userRepository;
    }


    private MutableLiveData<List<CustomerAccount>> _customerAccountsLiveData = new MutableLiveData<>();
    public LiveData<List<CustomerAccount>> customerAccountsLiveData = _customerAccountsLiveData;

    public void loadCustomers(){
        userRepository.getAccountDetails(savedStateHandle.get("account_no")).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull CustomerAccountResponse listApiResponse) {
                if (listApiResponse.getCode().equals("00")){
                    _customerAccountsLiveData.postValue(listApiResponse.getData());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }


}
