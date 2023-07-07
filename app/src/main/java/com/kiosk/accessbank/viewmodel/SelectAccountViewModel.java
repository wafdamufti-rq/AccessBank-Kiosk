package com.kiosk.accessbank.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.kiosk.accessbank.source.model.AccountSummary;
import com.kiosk.accessbank.source.model.AccountSummaryResponse;
import com.kiosk.accessbank.source.model.CustomerAccountResponse;
import com.kiosk.accessbank.source.model.CustomerAccount;
import com.kiosk.accessbank.source.repository.UserRepository;
import com.kiosk.accessbank.util.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class SelectAccountViewModel extends BaseViewModel{

    private SavedStateHandle savedStateHandle;

    private UserRepository userRepository;



    private MutableLiveData<String> _accountLiveData = new MutableLiveData<>();
    public LiveData<String> accountLiveData = _accountLiveData;

    public SelectAccountViewModel(){

    }
    @Inject
    public SelectAccountViewModel( SavedStateHandle savedStateHandle, UserRepository userRepository) {
        this.savedStateHandle = savedStateHandle;
        this.userRepository = userRepository;

        _accountLiveData.postValue(savedStateHandle.get(Constants.ACCOUNT_NO_EXTRA));
    }


    private MutableLiveData<List<AccountSummary>> _customerAccountsLiveData = new MutableLiveData<>();
    public LiveData<List<AccountSummary>> customerAccountsLiveData = _customerAccountsLiveData;

    public void loadCustomers(){
        userRepository.getAccountSummaryByCustomerID(savedStateHandle.get("acc")).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull AccountSummaryResponse listApiResponse) {
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
