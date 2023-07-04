package com.kiosk.accessbank.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.CustomerAccount;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UpdateInformationViewModel extends BaseViewModel{

    private SavedStateHandle savedStateHandle;

    private final MutableLiveData<CustomerAccount> _selectedAccountLiveData = new MutableLiveData<>();

    public LiveData<CustomerAccount> selectedAccountLiveData = _selectedAccountLiveData;


    @Inject
    public UpdateInformationViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        loadAccount();
    }


    private void loadAccount(){
        _selectedAccountLiveData.postValue(savedStateHandle.get("customer_account"));
    }
}
