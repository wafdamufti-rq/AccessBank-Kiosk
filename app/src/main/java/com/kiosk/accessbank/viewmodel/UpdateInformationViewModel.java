package com.kiosk.accessbank.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.CustomerAccount;
import com.kiosk.accessbank.source.model.User;
import com.kiosk.accessbank.source.repository.UserRepository;
import com.kiosk.accessbank.util.Constants;
import com.kiosk.accessbank.util.UpdateType;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class UpdateInformationViewModel extends BaseViewModel{

    private SavedStateHandle savedStateHandle;

    private final MutableLiveData<CustomerAccount> _selectedAccountLiveData = new MutableLiveData<>();

    public LiveData<CustomerAccount> selectedAccountLiveData = _selectedAccountLiveData;

    private UserRepository userRepository;

    public UpdateInformationViewModel() {
    }

    @Inject
    public UpdateInformationViewModel(SavedStateHandle savedStateHandle, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.savedStateHandle = savedStateHandle;
    }



    private final MutableLiveData<ApiResponse<Object>> _submittedLiveData = new MutableLiveData<>();

    public LiveData<ApiResponse<Object>> submittedLiveData = _submittedLiveData;



    public void loadAccount(){
        _selectedAccountLiveData.postValue(savedStateHandle.get(Constants.ACCOUNT_EXTRA));
    }

    public void submitUpdate(UpdateType updateType, String toString) {
        CustomerAccount account = savedStateHandle.get(Constants.ACCOUNT_EXTRA);
        if (updateType == UpdateType.EMAIL){
            userRepository.changeEmail(savedStateHandle.get(account.account_no),toString).subscribe(new SingleObserver<>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    disposable.add(d);
                }

                @Override
                public void onSuccess(@NonNull ApiResponse<Object> objectApiResponse) {
                    _submittedLiveData.postValue(objectApiResponse);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    _submittedLiveData.postValue(new ApiResponse<>("01", e.getMessage(), new Object()));
                }
            });
        }else{
            userRepository.changePhoneNumber(savedStateHandle.get(account.account_no),toString).subscribe(new SingleObserver<>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    disposable.add(d);
                }

                @Override
                public void onSuccess(@NonNull ApiResponse<Object> objectApiResponse) {
                    _submittedLiveData.postValue(objectApiResponse);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    _submittedLiveData.postValue(new ApiResponse<>("01", e.getMessage(), new Object()));
                }
            });
        }
    }
}
