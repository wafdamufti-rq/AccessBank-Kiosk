package com.kiosk.accessbank.viewmodel;


import androidx.lifecycle.SavedStateHandle;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends BaseViewModel{

    private SavedStateHandle savedStateHandle;


    @Inject
    LoginViewModel(SavedStateHandle savedStateHandle){
        this.savedStateHandle = savedStateHandle;
    }
}
