package com.kiosk.accessbank.viewmodel;

import android.util.Log;

import androidx.lifecycle.SavedStateHandle;

import com.kiosk.accessbank.source.repository.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

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




}
