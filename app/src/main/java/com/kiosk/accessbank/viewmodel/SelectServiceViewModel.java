package com.kiosk.accessbank.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.kiosk.accessbank.source.model.AccountSummary;
import com.kiosk.accessbank.util.Constants;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SelectServiceViewModel extends BaseViewModel{

    private SavedStateHandle savedStateHandle;

    public SelectServiceViewModel() {
    }

    @Inject
    public SelectServiceViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        observeAccountSummary();
    }

    private MutableLiveData<AccountSummary> _accountSummaryLiveData = new MutableLiveData<>();

    public LiveData<AccountSummary> accountSummaryLiveData = _accountSummaryLiveData;

    private void observeAccountSummary(){
        _accountSummaryLiveData.postValue(savedStateHandle.get(Constants.ACCOUNT_SUMMARY_EXTRA));
    }





}
